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
package org.n52.sos.importer.view;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.log4j.Logger;
import org.n52.sos.importer.controller.Step1Controller;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.utils.Constants;
import org.n52.sos.importer.view.utils.ToolTips;

/**
 * chooses a CSV file
 * @author Raimund
 *
 */
public class Step1Panel extends JPanel {
	
	static final long serialVersionUID = 1L;
	private final Step1Controller step1Controller;
	
	private static final Logger logger = Logger.getLogger(Step1Panel.class);
	
	private final JLabel csvFileLabel = new JLabel(Lang.l().step1File() + " :");
	private final JTextField csvFileTextField = new JTextField(25);
	private final JButton browse = new JButton(Lang.l().step1BrowseButton());
	private final Step1Panel _this = this;
	
	private static final String welcomeResBunName = "org.n52.sos.importer.html.welcome"; //$NON-NLS-1$

	private static final ResourceBundle welcomeRes = ResourceBundle.getBundle(welcomeResBunName);
	
	public Step1Panel(Step1Controller step1Controller) {
		super();
		// init fields
		JScrollPane welcomePanel = this.welcomePanel();
		JPanel languagePanel = this.languagePanel();
		// csv Panel
		JPanel csvPanel = new JPanel();
		csvPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		csvPanel.add(csvFileLabel);
		csvPanel.add(csvFileTextField);
		csvPanel.add(browse);
		// create gui
		this.step1Controller = step1Controller;
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.add(welcomePanel);
		this.add(languagePanel);
		this.add(csvPanel);
		csvFileTextField.setToolTipText(ToolTips.get(ToolTips.CSV_File));
		browse.addActionListener(new BrowseButtonClicked());
	}
	
	public void setCSVFilePath(String filePath) {
		csvFileTextField.setText(filePath);
	}
	
	public String getCSVFilePath() {
		return csvFileTextField.getText();
	}
	
	private class BrowseButtonClicked implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			step1Controller.browseButtonClicked();
		}
	}
	
	private JPanel languagePanel() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(Lang.l().step1SelectLanguage());
		JComboBox jcb = new JComboBox(Lang.getAvailableLocales());
		//
		jcb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
		        Locale selectedLocale = (Locale)cb.getSelectedItem();
		        Lang.setCurrentLocale(selectedLocale);
			}
		});
		jcb.setSelectedItem(Lang.getCurrentLocale());
		jcb.setEditable(false);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel.add(label);
		panel.add(jcb);
		return panel;
	}

	/**
	 * This method reads the welcome message from a HTML file and presents it 
	 * using a <code>JEditorPane</code>
	 * {@link javax.swing.JEditorPane }
	 */
	private JScrollPane welcomePanel(){
		JEditorPane pane  = new JEditorPane();
		JScrollPane scrollPane = new JScrollPane(pane);
		String t = welcomeRes.getString(Constants.language());
		//
		pane.setEditable(false);
		pane.setContentType(Constants.WELCOME_RES_CONTENT_TYPE);
		pane.setText(t);
		float[] f = Color.RGBtoHSB(238, 238, 238, null);
		pane.setBackground(Color.getHSBColor(f[0], f[1], f[2]));
		pane.setDragEnabled(true);
		//
		// Add simple hyperlink functionality -> call system Browser with URL
		pane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				      // try to start system browser with link
					if(Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().browse(e.getURL().toURI());
						} catch (IOException e1) {
							String error = "Could not start system browser with URL: \"" + e.getURL() + "\"";
							logger.error(error, e1);
							JOptionPane.showMessageDialog(_this, error, "Error Opening Browser", JOptionPane.ERROR_MESSAGE);
						} catch (URISyntaxException e1) {
							String error = "Syntax error in URL: \"" + e.getURL() + "\"";
							logger.error(error, e1);
							JOptionPane.showMessageDialog(_this, error, "Error Opening Browser", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});
		//
		//
		pane.setCaretPosition(0);
		//Put the editor pane in a scroll pane.
		scrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(MainFrame.DIALOG_WIDTH-20, 400));
		scrollPane.setAutoscrolls(true);
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPane.setWheelScrollingEnabled(true);
		if(Constants.GUI_DEBUG) {
			scrollPane.setBorder(Constants.DEBUG_BORDER);
		}
		return scrollPane;
	}
		
}
