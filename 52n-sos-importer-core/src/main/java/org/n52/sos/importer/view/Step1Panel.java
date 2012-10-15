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
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.log4j.Logger;
import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.BackNextController;
import org.n52.sos.importer.controller.Step1Controller;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.utils.ToolTips;

/**
 * chooses a CSV file
 * @author Raimund
 *
 */
public class Step1Panel extends JPanel {
	
	static final long serialVersionUID = 1L;
	private final Step1Controller step1Controller;
	
	private final String[] feedingTypes = new String[]{"One-Time-Feed from a local CSV file", "One-Time-Feed / Repetitive Feed from a FTP-Server"};
	
	// separation of type cases
	public static final int CSV_FILE = 0;
	public static final int FTP_FILE = 1;
	
	private static final Logger logger = Logger.getLogger(Step1Panel.class);
	private final JTextField csvFileTextField = new JTextField(25);
	private final JTextField jtfUrl = new JTextField();
	private final JTextField jtfUser = new JTextField();
	private final JTextField jtfDirectory = new JTextField();
	private final JTextField jtfFilenameSchema = new JTextField();
	private final JPasswordField jpfPassword = new JPasswordField();
	private final JCheckBox jcbRegex = new JCheckBox();
	private final JComboBox jcbChooseInputType = new JComboBox(feedingTypes);
	private final Step1Panel _this = this;
	private final JPanel cardPanel = new JPanel(new CardLayout());
	
	private static final String welcomeResBunName = "org.n52.sos.importer.html.welcome"; //$NON-NLS-1$

	private static final ResourceBundle welcomeRes = ResourceBundle.getBundle(welcomeResBunName);
	
	public Step1Panel(Step1Controller step1Controller) {
		super();
		JScrollPane welcomePanel = initWelcomePanel();
		JPanel languagePanel = initLanguagePanel();
		JPanel csvPanel = initCsvPanel();
		this.step1Controller = step1Controller;
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(languagePanel);
		add(csvPanel);
		add(welcomePanel);
	}
	
