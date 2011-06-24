package org.n52.sos.importer.model.position;

public class Position {

	private Longitude longitude;
	
	private Latitude latitude;
	
	private Height height;
	
	private EPSGCode epsgCode;

	public Height getHeight() {
		return height;
	}

	public void setHeight(Height height) {
		this.height = height;
	}

	public EPSGCode getEPSGCode() {
		return epsgCode;
	}

	public void setEPSGCode(EPSGCode epsgCode) {
		this.epsgCode = epsgCode;
	}

	public void setLongitude(Longitude longitude) {
		this.longitude = longitude;
	}

	public Longitude getLongitude() {
		return longitude;
	}

	public void setLatitude(Latitude latitude) {
		this.latitude = latitude;
	}

	public Latitude getLatitude() {
		return latitude;
	}
	
}
