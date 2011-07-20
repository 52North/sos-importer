package org.n52.sos.importer.view.step3;

import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

import org.n52.sos.importer.Combination;
import org.n52.sos.importer.config.EditableComboBoxItems;
import org.n52.sos.importer.config.Settings;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;

public class DateAndTimeCombinationPanel extends CombinationPanel {
	//source: 	http://download.oracle.com/javase/tutorial/uiswing/
	// 			examples/components/ComboBoxDemo2Project/src/components/
	// 			ComboBoxDemo2.java
	private static final long serialVersionUID = 1L;
	
	private DateAndTime dateAndTime;
	
	public DateAndTimeCombinationPanel(JPanel containerPanel) {	
		super(containerPanel);
	}

	@Override
	public Combination getCombination() {
		if (dateAndTime == null) dateAndTime = new DateAndTime();
		return dateAndTime;
	}

	@Override
	public String[] getGroupItems() {
		return Settings.getInstance().getDateAndTimeGroups();
	}

	@Override
	public DefaultComboBoxModel getPatterns() {
		return EditableComboBoxItems.getInstance().getDateAndTimePatterns();
	}

	@Override
	public Object getTestValue() {
		return new Date();
	}
}