	private JPanel initCsvPanel() {
		JPanel csvPanel = new JPanel();
		csvPanel.setBorder(BorderFactory.createTitledBorder(Lang.l().step1File()));
		csvPanel.setLayout(new GridBagLayout());
		
		// one time feed
		JPanel oneTimeFeed = new JPanel();
		oneTimeFeed.setLayout(new GridBagLayout());
		oneTimeFeed.setBorder(BorderFactory.createEtchedBorder());
		JLabel instructionLabel = new JLabel(Lang.l().step1InstructionLabel() + ":");
		instructionLabel.setFont(Constants.DEFAULT_INSTRUCTIONS_FONT_LARGE_BOLD);
		JButton browse =  new JButton(Lang.l().step1BrowseButton());
		browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				step1Controller.browseButtonClicked();
			}
		});
		
		GridBagConstraints gbcOneTimeFeed =  new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,
						2, 2, 2), 0, 0);
		oneTimeFeed.add(instructionLabel, gbcOneTimeFeed);
		gbcOneTimeFeed.gridx = 1;
		oneTimeFeed.add(csvFileTextField, gbcOneTimeFeed);
		gbcOneTimeFeed.gridx = 2;
		oneTimeFeed.add(browse, gbcOneTimeFeed);
		csvFileTextField.setToolTipText(ToolTips.get(ToolTips.CSV_File));
		
		// repetitive feed
		RepetitiveFeedKeyListener keyListener = new RepetitiveFeedKeyListener();
		JPanel repetitiveFeed = new JPanel();
		repetitiveFeed.setBorder(BorderFactory.createEtchedBorder());
		repetitiveFeed.setLayout(new GridBagLayout());
		JLabel jlUrl = new JLabel("FTP-Server:");
		JLabel jlUser = new JLabel("Benutzer:");
		JLabel jlPassword = new JLabel("Passwort:");
		JLabel jlRegex = new JLabel("<html>Reguläre<br/>Ausdrücke:</html>");
		JLabel jlRegexDesc = new JLabel("<html>Hinweis: Wählen Sie diese Option, um " +
				"nachfolgend dynamische Ordner- oder/und Dateistrukturen zu beschreiben. " +
				"Achten Sie dabei auf die Bedeutung spezieller Zeichen von regulären " +
				"Ausdrücken, insbesonde Escape-Zeichen.</html>");
		JLabel jlDirectory = new JLabel("Pfad:");
		JLabel jlFileSchema = new JLabel("Dateinamen-Schema:");
		
		// this keylistener instantly checks whether the input data is sufficient
		jtfUrl.addKeyListener(keyListener);
		jtfUser.addKeyListener(keyListener);
		jpfPassword.addKeyListener(keyListener); 
		jtfDirectory.addKeyListener(keyListener);
		jtfFilenameSchema.addKeyListener(keyListener);
		
		GridBagConstraints gbcLabel =  new GridBagConstraints(0, 0, 2, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,
						2, 2, 2), 0, 0);
		GridBagConstraints gbcInput = new GridBagConstraints(3, 0, 2, 1, 1, 0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(2,
						2, 2, 2), 0, 0);
		
		gbcLabel.gridy = gbcInput.gridy = 0;
		repetitiveFeed.add(jlUrl, gbcLabel);
		repetitiveFeed.add(jtfUrl, gbcInput);
		gbcLabel.gridy = gbcInput.gridy = 1;
		repetitiveFeed.add(jlUser, gbcLabel);
		repetitiveFeed.add(jtfUser, gbcInput);
		gbcLabel.gridy = gbcInput.gridy = 2;
		repetitiveFeed.add(jlPassword, gbcLabel);
		repetitiveFeed.add(jpfPassword, gbcInput);
		
		gbcLabel.gridy = gbcInput.gridy = 3;
		gbcLabel.gridwidth = 1;
		repetitiveFeed.add(jlRegex, gbcLabel);
		gbcLabel.gridwidth = 2;
		gbcLabel.gridx = 1;
		gbcLabel.anchor = GridBagConstraints.CENTER;
		gbcLabel.fill = GridBagConstraints.NONE;
		repetitiveFeed.add(jcbRegex, gbcLabel);
		gbcLabel.gridx = 0;
		gbcLabel.anchor = GridBagConstraints.WEST;
		gbcLabel.fill = GridBagConstraints.BOTH;
		repetitiveFeed.add(jlRegexDesc, gbcInput);
		
		gbcLabel.gridy = gbcInput.gridy = 4;
		repetitiveFeed.add(jlDirectory, gbcLabel);
		repetitiveFeed.add(jtfDirectory, gbcInput);
		gbcLabel.gridy = gbcInput.gridy = 5;
		repetitiveFeed.add(jlFileSchema, gbcLabel);
		repetitiveFeed.add(jtfFilenameSchema, gbcInput);

		// feeding type chooser section
		cardPanel.add(oneTimeFeed, "onetime");
		cardPanel.add(repetitiveFeed, "repetitive");
		
		jcbChooseInputType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (jcbChooseInputType.getSelectedIndex() == 0) {
					((CardLayout) cardPanel.getLayout()).show(cardPanel, "onetime");
					step1Controller.checkInputFileValue();
				} else {
					((CardLayout) cardPanel.getLayout()).show(cardPanel, "repetitive");
					inputTyped();
				}
			}
		});
		
		GridBagConstraints gbcChoose = new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,
						5, 5, 5), 0, 0);
		
		csvPanel.add(jcbChooseInputType, gbcChoose);
		gbcChoose.gridy = 1;
		gbcChoose.fill = GridBagConstraints.HORIZONTAL;
		csvPanel.add(cardPanel, gbcChoose);
		
		return csvPanel;
	}
	
	public int getFeedingType() {
		return (jcbChooseInputType.getSelectedIndex() == CSV_FILE)? CSV_FILE : FTP_FILE;
	}
	
	public void setFeedingType(int feedingType) {
		if (feedingType == CSV_FILE) {
			((CardLayout) cardPanel.getLayout()).show(cardPanel, "onetime");
			step1Controller.checkInputFileValue();
			jcbChooseInputType.setSelectedIndex(0);
		} else {
			((CardLayout) cardPanel.getLayout()).show(cardPanel, "repetitive");
			inputTyped();
			jcbChooseInputType.setSelectedIndex(1);
		}
	}
	
	public String getUrl() {
		return jtfUrl.getText();
	}
	
	public void setUrl(String url) {
		jtfUrl.setText(url);
		inputTyped();
	}
	
	public String getUser() {
		return jtfUser.getText();
	}
	
	public void setUser(String user) {
		jtfUser.setText(user);
		inputTyped();
	}
	
	public String getPassword() {
		String password = new String();
		for (int i = 0; i < jpfPassword.getPassword().length; i++) {
			password += jpfPassword.getPassword()[i];
		}
		return password;
	}
	
	public void setPassword(String password) {
		jpfPassword.setText(password);
		inputTyped();
	}
	
	public boolean getRegexStatus() {
		return jcbRegex.isSelected();
	}

	public void setRegexStatus(boolean isSelected) {
		jcbRegex.setSelected(isSelected);
		inputTyped();
	}

	public String getDirectory() {
		return jtfDirectory.getText();
	}
	
	public void setDirectory(String directory) {
		jtfDirectory.setText(directory);
		inputTyped();
	}
	
	public String getFilenameSchema() {
		return jtfFilenameSchema.getText();
	}
	
	public void setFilenameSchema(String filenameSchema) {
		jtfFilenameSchema.setText(filenameSchema);
		inputTyped();
	}
	
	public void setCSVFilePath(String filePath) {
		csvFileTextField.setText(filePath);
	}
	
	public String getCSVFilePath() {
		return csvFileTextField.getText();
	}
	
	private JPanel initLanguagePanel() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(Lang.l().step1SelectLanguage());
		JComboBox jcb = new JComboBox(Lang.getAvailableLocales());
		jcb.setSelectedItem(Lang.getCurrentLocale());
		jcb.setEditable(false);
		//
		jcb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
		        Locale selectedLocale = (Locale)cb.getSelectedItem();
		        Lang.setCurrentLocale(selectedLocale);
		        ToolTips.loadSettings();
		        // restart application drawing -> BUG 619 
		        BackNextController.getInstance().restartCurrentStep();
			}
		});
		//
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panel.add(label);
		panel.add(jcb);
		return panel;
	}

	/**
	 * This method reads the welcome message from a HTML file and presents it 
	 * using a <code>JEditorPane</code>
	 * {@link javax.swing.JEditorPane }
	 */
	private JScrollPane initWelcomePanel(){
		JEditorPane pane  = new JEditorPane();
		JScrollPane scrollPane = new JScrollPane(pane);
		String t = welcomeRes.getString(Constants.language());
		//
		pane.setEditable(false);
		pane.setContentType(Constants.WELCOME_RES_CONTENT_TYPE);
		pane.setText(t);
		pane.setBackground(Constants.DEFAULT_COLOR_BACKGROUND);
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
		scrollPane.setPreferredSize(new Dimension(Constants.DIALOG_WIDTH-20, 400));
		scrollPane.setAutoscrolls(true);
		scrollPane.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Lang.l().step1Introduction(), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		scrollPane.setWheelScrollingEnabled(true);
		if(Constants.GUI_DEBUG) {
			scrollPane.setBorder(Constants.DEBUG_BORDER);
		}
		return scrollPane;
	}
	
	/*
	 * Instantly checks whether the data, to recieve a file from a ftp server,
	 * are complete. Only the server's url and a file name respectively a file
	 * schema are mandatory.
	 */
	private void inputTyped() {
		// repetitive feed inputs ok
		if (getUrl() != null && !getUrl().equals("")
				&& getFilenameSchema() != null
				&& !getFilenameSchema().equals("")) {
			BackNextController.getInstance().setNextButtonEnabled(true);
		} else {
			BackNextController.getInstance().setNextButtonEnabled(false);
		}
	}
	
	private class RepetitiveFeedKeyListener implements KeyListener {

		public void keyPressed(KeyEvent e) {
		}

		
		public void keyReleased(KeyEvent e) {
			inputTyped();
		}

		public void keyTyped(KeyEvent e) {
		}
		
	}
}
