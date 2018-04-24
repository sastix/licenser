package com.sastix.licenser.commons.content;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class AccessCodeInfoDTO {

    @NotNull
    @NotEmpty
    private String accessCode;

    @NotNull
    @NotEmpty
    private Integer level;

    private Boolean isActivated;

    @NotNull
    @NotEmpty
    private Integer duration;

    private DateTime activationDate;

    private Long userId;

    /**
     * Default Constructor.
     */
    public AccessCodeInfoDTO() {
    }

    /**
     * Constructor with the mandatory fields.
     *
     * @param accessCode  a String with the access code
     * @param level       an Integer with the level
     * @param duration    an Integer that defines the duration that the access code is valid
     */
    public AccessCodeInfoDTO(final String accessCode,
                             final Integer level,
                             final Integer duration) {
        this.accessCode = accessCode;
        this.level = level;
        this.duration = duration;
    }

    /**
     * Constructor with all fields.
     *
     * @param accessCode  a String with the access code
     * @param level       an Integer with the level
     * @param isActivated a Boolean that defines if the access code is activated
     * @param duration    an Integer that defines the duration that the access code is valid
     */
    public AccessCodeInfoDTO(final String accessCode,
                             final Integer level,
                             Boolean isActivated,
                             final Integer duration) {
        this.accessCode = accessCode;
        this.level = level;
        this.isActivated = isActivated;
        this.duration = duration;
    }

    /**
     * Returns the Access Code.
     *
     * @return a String with the Access Code
     */
    public String getAccessCode() {
        return accessCode;
    }

    /**
     * Set the access code.
     *
     * @param accessCode a String with the access code
     */
    public void setAccessCode(final String accessCode) {
        this.accessCode = accessCode;
    }

    /**
     * Returns the Level.
     *
     * @return an Integer with the Level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * Set the Level.
     *
     * @param level an Integer with the level
     */
    public void setLevel(final Integer level) {
        this.level = level;
    }

    /**
     * Returns the activation status of the access code.
     *
     * @return a Boolean with the activation status
     */
    public Boolean getActivated() {
        return isActivated;
    }

    /**
     * Set activation status of the access code.
     *
     * @param activated a Boolean with the activation status
     */
    public void setActivated(final Boolean activated) {
        isActivated = activated;
    }

    /**
     * Returns the duration that this access code is valid.
     *
     * @return an Integer with the duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Set the duration that this access code is valid.
     *
     * @param duration an Integer with the duration
     */
    public void setDuration(final Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "AccessCodeInfoDTO{" +
                "accessCode='" + accessCode + '\'' +
                ", level='" + level + '\'' +
                ", isActivated=" + isActivated +
                ", duration='" + duration + '\'' +
                '}';
    }

    public DateTime getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(DateTime activationDate) {
        this.activationDate = activationDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
