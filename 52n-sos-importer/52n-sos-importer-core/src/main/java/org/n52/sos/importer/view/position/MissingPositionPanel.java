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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.geotools.data.ows.Layer;
import org.geotools.data.wms.WebMapServer;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.map.WMSLayer;
import org.geotools.ows.ServiceException;
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
import org.n52.sos.importer.view.utils.n52Utils;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
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
	
	private MapContent mapContent;
	
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
		
		add(inputType,BorderLayout.NORTH);
		add(containerPanel,BorderLayout.CENTER);
		
		if (Constants.GUI_DEBUG) {
			inputType.setBorder(Constants.DEBUG_BORDER);
			manualInputPanel.setBorder(Constants.DEBUG_BORDER);
			mapPanel.setBorder(Constants.DEBUG_BORDER);
		}
		setVisible(true);
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
		mapPane.setEnabled(true);
		initMap(mapPane);
		
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

	private void initMap(JMapPane mapPane2) {
		try {
			mapContent = new MapContent();

			URL url = null;
			try {
				url = new URL(Constants.WMS_URL() + Constants.WMS_GET_CAPABILITIES_REQUEST);
			} catch (MalformedURLException e) {
				//will not happen
				logger.error(String.format("WMS URL not correct: \"%s\"",url),e);
			}
			/*
			 * @see http://docs.geotools.org/stable/userguide/extension/wms/wms.html#layer
			 */
			WebMapServer wms = null;
			wms = new WebMapServer(url);
			Layer rootLayer = wms.getCapabilities().getLayer();
			Layer specifiedBackgroundLayer = getBackgroundLayerByName(
					rootLayer.getChildren(),
					Constants.WMS_DEFAULT_BACKGROUND_LAYER_NAME); 
			
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Is layer null? %s; Name: %s",
						(specifiedBackgroundLayer==null),
 						(specifiedBackgroundLayer!=null?specifiedBackgroundLayer.getName():"")
						));
				logger.debug(String.format("Is WMS null? %s", (wms==null)));
			}

			WMSLayer displayLayer = new WMSLayer( wms, specifiedBackgroundLayer );
			mapContent.addLayer(displayLayer);

			// When first shown on screen it will display the layers.
			mapPane.setMapContent( mapContent );
			// 										values: minX, maxX, minY, maxY, crs
			ReferencedEnvelope bounds = Constants.WMS_ENVELOPE();
			mapPane.setDisplayArea(bounds);

		} catch (UnsupportedEncodingException e1) {
			logger.error(String.format("Exception thrown: %s",
					e1.getMessage()),
					e1);
		} catch (IllegalStateException e1) {
			logger.error(String.format("Exception thrown: %s",
					e1.getMessage()),
					e1);
		} catch (MalformedURLException e1) {
			logger.error(String.format("Exception thrown: %s",
					e1.getMessage()),
					e1);
		} catch (IOException e1) {
			logger.error(String.format("Exception thrown: %s",
					e1.getMessage()),
					e1);
		} catch (ServiceException e) {
			logger.error(String.format("Exception thrown: %s",
					e.getMessage()),
					e);
		} catch (MismatchedDimensionException e) {
			logger.error(String.format("Exception thrown: %s",
						e.getMessage()),
					e);
		} catch (NoSuchAuthorityCodeException e) {
			logger.error(String.format("Exception thrown: %s",
						e.getMessage()),
					e);
		} catch (FactoryException e) {
			logger.error(String.format("Exception thrown: %s",
						e.getMessage()),
					e);
		}
	}

	private Layer getBackgroundLayerByName(Layer[] children,
			String wmsDefaultBackgroundLayerName) {
		for (Layer layer : children) {
			if (layer.getName().equals(wmsDefaultBackgroundLayerName)) {
				return layer;
			}
		}
		return null;
	}

	private JPanel initInputType() {
		manualInput = new JRadioButton("Define position manually");
		manualInput.setSelected(false);
		manualInput.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				containerPanel.removeAll();
				containerPanel.add(manualInputPanel,BorderLayout.CENTER);
				revalidate();
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
				revalidate();
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
		if (mapInput.isSelected()) {
			Position p = s6cM.getPosition();
			p.setEPSGCode(new EPSGCode(Constants.DEFAULT_EPSG_CODE));
			p.setLatitude(new Latitude(n52Utils.parseDouble(latitudeTextField.getText()), 
					Constants.DEFAULT_UNIT_FOI_POSITION));
			p.setLongitude(new Longitude(n52Utils.parseDouble(longitudeTextField.getText()),
					Constants.DEFAULT_UNIT_FOI_POSITION));
			p.setHeight(new Height(Constants.DEFAULT_HEIGHT_FOI_POSITION, 
					Constants.DEFAULT_HEIGHT_UNIT_FOI_POSITION));
		} else if (manualInput.isSelected()) {
			java.awt.Component[] subPanels = manualInputPanel.getComponents();
			for (java.awt.Component component : subPanels) {
				if (component instanceof MissingComponentPanel) {
					MissingComponentPanel mcp = (MissingComponentPanel) component;
					mcp.assignValues();
				}
			}
		}
		if (mapContent != null) {
			mapContent.dispose();
		}
	}
	
	public void loadSettings() {
		// load settings from model and set map and manual interface to model position
		Position p = s6cM.getPosition();
		if (p.getEPSGCode() == null && p.getHeight() == null && p.getLatitude() == null && p.getLongitude() == null) {
			// on init -> set to default
			return;
		}
		if (p.getEPSGCode() != null && p.getEPSGCode().getValue() != Constants.DEFAULT_EPSG_CODE) {
			longitudeTextField.setText(p.getLongitude().getValue()+"");
			latitudeTextField.setText(p.getLatitude().getValue()+"");
			java.awt.Component[] subPanels = manualInputPanel.getComponents();
			for (java.awt.Component component : subPanels) {
				if (component instanceof MissingLatitudePanel) {
					MissingLatitudePanel mcp = (MissingLatitudePanel) component;
					mcp.setMissingComponent(p.getLatitude());
				} else if (component instanceof MissingLongitudePanel) {
					MissingLongitudePanel mcp = (MissingLongitudePanel) component;
					mcp.setMissingComponent(p.getLongitude());
				} else if (component instanceof MissingHeightPanel) {
					MissingHeightPanel mcp = (MissingHeightPanel) component;
					mcp.setMissingComponent(p.getHeight());
				} else if (component instanceof MissingEPSGCodePanel) {
					MissingEPSGCodePanel mcp = (MissingEPSGCodePanel) component;
					mcp.setMissingComponent(p.getEPSGCode());
				}
			} 
		} else {
			latitudeTextField.setText(p.getLatitude().getValue()+"");
			longitudeTextField.setText(p.getLongitude().getValue()+"");
			epsgTextField.setText(p.getEPSGCode().getValue()+"");
		}
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
		longitudeTextField.setText(String.format("%.4f",pos.x));
		latitudeTextField.setText(String.format("%.4f",pos.y));
	}
	
}
