package org.n52.sos.importer.view;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class Step8Panel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JProgressBar sensorProgressBar = new JProgressBar();
	
	private JLabel registerSensorLabel = new JLabel("Register 0 Sensors...");
	
	private JLabel successfulSensorsLabel = new JLabel("Successful: 0");
	
	private JLabel erroneousSensorsLabel = new JLabel("Errors: 0");
	
	private JLabel insertObservationLabel = new JLabel("Insert 0 Observations...");
	
	private JProgressBar observationProgressBar = new JProgressBar();
	
	private JLabel successfulObservationsLabel = new JLabel("Successful: 0");
	
	private JLabel erroneousObservationsLabel = new JLabel("Errors: 0");
	
	
	public Step8Panel() {
		sensorProgressBar.setStringPainted(true);
		observationProgressBar.setStringPainted(true);
		
		this.setLayout(new GridLayout(2, 1));
		JPanel sensorPanel = new JPanel(new GridLayout(5, 1));
		sensorPanel.add(registerSensorLabel);
		sensorPanel.add(sensorProgressBar);
		sensorPanel.add(successfulSensorsLabel);
		sensorPanel.add(erroneousSensorsLabel);
		sensorPanel.add(new JLabel(""));
		this.add(sensorPanel);

		JPanel observationPanel = new JPanel(new GridLayout(5, 1));
		observationPanel.add(insertObservationLabel);
		observationPanel.add(observationProgressBar);
		observationPanel.add(successfulObservationsLabel);
		observationPanel.add(erroneousObservationsLabel);
		observationPanel.add(new JLabel(""));
		this.add(observationPanel);
	}
	
	public void setIndeterminate(boolean aFlag) {
		sensorProgressBar.setIndeterminate(aFlag);
	}
	
	public void setTotalNumberOfSensors(int n) {
		registerSensorLabel.setText("Register " + n + " sensors...");
	}
	
	public void setRegisterSensorProgress(int n) {
		sensorProgressBar.setValue(n);
	}
	
	public void setNumberOfSuccessfulSensors(int n) {
		successfulSensorsLabel.setText("Successful: " + n);
	}
	
	public void setNumberOfErroneousSensors(int n) {
		erroneousSensorsLabel.setText("Errors: " + n);
	}
	
	public void setInsertObservationProgress(int n) {
		observationProgressBar.setValue(n);
	}
	
	public void setNumberOfSuccessfulObservations(int n) {
		successfulObservationsLabel.setText("Successful: " + n );
	}
	
	public void setNumberOfErroneousObservations(int n) {
		erroneousObservationsLabel.setText("Errors: " + n);
	}
	
	public void setTotalNumberOfObservations(int n) {
		insertObservationLabel.setText("Insert " + n + " observations...");
	}
}
