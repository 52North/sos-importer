package org.n52.sos.importer.view;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.apache.log4j.Logger;

/**
 * shows progress while assembling data, registering sensors
 * and inserting observations, provides a link to the log file
 * @author Raimund
 *
 */
public class Step8Panel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(Step8Panel.class);
	
	private JProgressBar sensorProgressBar = new JProgressBar();
	
	private JLabel registerSensorLabel = new JLabel("Register 0 Sensors...");
	
	private JLabel successfulSensorsLabel = new JLabel("Successful: 0");
	
	private JLabel erroneousSensorsLabel = new JLabel("Errors: 0");
	
	private JLabel insertObservationLabel = new JLabel("Insert 0 Observations...");
	
	private JProgressBar observationProgressBar = new JProgressBar();
	
	private JLabel successfulObservationsLabel = new JLabel("Successful: 0");
	
	private JLabel erroneousObservationsLabel = new JLabel("Errors: 0");
	
	private JLabel logFileLabel = new JLabel();
	
	public Step8Panel() {
		sensorProgressBar.setStringPainted(true);
		observationProgressBar.setStringPainted(true);
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel progressPanel = new JPanel(new GridLayout(2, 1));
		JPanel sensorPanel = new JPanel(new GridLayout(5, 1));
		sensorPanel.add(registerSensorLabel);
		sensorPanel.add(sensorProgressBar);
		sensorPanel.add(successfulSensorsLabel);
		sensorPanel.add(erroneousSensorsLabel);
		sensorPanel.add(new JLabel(""));
		progressPanel.add(sensorPanel);

		JPanel observationPanel = new JPanel(new GridLayout(5, 1));
		observationPanel.add(insertObservationLabel);
		observationPanel.add(observationProgressBar);
		observationPanel.add(successfulObservationsLabel);
		observationPanel.add(erroneousObservationsLabel);
		observationPanel.add(new JLabel(""));
		progressPanel.add(observationPanel);
		
		progressPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(progressPanel);
		logFileLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(logFileLabel);
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
	
	public void setLogFileURI(URI uri) {
		logger.info("Log file is saved at: " + uri);
		logFileLabel.setText("<html><a href=>Check log file</a></html>");
		logFileLabel.addMouseListener(new LogFileLinkClicked(uri));
	}
	
	private class LogFileLinkClicked implements MouseListener {

		private URI logFileURI;
		
		public LogFileLinkClicked(URI logFileURI) {
			this.logFileURI = logFileURI;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {			
		}

		@Override
		public void mouseEntered(MouseEvent e) {			
		}

		@Override
		public void mouseExited(MouseEvent e) {		
		}

		@Override
		public void mousePressed(MouseEvent e) {
			Desktop desktop = Desktop.getDesktop();
			try {
	            desktop.browse(logFileURI);
			} catch (IOException ioe) {
				logger.error("Unable to open log file: " + ioe.getMessage());
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {	
		}		
	}
}
