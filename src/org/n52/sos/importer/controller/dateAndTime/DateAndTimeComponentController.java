package org.n52.sos.importer.controller.dateAndTime;

import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.bean.TableConnection;

public abstract class DateAndTimeComponentController extends TableConnection {
	
	public abstract void getMissingComponents(List<JPanel> missingComponents);
}
