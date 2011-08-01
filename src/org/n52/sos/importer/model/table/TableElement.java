package org.n52.sos.importer.model.table;

import java.util.HashSet;

public abstract class TableElement {

	public abstract void mark();
	
	public abstract String getValueFor(Cell c);
	
	public abstract Cell getCellFor(Cell c);
	
	/**
	 * retrieves all values in this column, row or cell
	 * @return
	 */
	public abstract HashSet<String> getValues();
}
