/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.importer.model.position;

import java.text.MessageFormat;
import java.text.ParseException;

import org.n52.sos.importer.model.Combination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Position extends Combination {

	private static final Logger logger = LoggerFactory.getLogger(Position.class);
	
	private Latitude latitude;
	
	private Longitude longitude;
	
	private Height height;
	
	private EPSGCode epsgCode;

	private String group;
	
	public Position() {
		super();
	}
	
	public Position(final Latitude latitude, final Longitude longitude, final Height height,
			final EPSGCode epsgCode) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.height = height;
		this.epsgCode = epsgCode;
	}

	public Height getHeight() {
		return height;
	}

	public void setHeight(final Height height) {
		if (getGroup() != null) {
			if (height != null) {
				logger.info("Add " + height + " to " + this);
			} else {
				logger.info("Remove " + this.height + " from " + this);
			}
		}
		this.height = height;
	}

	public EPSGCode getEPSGCode() {
		return epsgCode;
	}

	public void setEPSGCode(final EPSGCode epsgCode) {
		if (getGroup() != null) {
			if (epsgCode != null) {
				logger.info("Add " + epsgCode + " to " + this);
			} else {
				logger.info("Remove " + this.epsgCode + " from " + this);
			}
		}
		this.epsgCode = epsgCode;
	}

	public void setLongitude(final Longitude longitude) {
		if (getGroup() != null) {
			if (longitude != null) {
				logger.info("Add " + longitude + " to " + this);
			} else {
				logger.info("Remove " + this.longitude + " from " + this);
			}
		}
		this.longitude = longitude;
	}

	public Longitude getLongitude() {
		return longitude;
	}

	public void setLatitude(final Latitude latitude) {
		if (getGroup() != null) {
			if (latitude != null) {
				logger.info("Add " + latitude + " to " + this);
			} else {
				logger.info("Remove " + this.latitude + " from " + this);
			}
		}
		this.latitude = latitude;
	}

	public Latitude getLatitude() {
		return latitude;
	}

	@Override
	public void setGroup(final String group) {
		this.group = group;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public String format(final Object o) {
		final Position p = (Position) o;
		String positionString = getPattern();
		// TODO remove explicit string from here
		positionString = positionString.replaceAll("LAT", p.getLatitude().getValue() + p.getLatitude().getUnit());
		positionString = positionString.replaceAll("LON", p.getLongitude().getValue() + p.getLongitude().getUnit());
		positionString = positionString.replaceAll("ALT", p.getHeight().getValue() + p.getHeight().getUnit());
		positionString = positionString.replaceAll("EPSG", p.getEPSGCode().getValue() + "");
		return positionString;
	}

	@Override
	public Position parse(final String s) {
		String pattern = getPattern();
		
		pattern = pattern.replaceAll("LAT", "{0}");
		pattern = pattern.replaceAll("LON", "{1}");
		pattern = pattern.replaceAll("ALT", "{2}");
		pattern = pattern.replaceAll("EPSG", "{3}");
		
		final MessageFormat mf = new MessageFormat(pattern);
		Object[] o = null;
		try {
			o = mf.parse(s);
		} catch (final ParseException e) {
			throw new NumberFormatException();
		}
		
		if (o == null) {
			throw new NumberFormatException();
		}
			
		Latitude latitude = null;
		Longitude longitude = null;
		Height height = null;
		EPSGCode epsgCode = null;
			
		if (o.length > 0 && o[0] != null) {
			latitude = Latitude.parse((String) o[0]);
		}
		if (o.length > 1 && o[1] != null) {
			longitude = Longitude.parse((String)o[1]);
		}
		if (o.length > 2 && o[2] != null) {
			height = Height.parse((String)o[2]);
		}
		if (o.length > 3 && o[3] != null) {
			epsgCode = EPSGCode.parse((String)o[3]);
		}
		return new Position(latitude, longitude, height, epsgCode);
	}

	@Override
	public String toString() {
		if (getGroup() == null) {
			return "Position (" + latitude + ", " + longitude + ", " 
			+ height + ", " + epsgCode + ")";
		} else {
			return "Position group " + getGroup();
		}
	}
}
