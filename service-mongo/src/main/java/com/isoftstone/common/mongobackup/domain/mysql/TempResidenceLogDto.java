package com.isoftstone.common.mongobackup.domain.mysql;

public class TempResidenceLogDto  {
    private String faceId;

    private Long residenceTime;

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public Long getResidenceTime() {
        return residenceTime;
    }

    public void setResidenceTime(Long residenceTime) {
        this.residenceTime = residenceTime;
    }
}