package org.n52.sos.importer.view.dateAndTime;

import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;

public abstract class MissingDateAndTimePanel extends MissingComponentPanel {

	private static final long serialVersionUID = 1L;

	protected DateAndTime dateAndTime;
	
	public MissingDateAndTimePanel(DateAndTime dateAndTime) {
		super();
		this.dateAndTime = dateAndTime;
	}	
}
