package com.sastix.licenser.server.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_global_role")
public class UserGlobalRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "global_role_id")
    private GlobalRole globalRole;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "activation_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime activationDate;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public DateTime getActivationDate() { return activationDate; }

    public void setActivationDate(DateTime activationDate) { this.activationDate = activationDate; }

    public long getUserId() { return userId; }

    public void setUserId(long user_id) { this.userId = user_id; }

    public GlobalRole getGlobalRole() { return globalRole; }

    public void setGlobalRole(GlobalRole globalRole) { this.globalRole = globalRole; }
}
