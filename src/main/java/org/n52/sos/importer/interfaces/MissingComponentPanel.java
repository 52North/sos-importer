package org.n52.sos.importer.interfaces;

import javax.swing.JPanel;

/**
 * represents the view of a component (e.g. a day) which
 * is not available in the CSV file; therefore, it has to
 * be chosen manually
 * @author Raimund
 *
 */
public abstract class MissingComponentPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * allocate values of the missing component
	 */
	public abstract void assignValues();
	
	/**
	 * release values of the missing component
	 */
	public abstract void unassignValues();
	
	/**
	 * checks if all values are in the defined range 
	 * of this component; returns false, if not
	 */
	public abstract boolean checkValues();
	
	/**
	 * returns the missing component
	 */
	public abstract Component getMissingComponent();
	
	/**
	 * initializes the missing component
	 */
	public abstract void setMissingComponent(Component c);
}
