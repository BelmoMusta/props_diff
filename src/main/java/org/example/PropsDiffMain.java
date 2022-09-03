package org.example;

import org.javers.core.ChangesByObject;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.PropertyChange;
import org.javers.core.diff.changetype.map.EntryAdded;
import org.javers.core.diff.changetype.map.EntryChange;
import org.javers.core.diff.changetype.map.EntryRemoved;
import org.javers.core.diff.changetype.map.EntryValueChange;
import org.javers.core.diff.changetype.map.KeyValueChange;
import org.javers.core.diff.changetype.map.MapChange;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
@ComponentScan
public class PropsDiffMain {

    private static final ApplicationContext APPLICATION_CONTEXT;
    private static final ApplicationContext APPLICATION_CONTEXT_2;

    static {

        APPLICATION_CONTEXT = new AnnotationConfigApplicationContext(PropsDiffMain.class);
        APPLICATION_CONTEXT_2 = new AnnotationConfigApplicationContext(PropsDiffMain.class);
    }

    public static void main(String[] args) throws Exception {
        StringBuilder srcName = new StringBuilder();
        StringBuilder destName = new StringBuilder();
        List<String> src = new ArrayList<>();
        List<String> dest = new ArrayList<>();


        fillFileLists(args,srcName, destName, src, dest);
        ConfigurableEnvironment srcEnv = (StandardEnvironment) APPLICATION_CONTEXT.getEnvironment();
        for (String fileLocation : src) {
            CustomPropertySource fileLocationResource = new CustomPropertySource(new File(fileLocation));
            srcEnv.getPropertySources().addLast(fileLocationResource);
        }

        ConfigurableEnvironment destEnv = (StandardEnvironment) APPLICATION_CONTEXT_2.getEnvironment();
        for (String fileLocation : dest) {
            CustomPropertySource fileLocationResource = new CustomPropertySource(new File(fileLocation));
            destEnv.getPropertySources().addLast(fileLocationResource);
        }

        Javers javers = JaversBuilder.javers().build();

        Map<String, Object> srcProps = getAllProps(srcEnv);
        Map<String, Object> destProps = getAllProps(destEnv);


        Diff diff = javers.compare(srcProps, destProps);
        List<DiffContent> list = new ArrayList<>();
        for (ChangesByObject changesByObject : diff.groupByObject()) {
            for (PropertyChange propertyChange : changesByObject.getPropertyChanges()) {
                KeyValueChange<EntryChange> mapChange = (MapChange) propertyChange;
                for (EntryChange entryChange : mapChange.getEntryChanges()) {
                    DiffContent myChange = new DiffContent();
                    myChange.setDiffType(entryChange.getClass().getSimpleName());
                    fillCustoChange(entryChange, myChange);
                    list.add(myChange);
                }
            }
        }
        VelocityGenerator.generateHtmlFile(list);
    }

    static void fillCustoChange(EntryChange entryChange, DiffContent destObject) {

        String key = String.valueOf(entryChange.getKey());
        String left = "";
        String right = "";
        String type = "";
        String cssClasses = "";
        if (entryChange instanceof EntryValueChange) {
            left = String.valueOf(((EntryValueChange) entryChange).getLeftValue());
            right = String.valueOf(((EntryValueChange) entryChange).getRightValue());
            type = "CHANGED";
            cssClasses += " alerte alerte-success";

        } else if (entryChange instanceof EntryAdded) {
            left = "NOT FOUND";
            right = String.valueOf(((EntryAdded) entryChange).getValue());
            type = "ADDED";
            cssClasses += " warning";

        } else if (entryChange instanceof EntryRemoved) {
            left = String.valueOf(((EntryRemoved) entryChange).getValue());
            right = "NOT FOUND";
            type = "REMOVED";
            cssClasses += " danger";

        }
        destObject.setKey(key);
        destObject.setSrcValue(left);
        destObject.setDestValue(right);
        destObject.setDiffType(type);
        destObject.setCssClasses(cssClasses);
    }

    private static void fillFileLists(String[] args,
                                      StringBuilder srcName,
                                      StringBuilder destName,
                                      List<String> src,
                                      List<String> dest) {

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("--src".equals(arg)) {
                srcName.append(args[i+1]);
                for (int j = i + 2; j < args.length; j++) {
                    if (!"--dest".equals(args[j])) {
                        src.add(args[j]);
                    } else break;
                }
            }

            if ("--dest".equals(arg)) {
                destName.append(args[i+1]);
                for (int j = i + 2; j < args.length; j++) {
                    if (!"--src".equals(args[j])) {
                        dest.add(args[j]);
                    } else break;
                }
            }
        }
    }

    public static Map<String, Object> getAllProps(ConfigurableEnvironment environment) {
        Map<String, Object> map = new HashMap<>();
        for (Iterator it = environment.getPropertySources().iterator(); it.hasNext(); ) {
            PropertySource propertySource = (PropertySource) it.next();
            if (propertySource instanceof CustomPropertySource) {
                CustomPropertySource mSource = (CustomPropertySource) propertySource;
                String[] propertyNames = mSource.getPropertyNames();
                for (String propertyName : propertyNames) {
                    try {
                        map.put(propertyName, environment.getProperty(propertyName));
                    } catch (IllegalArgumentException ex) {
                        map.put(propertyName, mSource.getProperty(propertyName));
                    }
                }

            }
        }

        return map;
    }
}
