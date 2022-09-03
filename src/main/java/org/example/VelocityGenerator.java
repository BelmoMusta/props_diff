package org.example;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class VelocityGenerator {

    public static void generateHtmlFile(List<DiffContent> diffContent) throws IOException {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        ve.init();
        Template t = ve.getTemplate("template.vm.html");
        VelocityContext context = new VelocityContext();
        context.put("diffContent", diffContent);
        context.put("SRC", "22.5");
        context.put("DEST", "22.6");
        FileWriter fileWriter = new FileWriter("index.html");
        t.merge(context, fileWriter);
        fileWriter.close();
    }

}
