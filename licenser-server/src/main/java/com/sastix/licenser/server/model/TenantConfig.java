package com.sastix.licenser.server.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "tenant_config")
public class TenantConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "component", length = 8)
    private String component;

    @Column(name = "data", columnDefinition = "TEXT")
    @Lob
    private String data;

    @Column(name = "name", length = 64)
    private String name;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getComponent() { return component; }

    public void setComponent(String component) { this.component = component; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getData() { return data; }

    public void setData(String data) { this.data = data; }
}
