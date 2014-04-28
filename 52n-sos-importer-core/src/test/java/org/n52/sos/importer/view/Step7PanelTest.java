/**
 * Copyright (C) 2014
 * by 52 North Initiative for Geospatial Open Source Software GmbH
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
package org.n52.sos.importer.view;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class Step7PanelTest {

	private static final String POX = "POX";
	private static final String V_20 = "2.0.0";
	private static final String V_10 = "1.0.0";
	private Step7Panel panel;

	@Before
	public void init() {
		panel = new Step7Panel();
	}

	@Test
	public void shouldReturnSosVersion100AsDefault() {
		assertThat(panel.getSosVersion(), is(V_10));
	}

	@Test
	public void shouldReturnSosVersion() {
		panel.setSosVersion(V_20);
		assertThat(panel.getSosVersion(), is(V_20));
	}

	@Test
	public void shouldReturnBindingPOXAsDefault() {
		assertThat(panel.getSosBinding(), is(POX));
	}

	@Test
	public void shouldReturnBinding() {
		panel.setBinding(POX);
		assertThat(panel.getSosBinding(), is(POX));
	}
}
