
package com.micdog.openflag.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "flags", indexes = { @Index(name="ux_flag_key", columnList = "flagKey", unique = true)})
public class Flag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String flagKey;

    private boolean enabled = false;
    private Integer percentage = 0; // 0..100

    @Lob
    private String includeIds; // csv

    @Lob
    private String excludeIds; // csv

    private String attributeKey; // e.g. country
    @Lob
    private String attributeAllowedCsv; // csv

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void touch() { this.updatedAt = Instant.now(); }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFlagKey() { return flagKey; }
    public void setFlagKey(String flagKey) { this.flagKey = flagKey; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Integer getPercentage() { return percentage == null ? 0 : percentage; }
    public void setPercentage(Integer percentage) { this.percentage = percentage; }

    public String getIncludeIds() { return includeIds; }
    public void setIncludeIds(String includeIds) { this.includeIds = includeIds; }

    public String getExcludeIds() { return excludeIds; }
    public void setExcludeIds(String excludeIds) { this.excludeIds = excludeIds; }

    public String getAttributeKey() { return attributeKey; }
    public void setAttributeKey(String attributeKey) { this.attributeKey = attributeKey; }

    public String getAttributeAllowedCsv() { return attributeAllowedCsv; }
    public void setAttributeAllowedCsv(String attributeAllowedCsv) { this.attributeAllowedCsv = attributeAllowedCsv; }
}
