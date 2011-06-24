package org.n52.sos.importer.model.position;

import org.n52.sos.importer.model.table.TableElement;

public class Latitude extends PositionComponentModel {

	public Latitude(TableElement tableElement) {
		super(tableElement);
	}
	
	public Latitude(String value) {
		super(value);
	}
}
