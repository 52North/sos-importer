package org.n52.sos.importer.view.dateAndTime;

import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

public class MissingDatePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel dateLabel = new JLabel("Date: ");
	
	private SpinnerDateModel dateModel;
	private JSpinner dateSpinner;
	
	public MissingDatePanel() {
		GregorianCalendar calendar = new GregorianCalendar();
		Date initDate = calendar.getTime();
		calendar.add(Calendar.YEAR, -100);
		Date earliestDate = calendar.getTime();
		calendar.add(Calendar.YEAR, 200);
		Date latestDate = calendar.getTime();
		dateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.YEAR);
		dateSpinner = new JSpinner(dateModel);
		dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(dateLabel);
		this.add(dateSpinner);
	}
}
