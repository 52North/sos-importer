package org.n52.sos.importer.model.table;

import java.util.HashSet;

/**
 * can either be a column, row or cell in the table
 * @author Raimund
 *
 */
public abstract class TableElement {

	/**
	 * colors this table element in the table
	 */
	public abstract void mark();
	
	/**
	 * returns the corresponding value of another metadata type
	 * for a measured value or feature of interest cell
	 */
	public abstract String getValueFor(Cell c);
	
	/**
	 * returns the corresponding cell of another metadata type
	 * for a measured value or feature of interest cell
	 */
	public abstract Cell getCellFor(Cell c);
	
	/**
	 * retrieves all values in this column, row or cell
	 */
	public abstract HashSet<String> getValues();
}
