/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.importer.test;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;

import javax.swing.JFrame;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step6cModel;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.view.Step6cPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Step6cWMSPanelTest class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 * @since 0.5.0
 */
public class Step6cWMSPanelTest extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(Step6cWMSPanelTest.class);

	/**
	 * <p>main.</p>
	 *
	 * @param args an array of {@link java.lang.String} objects.
	 */
	public static void main(final String[] args) {
		Constants.GUI_DEBUG = true;
		final FeatureOfInterest foi = new FeatureOfInterest();
		foi.setName("testFOIname");
		foi.setPosition(new Position());
		final Step6cModel s6cM = new Step6cModel(foi);

		final JFrame frame = new Step6cWMSPanelTest(s6cM);
		frame.setPreferredSize(new Dimension(Constants.DIALOG_WIDTH, Constants.DIALOG_HEIGHT));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		if (Constants.GUI_DEBUG) {
			final long eventMask = AWTEvent.COMPONENT_EVENT_MASK + AWTEvent.ADJUSTMENT_EVENT_MASK;
			Toolkit.getDefaultToolkit().addAWTEventListener( new AWTEventListener() {
			    @Override
				public void eventDispatched(final AWTEvent e)
			    {
			        if (logger.isDebugEnabled() && e.getSource().getClass().getName().indexOf("org.geotools.swing") != -1) {
						logger.debug("AWTEvent: {}",e);
					}
			    }
			}, eventMask);
		}
	}

	/**
	 * <p>Constructor for Step6cWMSPanelTest.</p>
	 *
	 * @param s6cM a {@link org.n52.sos.importer.model.Step6cModel} object.
	 */
	public Step6cWMSPanelTest(final Step6cModel s6cM) {
		getContentPane().add(new Step6cPanel("test description", "testFOIname", s6cM));
	}

}
