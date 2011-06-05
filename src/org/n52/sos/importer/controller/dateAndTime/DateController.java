package org.n52.sos.importer.controller.dateAndTime;

import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.dateAndTime.DateModel;
import org.n52.sos.importer.view.dateAndTime.MissingDatePanel;
import org.n52.sos.importer.view.dateAndTime.MissingDayPanel;
import org.n52.sos.importer.view.dateAndTime.MissingMonthPanel;
import org.n52.sos.importer.view.dateAndTime.MissingYearPanel;

public class DateController extends DateAndTimeComponentController {

	private DateModel dateModel;
	
	public DateController() {
		dateModel = new DateModel();
	}
	
	@Override
	public void getMissingComponents(List<JPanel> missingComponents) {
		System.out.println("TEST");
		if (dateModel.getDay() == -1 && dateModel.getMonth() == -1 && dateModel.getYear() == -1)
			missingComponents.add(new MissingDatePanel());
		else {
			if (dateModel.getDay() == -1)
				missingComponents.add(new MissingDayPanel());
			if (dateModel.getMonth() == -1) 
				missingComponents.add(new MissingMonthPanel());
			if (dateModel.getYear() == -1)
				missingComponents.add(new MissingYearPanel());
		}
	}

}
