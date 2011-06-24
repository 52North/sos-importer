package org.n52.sos.importer.view.dateAndTime;

import javax.swing.JPanel;

import org.n52.sos.importer.model.dateAndTime.DateAndTime;

public abstract class MissingDateAndTimePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	protected DateAndTime dateAndTime;
	
	public MissingDateAndTimePanel(DateAndTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
	
	public abstract void assignValues();
	
	public abstract void unassignValues();

}
