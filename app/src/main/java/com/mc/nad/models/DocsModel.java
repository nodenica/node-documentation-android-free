package com.mc.nad.models;

public class DocsModel {
    String version;
    ModuleModel[] modules;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ModuleModel[] getModules() {
        return modules;
    }

    public void setModules(ModuleModel[] modules) {
        this.modules = modules;
    }
}
