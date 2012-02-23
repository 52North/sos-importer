package org.n52.sos.importer.interfaces;

/**
 * interface for objects which can be formatted along a 
 * certain pattern into a String
 * (e.g. Java-Date + pattern "yyyy-MM-dd" --> "2011-08-04")
 * @author Raimund
 *
 */
public interface Formatable {

	/**
	 * formats an object along a certain pattern into a String
	 * @param o
	 * @return
	 */
	public abstract String format(Object o);
}
