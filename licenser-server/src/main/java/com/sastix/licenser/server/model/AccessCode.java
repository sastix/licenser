package com.sastix.licenser.server.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "access_code", uniqueConstraints = {@UniqueConstraint(columnNames = {"access_code", "tenant_id"})})
public class AccessCode {

    public AccessCode() {
    }

    public AccessCode(String accessCode, Integer level, Integer duration) {
        this.accessCode = accessCode;
        this.level = level;
        this.duration = duration;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "access_code")
    private String accessCode;

    @Column(name = "level")
    private Integer level;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "is_activated", nullable = false)
    private boolean isActivated;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @OneToMany(mappedBy = "accessCode")
    private Set<PackageAccessCode> packageAccessCodes = new HashSet<PackageAccessCode>(0);

    @OneToOne(mappedBy = "accessCode", cascade = CascadeType.PERSIST)
    private UserAccessCode userAccessCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Boolean getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<PackageAccessCode> getPackageAccessCodes() {
        return packageAccessCodes;
    }

    public void setPackageAccessCodes(Set<PackageAccessCode> packageAccessCodes) {
        this.packageAccessCodes = packageAccessCodes;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public UserAccessCode getUserAccessCode() {
        return userAccessCode;
    }

    public void setUserAccessCode(UserAccessCode userAccessCode) {
        this.userAccessCode = userAccessCode;
    }
}
