package com.sastix.licenser.server.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tenant")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "level")
    private Integer level;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Tenant parentId;

    @OneToMany
    @JoinColumn(name = "tenant_id")
    private List<TenantConfig> config = new ArrayList<TenantConfig>(0);

    @OneToMany
    @JoinColumn(name = "tenant_id")
    private List<AccessCode> accessCodes = new ArrayList<AccessCode>(0);

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() {  return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }


    public List<TenantConfig> getConfig() {
        return config;
    }

    public void setConfig(List<TenantConfig> config) {
        this.config = config;
    }

    public List<AccessCode> getAccessCodes() {
        return accessCodes;
    }

    public void setAccessCodes(List<AccessCode> accessCodes) {
        this.accessCodes = accessCodes;
    }

    public Integer getLevel() { return level; }

    public void setLevel(Integer level) { this.level = level; }

    public Tenant getParentId() { return parentId; }

    public void setParentId(Tenant parentId) { this.parentId = parentId; }
}
