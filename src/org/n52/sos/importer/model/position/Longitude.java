package org.n52.sos.importer.model.position;

import org.n52.sos.importer.model.table.TableElement;

public class Longitude extends PositionComponentModel {

	public Longitude(TableElement tableElement, String pattern) {
		super(tableElement, pattern);
	}
	
	public Longitude(double value, String unit) {
		super(value, unit);
	}
	
	@Override
	public String toString() {
		return "Longitude" + super.toString();
	}
}
