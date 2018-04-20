package com.sastix.licenser.commons.content;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AccessCodeDTO {

    @NotNull
    private String accessCode;

    @NotNull
    private Integer level;

    private BigDecimal price;

    @NotNull
    private Integer duration;

    private Boolean isActivated;

    private Boolean deleted;

    @NotNull
    private Integer tenantId;

    public AccessCodeDTO() {

    }

    public AccessCodeDTO(final String accessCode, final Integer level, final Integer duration, final Integer tenantId) {
        this.accessCode = accessCode;
        this.level = level;
        this.duration = duration;
        this.tenantId = tenantId;
    }


    public String getAccessCode() { return accessCode; }

    public void setAccessCode(String accessCode) { this.accessCode = accessCode; }

    public Integer getLevel() { return level; }

    public void setLevel(Integer level) { this.level = level; }

    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getDuration() { return duration; }

    public void setDuration(Integer duration) { this.duration = duration; }

    public Boolean getActivated() { return isActivated; }

    public void setActivated(Boolean activated) { isActivated = activated; }

    public Boolean getDeleted() { return deleted; }

    public void setDeleted(Boolean deleted) { this.deleted = deleted; }

    public Integer getTenantId() { return tenantId; }

    public void setTenantId(Integer tenantId) { this.tenantId = tenantId; }
}
