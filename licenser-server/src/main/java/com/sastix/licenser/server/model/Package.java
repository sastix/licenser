package com.sastix.licenser.server.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "package")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "category", length = 64)
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "max_users")
    private Integer maxUsers;

    @OneToMany(mappedBy = "mPackage")
    private Set<PackageAccessCode> packageAccessCodes = new HashSet<PackageAccessCode>(0);

    @OneToMany(mappedBy = "mPackage")
    private Set<MaxUsersByPackageGlobalRole> maxUsersByPackageGlobalRoles = new HashSet<MaxUsersByPackageGlobalRole>(0);

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getDuration() { return duration; }

    public void setDuration(Integer duration) { this.duration = duration; }

    public Integer getMaxUsers() { return maxUsers; }

    public void setMaxUsers(Integer maxUsers) { this.maxUsers = maxUsers; }

    public Set<PackageAccessCode> getPackageAccessCodes() { return packageAccessCodes; }

    public void setPackageAccessCodes(Set<PackageAccessCode> packageAccessCodes) { this.packageAccessCodes = packageAccessCodes; }

    public Set<MaxUsersByPackageGlobalRole> getMaxUsersByPackageGlobalRoles() { return maxUsersByPackageGlobalRoles; }

    public void setMaxUsersByPackageGlobalRoles(Set<MaxUsersByPackageGlobalRole> maxUsersByPackageGlobalRoles) { this.maxUsersByPackageGlobalRoles = maxUsersByPackageGlobalRoles; }
}
