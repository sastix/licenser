package com.sastix.licenser.commons.content;

import javax.validation.constraints.NotNull;

public class TenantDTO {

    @NotNull
    private Integer id;

    private String name;

    private String description;

    private Integer level;

    private Integer parentId;

    public TenantDTO() {

    }

    public TenantDTO(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
