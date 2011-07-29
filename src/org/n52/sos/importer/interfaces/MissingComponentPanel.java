package org.n52.sos.importer.interfaces;

import javax.swing.JPanel;

public abstract class MissingComponentPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public abstract void assignValues();
	
	public abstract void unassignValues();
	
	public abstract boolean checkValues();
	
	public abstract Component getMissingComponent();
	
	public abstract void setMissingComponent(Component c);
}
