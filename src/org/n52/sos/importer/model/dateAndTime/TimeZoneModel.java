package org.n52.sos.importer.model.dateAndTime;

public class TimeZoneModel {

	private int UTCOffset;
	
	public TimeZoneModel(int UTCOffset) {
		this.UTCOffset = UTCOffset;
	}
	
	public void setUTCOffset(int UTCOffset) {
		this.UTCOffset = UTCOffset;
	}

	public int getUTCOffset() {
		return UTCOffset;
	}
}
