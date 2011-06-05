package org.n52.sos.importer.controller.dateAndTime;

import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.dateAndTime.TimeModel;
import org.n52.sos.importer.view.dateAndTime.MissingHoursPanel;
import org.n52.sos.importer.view.dateAndTime.MissingMinutesPanel;
import org.n52.sos.importer.view.dateAndTime.MissingSecondsPanel;
import org.n52.sos.importer.view.dateAndTime.MissingTimePanel;

public class TimeController extends DateAndTimeComponentController {

	private TimeModel timeModel;

	public TimeController() {
		timeModel = new TimeModel();
	}
	
	public TimeModel getTimeModel() {
		return timeModel;
	}

	public void setTimeModel(TimeModel timeModel) {
		this.timeModel = timeModel;
	}
	
	@Override
	public void getMissingComponents(List<JPanel> missingComponents) {
		if (timeModel.getSeconds() == -1 && timeModel.getMinutes() == -1 && timeModel.getHours() == -1)
			missingComponents.add(new MissingTimePanel());
		else {
			if (timeModel.getSeconds() == -1)
				missingComponents.add(new MissingSecondsPanel());
			if (timeModel.getMinutes() == -1) 
				missingComponents.add(new MissingMinutesPanel());
			if (timeModel.getHours() == -1)
				missingComponents.add(new MissingHoursPanel());
		}
	}

}
