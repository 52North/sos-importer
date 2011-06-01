package org.n52.sos.importer;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.n52.sos.importer.view.MainFrame;

public class Step5cPanel extends StepPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel featureOfInterestLabel = new JLabel("Please choose the position " +
			"for the following feature of interest:");
	private final JTextField featureOfInterestTextField = new JTextField(10);
	
	private final JLabel referenceSystemNameLabel = new JLabel("Reference system: ");	
	private final String[] referenceSystemNames = {"WGS84"};
	private final JComboBox referenceSystemNameComboBox = new JComboBox(referenceSystemNames);
	private final JLabel EPSGCodeLabel = new JLabel("EPSG-Code: ");
	private final String[] EPSGCodes = {"5326"};
	private final JComboBox EPSGCodeComboBox = new JComboBox(EPSGCodes);
	
	private final JLabel latitudeLabel = new JLabel("   Latitude: ");
	private final JTextField latitudeTextField = new JTextField(8);
	private final JLabel longitudeLabel = new JLabel("   Longitude: ");
	private final JTextField longitudeTextField = new JTextField(8);
	private final JLabel heightLabel = new JLabel("   Height: ");
	private final JTextField heightTextField = new JTextField(8);
	
	private final JLabel latitudeUnitLabel = new JLabel("   Unit: ");
	private final String[] latLonUnits = {"� ' ''"};
	private final JComboBox latitudeUnitComboBox = new JComboBox(latLonUnits);
	private final JLabel longitudeUnitLabel = new JLabel("   Unit: ");
	private final JComboBox longitudeUnitComboBox = new JComboBox(latLonUnits);
	private final JLabel heightUnitLabel = new JLabel("   Unit: ");
	private final String[] heightUnits = {"meters", "feet"};
	private final JComboBox heightUnitComboBox = new JComboBox(heightUnits);
	
	public Step5cPanel(MainFrame mainFrame) {
		super(mainFrame);
		featureOfInterestTextField.setEditable(false);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JPanel featureOfInterestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		featureOfInterestPanel.add(featureOfInterestLabel);
		featureOfInterestPanel.add(featureOfInterestTextField);
		this.add(featureOfInterestPanel);
		
		JPanel positionWrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel positionPanel = new JPanel(new GridLayout(3,4));
		JPanel latitudePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		latitudePanel.add(latitudeLabel);
		positionPanel.add(latitudePanel);
		positionPanel.add(latitudeTextField);
		JPanel latitudeUnitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		latitudeUnitPanel.add(latitudeUnitLabel);
		positionPanel.add(latitudeUnitPanel);
		positionPanel.add(latitudeUnitComboBox);
		JPanel longitudePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		longitudePanel.add(longitudeLabel);
		positionPanel.add(longitudePanel);
		positionPanel.add(longitudeTextField);
		JPanel longitudeUnitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		longitudeUnitPanel.add(longitudeUnitLabel);
		positionPanel.add(longitudeUnitPanel);
		positionPanel.add(longitudeUnitComboBox);
		JPanel heightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		heightPanel.add(heightLabel);
		positionPanel.add(heightPanel);
		positionPanel.add(heightTextField);
		JPanel heightUnitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		heightUnitPanel.add(heightUnitLabel);
		positionPanel.add(heightUnitPanel);
		positionPanel.add(heightUnitComboBox);
		positionWrapperPanel.add(positionPanel);
		this.add(positionWrapperPanel);
		
		JPanel referenceSystemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		referenceSystemPanel.add(referenceSystemNameLabel);
		referenceSystemPanel.add(referenceSystemNameComboBox);
		referenceSystemPanel.add(EPSGCodeLabel);
		referenceSystemPanel.add(EPSGCodeComboBox);
		this.add(referenceSystemPanel);
	}
	
	@Override
	protected void loadSettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getDescription() {
		return "Step 5: Add missing metadata";
	}

	@Override
	protected void back() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void next() {
		// TODO Auto-generated method stub
		
	}

}
