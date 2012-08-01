/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.sos.importer.view.position;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.apache.log4j.Logger;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.swing.JMapPane;
import org.geotools.swing.action.PanAction;
import org.geotools.swing.action.ResetAction;
import org.geotools.swing.action.ZoomInAction;
import org.geotools.swing.action.ZoomOutAction;
import org.geotools.swing.control.JMapStatusBar;
import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step6cModel;
import org.n52.sos.importer.model.position.EPSGCode;
import org.n52.sos.importer.model.position.Height;
import org.n52.sos.importer.model.position.Latitude;
import org.n52.sos.importer.model.position.Longitude;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class MissingPositionPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(MissingPositionPanel.class);
	
	private Step6cModel s6cM;
	
	// GUI stuff
	private JRadioButton manualInput;
	private JRadioButton mapInput;
	private JPanel containerPanel;
	private JTextField latitudeTextField;
	private JTextField longitudeTextField;
	private JTextField epsgTextField;
	private JMapPane mapPane;
	private JPanel mapPanel;
	private JPanel manualInputPanel;
	
	public MissingPositionPanel(Step6cModel s6cM) {
		setLayout(new BorderLayout(0, 0));
		
		this.s6cM = s6cM;
		
		JPanel inputType = initInputType();
		manualInputPanel = initManualInputPanel(s6cM);
		mapPanel = initMapPanel();
		
		containerPanel = new JPanel();
		containerPanel.setLayout(new BorderLayout(0,0));
		containerPanel.add(mapPanel,BorderLayout.CENTER);
		containerPanel.add(manualInputPanel,BorderLayout.CENTER);
		manualInputPanel.setVisible(false);
		mapPanel.setVisible(true);
		
		add(inputType,BorderLayout.NORTH);
		add(containerPanel,BorderLayout.CENTER);
		
		if (Constants.GUI_DEBUG) {
			inputType.setBorder(Constants.DEBUG_BORDER);
			manualInputPanel.setBorder(Constants.DEBUG_BORDER);
			mapPanel.setBorder(Constants.DEBUG_BORDER);
		}
	}

	private JPanel initManualInputPanel(Step6cModel s6cM) {
		JPanel manualInputPanel = new JPanel();
		manualInputPanel.setLayout(new GridLayout(4, 1));
		manualInputPanel.add(new MissingLatitudePanel(s6cM.getPosition()));
		manualInputPanel.add(new MissingLongitudePanel(s6cM.getPosition()));
		manualInputPanel.add(new MissingHeightPanel(s6cM.getPosition()));
		manualInputPanel.add(new MissingEPSGCodePanel(s6cM.getPosition()));
		return manualInputPanel;
	}

	private JPanel initMapPanel() {
		mapPane = new JMapPane();
		WMS.set(mapPane);
		
		JPanel mapPanel = new JPanel();
		mapPanel.setLayout(new BorderLayout(0, 0));
		mapPanel.setBorder(new LineBorder(Color.BLACK, 1, true));
		
		JPanel mapControlPanel = new JPanel();
		mapPanel.add(mapControlPanel, BorderLayout.NORTH);
		mapControlPanel.setLayout(new GridLayout(0, 2, 10, 0));
		
		JPanel buttonControlPanel = new JPanel();
		mapControlPanel.add(buttonControlPanel);
		
		JButton pan = new JButton(new PanAction(mapPane,true));
		pan.setText("Pan");
		
		JButton zoomIn = new JButton(new ZoomInAction(mapPane,true));
		zoomIn.setText("Zoom in");
		
		JButton zoomOut = new JButton(new ZoomOutAction(mapPane,true));
		zoomOut.setText("Zoom out");
		
		JButton resetMap = new JButton(new ResetAction(mapPane,true));
		resetMap.setText("Reset");
		
		JButton select = new JButton();
		select.setText("Select");
        ImageIcon buttonIcon = new ImageIcon(getClass().getResource(Constants.WMS_VIEW_SELECT_TOOL_ICON_PNG_PATH));
		select.setIcon(buttonIcon);
		select.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPane.setCursorTool(new SelectPositionTool(MissingPositionPanel.this));
			}
		});
		buttonControlPanel.setLayout(new GridLayout(2, 3));
		
		buttonControlPanel.add(zoomOut);
		buttonControlPanel.add(zoomIn);
		buttonControlPanel.add(pan);
		buttonControlPanel.add(resetMap);
		buttonControlPanel.add(select);
		
		JPanel positionPanel = new JPanel();
		mapControlPanel.add(positionPanel);
		positionPanel.setLayout(new GridLayout(3, 2, 0, 0));
		
		JLabel latitudeLabel = new JLabel("Latitude:");
		positionPanel.add(latitudeLabel);
		
		latitudeTextField = new JTextField();
		latitudeLabel.setLabelFor(latitudeTextField);
		latitudeTextField.setText("");
		latitudeTextField.setFocusable(true);
		latitudeTextField.setEditable(false);
		positionPanel.add(latitudeTextField);
		latitudeTextField.setColumns(10);
		
		JLabel longitudeLabel = new JLabel("Longitude:");
		positionPanel.add(longitudeLabel);
		
		longitudeTextField = new JTextField();
		longitudeLabel.setLabelFor(longitudeTextField);
		longitudeTextField.setText("");
		longitudeTextField.setEditable(false);
		longitudeTextField.setFocusable(true);
		positionPanel.add(longitudeTextField);
		longitudeTextField.setColumns(10);
		
		JLabel epsgLabel = new JLabel("EPSG:");
		positionPanel.add(epsgLabel);
		
		epsgTextField = new JTextField();
		epsgLabel.setLabelFor(epsgTextField);
		epsgTextField.setText(Constants.DEFAULT_EPSG_CODE+"");
		epsgTextField.setEditable(false);
		epsgTextField.setFocusable(true);
		positionPanel.add(epsgTextField);
		epsgTextField.setColumns(10);
		
		mapPanel.add(mapPane,BorderLayout.CENTER);
		mapPanel.add(JMapStatusBar.createDefaultStatusBar(mapPane),BorderLayout.SOUTH);
		
		if (Constants.GUI_DEBUG) {	
			buttonControlPanel.setBorder(Constants.DEBUG_BORDER);
			mapControlPanel.setBorder(Constants.DEBUG_BORDER);
		}
		
		return mapPanel;
	}

	private JPanel initInputType() {
		manualInput = new JRadioButton("Define position manually");
		manualInput.setSelected(false);
		manualInput.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				containerPanel.removeAll();
				containerPanel.add(manualInputPanel,BorderLayout.CENTER);
				manualInputPanel.setVisible(true);
				repaint();
			}
		});
		
		mapInput = new JRadioButton("Select position on map");
		mapInput.setSelected(true);
		mapInput.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				containerPanel.removeAll();
				containerPanel.add(mapPanel,BorderLayout.CENTER);
				mapPanel.setVisible(true);
				repaint();
			}
		});
		
		ButtonGroup bGroup = new ButtonGroup();
		bGroup.add(manualInput);
		bGroup.add(mapInput);
		
		JPanel inputType = new JPanel();
		inputType.add(manualInput);
		inputType.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
		inputType.add(mapInput);
		return inputType;
	}

	public boolean isFinished() {
		if (this.manualInput.isSelected()) {
			java.awt.Component[] subPanels = manualInputPanel.getComponents();
			for (java.awt.Component component : subPanels) {
				if (component instanceof MissingComponentPanel) {
					MissingComponentPanel mcp = (MissingComponentPanel) component;
					if (!mcp.checkValues()) {
						return false;
					}
				}
			}
			return true;
		} else if (this.mapInput.isSelected()) {
			if (longitudeTextField.getText() != null && !longitudeTextField.getText().equals("")
					&& latitudeTextField.getText() != null && !latitudeTextField.getText().equals("")
					&& epsgTextField.getText() != null && !epsgTextField.getText().equals("")) {
				return true;
			}
		}
		return false;
	}

	public void saveSettings() {
		if (this.mapInput.isSelected()) {
			Position p = s6cM.getPosition();
			p.setEPSGCode(new EPSGCode(Constants.DEFAULT_EPSG_CODE));
			p.setLatitude(new Latitude(Double.parseDouble(latitudeTextField.getText()), 
					Constants.DEFAULT_UNIT_FOI_POSITION));
			p.setLongitude(new Longitude(Double.parseDouble(longitudeTextField.getText()),
					Constants.DEFAULT_UNIT_FOI_POSITION));
			p.setHeight(new Height(Constants.DEFAULT_HEIGHT_FOI_POSITION, 
					Constants.DEFAULT_HEIGHT_UNIT_FOI_POSITION));
		} else if (this.manualInput.isSelected()) {
			java.awt.Component[] subPanels = manualInputPanel.getComponents();
			for (java.awt.Component component : subPanels) {
				if (component instanceof MissingComponentPanel) {
					MissingComponentPanel mcp = (MissingComponentPanel) component;
					mcp.assignValues();
				}
			}
		}
	}
	
	public void loadSettings() {
		// XXX implement
		// load settings from model and set map and manual interface to model position
		Position p = s6cM.getPosition();
		if (p.getEPSGCode() == null && p.getHeight() == null && p.getLatitude() == null && p.getLongitude() == null) {
			// XXX on init -> set to default
			return;
		}
		// XXX transform from EPSG:? to EPSG 4326
		if (p.getEPSGCode() != null && p.getEPSGCode().getValue() != Constants.DEFAULT_EPSG_CODE) {
//			p = transform(p,Constants.DEFAULT_ALTITUDE_UNIT);
		}
		latitudeTextField.setText(p.getLatitude().getValue()+"");
		longitudeTextField.setText(p.getLongitude().getValue()+"");
		epsgTextField.setText(p.getEPSGCode().getValue()+"");
//		wmsCanvas.centerOnPoint(p);
		// implement WMSCanvas.setToPosition(s6cM.getPosition());
	}

	public void setSelectedPosition(DirectPosition2D pos) {
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("setSelectedPosition(%s)",
					pos));
		}
		CoordinateReferenceSystem crs = pos.getCoordinateReferenceSystem();
		Set<ReferenceIdentifier> ids = crs.getIdentifiers();
		ReferenceIdentifier[] idsA = ids.toArray(new ReferenceIdentifier[ids.size()]);
		// update current panel
		epsgTextField.setText(idsA[0].getCode());
		longitudeTextField.setText(pos.x+"");
		latitudeTextField.setText(pos.y+"");
		// update model
		Position p = s6cM.getPosition();
		p.setEPSGCode(new EPSGCode(Integer.parseInt(idsA[0].getCode())));
		p.setLongitude(new Longitude(pos.x, Constants.DEFAULT_LONGITUDE_UNIT));
		p.setLatitude(new Latitude(pos.y, Constants.DEFAULT_LATITUDE_UNIT));
	}
	
}
