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
package org.n52.sos.importer.test;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.Step6cModel;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.view.Step6cPanel;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class Step6cWMSPanelTest extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(Step6cWMSPanelTest.class);
	
	public static void main(String[] args) {
		FeatureOfInterest foi = new FeatureOfInterest();
		foi.setName("testFOIname");
		foi.setPosition(new Position());
		Step6cModel s6cM = new Step6cModel(foi);
		
		JFrame frame = new Step6cWMSPanelTest(s6cM);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public Step6cWMSPanelTest(Step6cModel s6cM) {
		getContentPane().add(new Step6cPanel("test description", "testFOIname", s6cM));
	}

}
