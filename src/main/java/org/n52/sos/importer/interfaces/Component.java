package org.n52.sos.importer.interfaces;

/**
 * represents a component (e.g. a day) of a combined element
 * (e.g. DateAndTime)
 * @author Raimund
 *
 */
public abstract class Component {

	/**
	 * returns a panel to complete the component
	 * in case it is missing
	 * @param c
	 * @return
	 */
	public abstract MissingComponentPanel getMissingComponentPanel(Combination c);
}
