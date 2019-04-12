package  com.isoftstone.common.jd;

import com.isoftstone.common.IDEntity;

public class SyncScheduleDto extends IDEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3129239465571538918L;
	private String syncTime;

    public String getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(String syncTime) {
        this.syncTime = syncTime == null ? null : syncTime.trim();
    }
}