package org.n52.sos.importer.interfaces;

/**
 * interface for objects which can convert Strings with the help 
 * of a certain pattern into this object
 * (e.g. "2011-08-04" + pattern "yyyy-MM-dd" --> DateAndTime object)
 * @author Raimund
 *
 */
public abstract interface Parseable {

	/**
	 * converts a String with the help of a certain pattern 
	 * into a user-defined object
	 * @param s
	 * @return
	 */
	public abstract Object parse(String s);
}
