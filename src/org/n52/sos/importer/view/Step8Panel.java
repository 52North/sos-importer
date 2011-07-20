package org.n52.sos.importer.view;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class Step8Panel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JProgressBar sensorProgressBar = new JProgressBar();
	
	private JLabel registerSensorLabel = new JLabel("Register Sensors...");
	
	private int sensorNumber;
	
	private JLabel sensorSuccessLabel = new JLabel();
	
	private JLabel insertObservationLabel = new JLabel("Insert Observations...");
	
	private JProgressBar observationProgressBar = new JProgressBar();
	
	private int observationNumber;
	
	private JLabel observationSuccessLabel = new JLabel();
	
	public Step8Panel() {
		sensorProgressBar.setStringPainted(true);
		observationProgressBar.setStringPainted(true);
		
		this.setLayout(new GridLayout(2, 1));
		JPanel sensorPanel = new JPanel(new GridLayout(4, 1));
		sensorPanel.add(registerSensorLabel);
		sensorPanel.add(sensorProgressBar);
		sensorPanel.add(sensorSuccessLabel);
		sensorPanel.add(new JLabel(""));
		this.add(sensorPanel);

		JPanel observationPanel = new JPanel(new GridLayout(4, 1));
		observationPanel.add(insertObservationLabel);
		observationPanel.add(observationProgressBar);
		observationPanel.add(observationSuccessLabel);
		observationPanel.add(new JLabel(""));
		this.add(observationPanel);
	}
	
	public void setTotalNumberOfSensors(int n) {
		this.sensorNumber = n;
		sensorSuccessLabel.setText("Successful: 0/" + sensorNumber);
	}
	
	public void setRegisterSensorProgress(int n) {
		sensorProgressBar.setValue(n);
	}
	
	public void setSensorSuccess(int n) {
		sensorSuccessLabel.setText("Successful: " + n + "/" + sensorNumber);
	}
	
	public void setTotalNumberOfObservations(int n) {
		this.observationNumber = n;
		observationSuccessLabel.setText("Successful: 0/" + observationNumber);
	}
	
	public void setInsertObservationProgress(int n) {
		observationProgressBar.setValue(n);
	}
	
	public void setObservationSuccess(int n) {
		observationSuccessLabel.setText("Successful: " + n + "/" + observationNumber);
	}
}
