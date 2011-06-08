package org.n52.sos.importer.view.dateAndTime;

import javax.swing.JPanel;

import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;

public abstract class MissingComponentPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	protected DateAndTimeController dateAndTimeController;
	
	public MissingComponentPanel(DateAndTimeController dateAndTimeController) {
		this.dateAndTimeController = dateAndTimeController;
	}
	
	public abstract void assignValues();
	
	public abstract void unassignValues();

}
