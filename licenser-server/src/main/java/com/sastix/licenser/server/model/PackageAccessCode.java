package com.sastix.licenser.server.model;

import javax.persistence.*;

@Entity
@Table(name = "package_access_code")
public class PackageAccessCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package mPackage;

    @ManyToOne
    @JoinColumn(name = "access_code_id")
    private AccessCode accessCode;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Package getmPackage() { return mPackage; }

    public void setmPackage(Package mPackage) { this.mPackage = mPackage; }

    public AccessCode getAccessCode() { return accessCode; }

    public void setAccessCode(AccessCode accessCode) { this.accessCode = accessCode; }
}
