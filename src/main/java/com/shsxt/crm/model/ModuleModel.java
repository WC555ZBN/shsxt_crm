package com.shsxt.crm.model;

import com.shsxt.crm.dto.ModuleDto;

import java.util.List;

public class ModuleModel {

    private Integer id;
    private String moduleName;
    private String moduleStyle;
    private String url;
    private List<ModuleDto> subModule;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleStyle() {
        return moduleStyle;
    }

    public void setModuleStyle(String moduleStyle) {
        this.moduleStyle = moduleStyle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ModuleDto> getSubModule() {
        return subModule;
    }

    public void setSubModule(List<ModuleDto> subModule) {
        this.subModule = subModule;
    }
}
