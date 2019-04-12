package com.isoftstone.common.jd;

import com.isoftstone.common.IDEntity;
import java.math.BigDecimal;

public class StatisticsDto extends IDEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3716022491691453063L;

    private String unitId;

    private String dateTime;

    private Long dateTimeMillis;

    private Long onetonighteenCount;

    private Long twentyOtonineCount;

    private Long thirtyOtonineCount;

    private Long fortyOtonineCount;

    private Long fiftyOtonineCount;

    private Long maleCount;

    private Long femaleCount;

    private Long currentTotalCount;

    private Long currentTotalInCount;

    private float countRateOverYesterday;

    private float countRateOverLastweek;

    private long avgResidenceTime;

    private float avgRateOverYesterday;
    private float avgRateOverLastweek; 
    
    private Long otooneResidenceCount;

    private Long onetothreeResidenceCount;

    private Long threetofiveResidenceCount;

    private Long fivetotenResidenceCount;

    private Long tentothirtyResidenceCount;

    private Long overthirtyResidenceCount;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId == null ? null : unitId.trim();
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime == null ? null : dateTime.trim();
    }

    public Long getDateTimeMillis() {
        return dateTimeMillis;
    }

    public void setDateTimeMillis(Long dateTimeMillis) {
        this.dateTimeMillis = dateTimeMillis;
    }

    public Long getOnetonighteenCount() {
        return onetonighteenCount;
    }

    public void setOnetonighteenCount(Long onetonighteenCount) {
        this.onetonighteenCount = onetonighteenCount;
    }

    public Long getTwentyOtonineCount() {
        return twentyOtonineCount;
    }

    public void setTwentyOtonineCount(Long twentyOtonineCount) {
        this.twentyOtonineCount = twentyOtonineCount;
    }

    public Long getThirtyOtonineCount() {
        return thirtyOtonineCount;
    }

    public void setThirtyOtonineCount(Long thirtyOtonineCount) {
        this.thirtyOtonineCount = thirtyOtonineCount;
    }

    public Long getFortyOtonineCount() {
        return fortyOtonineCount;
    }

    public void setFortyOtonineCount(Long fortyOtonineCount) {
        this.fortyOtonineCount = fortyOtonineCount;
    }

    public Long getFiftyOtonineCount() {
        return fiftyOtonineCount;
    }

    public void setFiftyOtonineCount(Long fiftyOtonineCount) {
        this.fiftyOtonineCount = fiftyOtonineCount;
    }

    public Long getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(Long maleCount) {
        this.maleCount = maleCount;
    }

    public Long getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(Long femaleCount) {
        this.femaleCount = femaleCount;
    }

    public Long getCurrentTotalCount() {
        return currentTotalCount;
    }

    public void setCurrentTotalCount(Long currentTotalCount) {
        this.currentTotalCount = currentTotalCount;
    }

    public Long getCurrentTotalInCount() {
        return currentTotalInCount;
    }

    public void setCurrentTotalInCount(Long currentTotalInCount) {
        this.currentTotalInCount = currentTotalInCount;
    }
 
    public long getAvgResidenceTime() {
        return avgResidenceTime;
    }

    public void setAvgResidenceTime(long avgResidenceTime) {
        this.avgResidenceTime = avgResidenceTime;
    }

    public Long getOtooneResidenceCount() {
        return otooneResidenceCount;
    }

    public void setOtooneResidenceCount(Long otooneResidenceCount) {
        this.otooneResidenceCount = otooneResidenceCount;
    }

    public Long getOnetothreeResidenceCount() {
        return onetothreeResidenceCount;
    }

    public void setOnetothreeResidenceCount(Long onetothreeResidenceCount) {
        this.onetothreeResidenceCount = onetothreeResidenceCount;
    }

    public Long getThreetofiveResidenceCount() {
        return threetofiveResidenceCount;
    }

    public void setThreetofiveResidenceCount(Long threetofiveResidenceCount) {
        this.threetofiveResidenceCount = threetofiveResidenceCount;
    }

    public Long getFivetotenResidenceCount() {
        return fivetotenResidenceCount;
    }

    public void setFivetotenResidenceCount(Long fivetotenResidenceCount) {
        this.fivetotenResidenceCount = fivetotenResidenceCount;
    }

    public Long getTentothirtyResidenceCount() {
        return tentothirtyResidenceCount;
    }

    public void setTentothirtyResidenceCount(Long tentothirtyResidenceCount) {
        this.tentothirtyResidenceCount = tentothirtyResidenceCount;
    }

    public Long getOverthirtyResidenceCount() {
        return overthirtyResidenceCount;
    }

    public void setOverthirtyResidenceCount(Long overthirtyResidenceCount) {
        this.overthirtyResidenceCount = overthirtyResidenceCount;
    }

	public float getAvgRateOverYesterday() {
		return avgRateOverYesterday;
	}

	public void setAvgRateOverYesterday(float avgRateOverYesterday) {
		this.avgRateOverYesterday = avgRateOverYesterday;
	}

	public float getAvgRateOverLastweek() {
		return avgRateOverLastweek;
	}

	public void setAvgRateOverLastweek(float avgRateOverLastweek) {
		this.avgRateOverLastweek = avgRateOverLastweek;
	}

	public float getCountRateOverLastweek() {
		return countRateOverLastweek;
	}

	public void setCountRateOverLastweek(float countRateOverLastweek) {
		this.countRateOverLastweek = countRateOverLastweek;
	}

	public float getCountRateOverYesterday() {
		return countRateOverYesterday;
	}

	public void setCountRateOverYesterday(float countRateOverYesterday) {
		this.countRateOverYesterday = countRateOverYesterday;
	}
}