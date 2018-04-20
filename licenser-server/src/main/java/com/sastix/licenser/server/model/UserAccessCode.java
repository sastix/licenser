package com.sastix.licenser.server.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Table(name = "user_access_code")
public class UserAccessCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_code_id")
    private AccessCode accessCode;

    @Column(name = "activation_date", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime activationDate;

    public UserAccessCode(){}

    public UserAccessCode(AccessCode accessCode, Long userId, DateTime activationDate){
        this.accessCode = accessCode;
        this.userId = userId;
        this.activationDate = activationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) { this.id = id; }

    public long getUserId() { return userId; }

    public void setUserId(long userId) { this.userId = userId; }

    public AccessCode getAccessCode() { return accessCode; }

    public void setAccessCode(AccessCode accessCode) { this.accessCode = accessCode; }

    public DateTime getActivationDate() { return activationDate; }

    public void setActivationDate(DateTime activationDate) { this.activationDate = activationDate; }
}

