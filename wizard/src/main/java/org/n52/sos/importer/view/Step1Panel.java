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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedMap;

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
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.BackNextController;
import org.n52.sos.importer.controller.Step1Controller;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.utils.ToolTips;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * chooses a CSV file
 *
 * @author Raimund
 * @version $Id: $Id
 */
public class Step1Panel extends JPanel {

    private static final String DEFAULT_FILE_ENCODING = "utf-8";
    static final long serialVersionUID = 1L;
    private final Step1Controller step1Controller;

    private final String[] feedingTypes = new String[]{Lang.l().step1FeedTypeCSV(), Lang.l().step1FeedTypeFTP()};

    // separation of type cases
    /** Constant <code>CSV_FILE=0</code> */
    public static final int CSV_FILE = 0;
    /** Constant <code>FTP_FILE=1</code> */
    public static final int FTP_FILE = 1;

    private static final Logger logger = LoggerFactory.getLogger(Step1Panel.class);
    private final JTextField csvFileTextField = new JTextField(25);
    private final JTextField jtfUrl = new JTextField();
    private final JTextField jtfUser = new JTextField();
    private final JTextField jtfDirectory = new JTextField();
    private final JTextField jtfFilenameSchema = new JTextField();
    private final JPasswordField jpfPassword = new JPasswordField();
    private final JCheckBox jcbRegex = new JCheckBox();
    private final JComboBox<String> jcbChooseInputType = new JComboBox<String>(feedingTypes);
    private final Step1Panel _this = this;
    private final JPanel cardPanel = new JPanel(new CardLayout());
    private JComboBox<String> encodingCB;

    private static final String welcomeResBunName = "org.n52.sos.importer.html.welcome"; //$NON-NLS-1$

    private static final ResourceBundle welcomeRes = ResourceBundle.getBundle(welcomeResBunName);

    /**
     * <p>Constructor for Step1Panel.</p>
     *
     * @param step1Controller a {@link org.n52.sos.importer.controller.Step1Controller} object.
     */
    public Step1Panel(final Step1Controller step1Controller) {
        super();
        final JScrollPane welcomePanel = initWelcomePanel();
        final JPanel languagePanel = initLanguagePanel();
        final JPanel csvPanel = initCsvPanel();
        this.step1Controller = step1Controller;

        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        add(languagePanel);
        add(csvPanel);
        add(welcomePanel);
    }

