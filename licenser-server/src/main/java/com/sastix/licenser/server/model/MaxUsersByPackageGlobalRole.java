package com.sastix.licenser.server.model;

import javax.persistence.*;

@Entity
@Table(name = "max_users_by_package_global_role")
public class MaxUsersByPackageGlobalRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package mPackage;

    @ManyToOne
    @JoinColumn(name = "global_role_id")
    private GlobalRole globalRole;

    @Column(name = "max_allowed")
    private Integer maxAllowed;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Package getmPackage() { return mPackage; }

    public void setmPackage(Package mPackage) { this.mPackage = mPackage; }

    public GlobalRole getGlobalRole() { return globalRole; }

    public void setGlobalRole(GlobalRole globalRole) { this.globalRole = globalRole; }

    public Integer getMaxAllowed() { return maxAllowed; }

    public void setMaxAllowed(Integer maxAllowed) { this.maxAllowed = maxAllowed;  }
}
