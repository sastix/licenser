package com.sastix.licenser.commons.content;

import javax.validation.constraints.NotNull;

public class ActivateAccessCodeDTO {

    @NotNull
    private String accessCode;

    @NotNull
    private Long userId;

    public ActivateAccessCodeDTO() {

    }

    public ActivateAccessCodeDTO(String accessCode, Long userId) {
        this.accessCode = accessCode;
        this.userId = userId;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void set_AccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
