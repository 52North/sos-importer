package org.n52.sos.importer.model.table;

import java.awt.Color;

public abstract class TableElement {

	public abstract void mark(Color color);
	
	public abstract String getValueFor(Cell c);
}
