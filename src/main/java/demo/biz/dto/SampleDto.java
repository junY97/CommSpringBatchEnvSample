package demo.biz.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects; // If you're using Objects.hash/equals

public class SampleDto {
    private Integer sampleId;
    private String sampleName;
    private String sampleDescription;
    private BigDecimal sampleValue;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ==> THIS IS CRUCIAL <==
    public SampleDto() {
        // Default no-argument constructor
    }

    // You might have other constructors, which is fine
    public SampleDto(Integer sampleId, String sampleName, /*... other fields ...*/ LocalDateTime updatedAt) {
        this.sampleId = sampleId;
        this.sampleName = sampleName;
        // ... initialize other fields ...
        this.updatedAt = updatedAt;
    }

    // Ensure all your getters and setters are correctly implemented
    public Integer getSampleId() {
        return sampleId;
    }

    public void setSampleId(Integer sampleId) {
        this.sampleId = sampleId;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getSampleDescription() {
        return sampleDescription;
    }

    public void setSampleDescription(String sampleDescription) {
        this.sampleDescription = sampleDescription;
    }

    public BigDecimal getSampleValue() {
        return sampleValue;
    }

    public void setSampleValue(BigDecimal sampleValue) {
        this.sampleValue = sampleValue;
    }

    public Boolean getActive() { // Getter for boolean can be isIsActive or getIsActive or getActive
        return isActive;
    }

    public void setActive(Boolean active) { // Setter name should match (e.g., if DB is IS_ACTIVE, field isActive, setter setActive)
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // It's also good practice to have toString(), equals(), and hashCode()
    @Override
    public String toString() {
        return "SampleDto{" +
                "sampleId=" + sampleId +
                ", sampleName='" + sampleName + '\'' +
                ", sampleDescription='" + sampleDescription + '\'' +
                ", sampleValue=" + sampleValue +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // Add equals() and hashCode() if needed, for example, using Objects.hash/equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleDto sampleDto = (SampleDto) o;
        return Objects.equals(sampleId, sampleDto.sampleId) && Objects.equals(sampleName, sampleDto.sampleName) && Objects.equals(sampleDescription, sampleDto.sampleDescription) && Objects.equals(sampleValue, sampleDto.sampleValue) && Objects.equals(isActive, sampleDto.isActive) && Objects.equals(createdAt, sampleDto.createdAt) && Objects.equals(updatedAt, sampleDto.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sampleId, sampleName, sampleDescription, sampleValue, isActive, createdAt, updatedAt);
    }
}
    