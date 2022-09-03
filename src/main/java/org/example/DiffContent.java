package org.example;

public class DiffContent {
    String key;
    String srcName = "22.5";
    String destName = "22.6";
    String srcValue;
    String destValue;
    String diffType;

    String cssClasses;

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public String getSrcValue() {
        return srcValue;
    }

    public void setSrcValue(String srcValue) {
        this.srcValue = srcValue;
    }

    public String getDestValue() {
        return destValue;
    }

    public void setDestValue(String destValue) {
        this.destValue = destValue;
    }

    public String getDiffType() {
        return diffType;
    }

    public void setDiffType(String diffType) {
        this.diffType = diffType;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "{" + diffType + "}"
                + srcName + "[" + key + "]="
                + srcValue + '|' + destName
                + "[" + key + "]=" + destValue;
    }

    public String getCssClasses() {
        return cssClasses;
    }

    public void setCssClasses(String cssClasses) {
        this.cssClasses = cssClasses;
    }
}