    private JPanel initCsvPanel() {
        final JPanel csvPanel = new JPanel();
        csvPanel.setBorder(BorderFactory.createTitledBorder(Lang.l().step1File()));
        csvPanel.setLayout(new GridBagLayout());

        // one time feed
        final JPanel oneTimeFeed = new JPanel();
        oneTimeFeed.setLayout(new GridBagLayout());
        oneTimeFeed.setBorder(BorderFactory.createEtchedBorder());
        final JLabel instructionLabel = new JLabel(Lang.l().step1InstructionLabel() + ":");
        instructionLabel.setFont(Constants.DEFAULT_INSTRUCTIONS_FONT_LARGE_BOLD);
        final JButton browse =  new JButton(Lang.l().step1BrowseButton());
        browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                step1Controller.browseButtonClicked();
            }
        });

        final JLabel encodingLabel = new JLabel(Lang.l().step1EncodingLabel() + ":");
        encodingLabel.setFont(Constants.DEFAULT_INSTRUCTIONS_FONT_LARGE_BOLD);
        final Set<String> charsets = getCharsets();
        encodingCB = new JComboBox<String>(charsets.toArray(new String[charsets.size()]));
        encodingCB.setSelectedIndex(getIndexOfEncoding(DEFAULT_FILE_ENCODING));

        final GridBagConstraints gbcOneTimeFeed =  new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,
                        2, 2, 2), 0, 0);
        oneTimeFeed.add(instructionLabel, gbcOneTimeFeed);
        gbcOneTimeFeed.gridx = 1;
        oneTimeFeed.add(csvFileTextField, gbcOneTimeFeed);
        gbcOneTimeFeed.gridx = 2;
        oneTimeFeed.add(browse, gbcOneTimeFeed);
        gbcOneTimeFeed.gridx = 0;
        gbcOneTimeFeed.gridy = 2;
        oneTimeFeed.add(encodingLabel, gbcOneTimeFeed);
        gbcOneTimeFeed.gridx = 1;
        oneTimeFeed.add(encodingCB, gbcOneTimeFeed);
        csvFileTextField.setToolTipText(ToolTips.get(ToolTips.CSV_File));

        // repetitive feed
        final RepetitiveFeedKeyListener keyListener = new RepetitiveFeedKeyListener();
        final JPanel repetitiveFeed = new JPanel();
        repetitiveFeed.setBorder(BorderFactory.createEtchedBorder());
        repetitiveFeed.setLayout(new GridBagLayout());
        final JLabel jlUrl = new JLabel(Lang.l().step1FtpServer() + ":");
        final JLabel jlUser = new JLabel(Lang.l().step1User() + ":");
        final JLabel jlPassword = new JLabel(Lang.l().step1Password() + ":");
        final JLabel jlRegex = new JLabel(Lang.l().step1Regex() + ":");
        final JLabel jlRegexDesc = new JLabel(Lang.l().step1RegexDescription() + ":");
        final JLabel jlDirectory = new JLabel(Lang.l().step1Directory() + ":");
        final JLabel jlFileSchema = new JLabel(Lang.l().step1FileSchema() + ":");

        // this keylistener instantly checks whether the input data is sufficient
        jtfUrl.addKeyListener(keyListener);
        jtfUser.addKeyListener(keyListener);
        jpfPassword.addKeyListener(keyListener);
        jtfDirectory.addKeyListener(keyListener);
        jtfFilenameSchema.addKeyListener(keyListener);

        final GridBagConstraints gbcLabel =  new GridBagConstraints(0, 0, 2, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,
                        2, 2, 2), 0, 0);
        final GridBagConstraints gbcInput = new GridBagConstraints(3, 0, 2, 1, 1, 0,
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
            @Override
            public void actionPerformed(final ActionEvent ae) {
                if (jcbChooseInputType.getSelectedIndex() == 0) {
                    ((CardLayout) cardPanel.getLayout()).show(cardPanel, "onetime");
                    step1Controller.checkInputFileValue();
                } else {
                    ((CardLayout) cardPanel.getLayout()).show(cardPanel, "repetitive");
                    inputTyped();
                }
            }
        });

        final GridBagConstraints gbcChoose = new GridBagConstraints(0, 0, 1, 1, 1, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,
                        5, 5, 5), 0, 0);

        csvPanel.add(jcbChooseInputType, gbcChoose);
        gbcChoose.gridy = 1;
        gbcChoose.fill = GridBagConstraints.HORIZONTAL;
        csvPanel.add(cardPanel, gbcChoose);

        return csvPanel;
    }

    private Set<String> getCharsets() {
        try {
            final SortedMap<String, Charset> availableCharsets = Charset.availableCharsets();
            return availableCharsets.keySet();
        } catch (final Exception e) {
            return Collections.singleton("UTF-8");
        }
    }

    private int getIndexOfEncoding(String encodingName) {
        int index = 0;
        if (encodingName == null || encodingName.isEmpty()) {
            encodingName = "UTF-8";
        }
        for (final String string : getCharsets()) {
            if (string.equalsIgnoreCase(encodingName)) {
                return index;
            }
            index++;
        }
        return 0;
    }

    /**
     * <p>getFeedingType.</p>
     *
     * @return a int.
     */
    public int getFeedingType() {
        return (jcbChooseInputType.getSelectedIndex() == CSV_FILE)? CSV_FILE : FTP_FILE;
    }

    /**
     * <p>setFeedingType.</p>
     *
     * @param feedingType a int.
     */
    public void setFeedingType(final int feedingType) {
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

    /**
     * <p>getUrl.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUrl() {
        final String protocollCheck = "ftp://";
        final String result = jtfUrl.getText();
        if (result.startsWith(protocollCheck)) {
            return result.substring(protocollCheck.length());
        }
        return result;
    }

    /**
     * <p>setUrl.</p>
     *
     * @param url a {@link java.lang.String} object.
     */
    public void setUrl(final String url) {
        jtfUrl.setText(url);
        inputTyped();
    }

    /**
     * <p>getUser.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUser() {
        return jtfUser.getText();
    }

    /**
     * <p>setUser.</p>
     *
     * @param user a {@link java.lang.String} object.
     */
    public void setUser(final String user) {
        jtfUser.setText(user);
        inputTyped();
    }

    /**
     * <p>getPassword.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPassword() {
        String password = new String();
        for (int i = 0; i < jpfPassword.getPassword().length; i++) {
            password += jpfPassword.getPassword()[i];
        }
        return password;
    }

    /**
     * <p>setPassword.</p>
     *
     * @param password a {@link java.lang.String} object.
     */
    public void setPassword(final String password) {
        jpfPassword.setText(password);
        inputTyped();
    }

    /**
     * <p>getRegexStatus.</p>
     *
     * @return a boolean.
     */
    public boolean getRegexStatus() {
        return jcbRegex.isSelected();
    }

    /**
     * <p>setRegexStatus.</p>
     *
     * @param isSelected a boolean.
     */
    public void setRegexStatus(final boolean isSelected) {
        jcbRegex.setSelected(isSelected);
        inputTyped();
    }

    /**
     * <p>getDirectory.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDirectory() {
        return jtfDirectory.getText();
    }

    /**
     * <p>setDirectory.</p>
     *
     * @param directory a {@link java.lang.String} object.
     */
    public void setDirectory(final String directory) {
        jtfDirectory.setText(directory);
        inputTyped();
    }

    /**
     * <p>getFilenameSchema.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFilenameSchema() {
        return jtfFilenameSchema.getText();
    }

    /**
     * <p>setFilenameSchema.</p>
     *
     * @param filenameSchema a {@link java.lang.String} object.
     */
    public void setFilenameSchema(final String filenameSchema) {
        jtfFilenameSchema.setText(filenameSchema);
        inputTyped();
    }

    /**
     * <p>setCSVFilePath.</p>
     *
     * @param filePath a {@link java.lang.String} object.
     */
    public void setCSVFilePath(final String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            csvFileTextField.setText(filePath);
        }
    }

    /**
     * <p>getCSVFilePath.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCSVFilePath() {
        return csvFileTextField.getText();
    }

    /**
     * <p>setFileEncoding.</p>
     *
     * @param encoding a {@link java.lang.String} object.
     */
    public void setFileEncoding(final String encoding) {
        try {
            encodingCB.setSelectedIndex(getIndexOfEncoding(encoding));
        } catch (final IllegalArgumentException iae) {
            encodingCB.setSelectedIndex(getIndexOfEncoding(DEFAULT_FILE_ENCODING));
        }
    }

    /**
     * <p>getFileEncoding.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFileEncoding() {
        return (String) encodingCB.getSelectedItem();
    }

    private JPanel initLanguagePanel() {
        final JPanel panel = new JPanel();
        final JLabel label = new JLabel(Lang.l().step1SelectLanguage());
        final JComboBox<Locale> jcb = new JComboBox<Locale>(Lang.getAvailableLocales());
        jcb.setSelectedItem(Lang.getCurrentLocale());
        jcb.setEditable(false);
        //
        jcb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final JComboBox<?> cb = (JComboBox<?>)e.getSource();
                final Locale selectedLocale = (Locale)cb.getSelectedItem();
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
        final JEditorPane pane  = new JEditorPane();
        final JScrollPane scrollPane = new JScrollPane(pane);
        final String t = welcomeRes.getString(Constants.language());
        //
        pane.setEditable(false);
        pane.setContentType(Constants.WELCOME_RES_CONTENT_TYPE);
        pane.setText(t);
        pane.setBackground(Constants.DEFAULT_COLOR_BACKGROUND);
        pane.setDragEnabled(true);
        //
        // Add simple hyperlink functionality -> call system Browser with URL
        pane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(final HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                      // try to start system browser with link
                    if(Desktop.isDesktopSupported()) {
                        try {
                            if (e.getURL() != null) {
                                Desktop.getDesktop().browse(e.getURL().toURI());
                            }
                        } catch (final IOException e1) {
                            final String error = "Could not start system browser with URL: \"" + e.getURL() + "\"";
                            logger.error(error, e1);
                            JOptionPane.showMessageDialog(_this, error, "Error Opening Browser", JOptionPane.ERROR_MESSAGE);
                        } catch (final URISyntaxException e1) {
                            final String error = "Syntax error in URL: \"" + e.getURL() + "\"";
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
        if(Constants.isGuiDebug()) {
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

    private class RepetitiveFeedKeyListener extends KeyAdapter {

        @Override
        public void keyReleased(final KeyEvent e) {
            inputTyped();
        }

    }
}
