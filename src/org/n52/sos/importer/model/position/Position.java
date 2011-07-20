package org.n52.sos.importer.model.position;

import org.apache.log4j.Logger;
import org.n52.sos.importer.Combination;

public class Position extends Combination {

	private static final Logger logger = Logger.getLogger(Position.class);
	
	private Longitude longitude;
	
	private Latitude latitude;
	
	private Height height;
	
	private EPSGCode epsgCode;

	private String group;
	
	public Height getHeight() {
		return height;
	}

	public void setHeight(Height height) {
		logger.info("Add " + height + " to " + this);
		this.height = height;
	}

	public EPSGCode getEPSGCode() {
		return epsgCode;
	}

	public void setEPSGCode(EPSGCode epsgCode) {
		logger.info("Add " + epsgCode + " to " + this);
		this.epsgCode = epsgCode;
	}

	public void setLongitude(Longitude longitude) {
		logger.info("Add " + longitude + " to " + this);
		this.longitude = longitude;
	}

	public Longitude getLongitude() {
		return longitude;
	}

	public void setLatitude(Latitude latitude) {
		logger.info("Add " + latitude + " to " + this);
		this.latitude = latitude;
	}

	public Latitude getLatitude() {
		return latitude;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getGroup() {
		return group;
	}
	
	@Override
	public String toString() {
		return "Position group " + getGroup();
	}

	@Override
	public String format(Object o) {
		return (String)o;
	}

	@Override
	public Object parse(String s) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
