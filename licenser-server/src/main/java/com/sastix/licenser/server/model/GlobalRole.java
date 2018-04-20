package com.sastix.licenser.server.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "global_role")
public class GlobalRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "role_name", length = 64)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany
    @JoinColumn(name = "global_role_id")
    private List<UserGlobalRole> userGlobalRoles = new ArrayList<UserGlobalRole>(0);

    @OneToMany(mappedBy = "globalRole")
    private Set<MaxUsersByPackageGlobalRole> maxUsersByPackageGlobalRoleSet = new HashSet<MaxUsersByPackageGlobalRole>(0);

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public List<UserGlobalRole> getUserGlobalRoles() { return userGlobalRoles; }

    public void setUserGlobalRoles(List<UserGlobalRole> userGlobalRoles) { this.userGlobalRoles = userGlobalRoles; }

    public Set<MaxUsersByPackageGlobalRole> getMaxUsersByPackageGlobalRoleSet() { return maxUsersByPackageGlobalRoleSet; }

    public void setMaxUsersByPackageGlobalRoleSet(Set<MaxUsersByPackageGlobalRole> maxUsersByPackageGlobalRoleSet) {
        this.maxUsersByPackageGlobalRoleSet = maxUsersByPackageGlobalRoleSet;
    }
}
