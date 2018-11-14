/*
 * Copyright (C) 2011-2018 52°North Initiative for Geospatial Open Source
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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.controller.BackNextController;
import org.n52.sos.importer.view.i18n.Lang;


/**
 * panel for back and next (and finish) button at the bottom of the main frame
 *
 * @author Raimund
 */
public final class BackNextPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static BackNextPanel instance;

    private JButton back;
    private JButton next;
    private JButton finish;

    private BackNextPanel() {
        super();
        if (Constants.isGuiDebug()) {
            setBorder(Constants.DEBUG_BORDER);
        }
        GridLayout layout = new GridLayout(1, 3);
        setLayout(layout);
        /*
         *  BACK button on the left
         */
        back = new JButton(Lang.l().backButtonLabel());
        back.addActionListener(new BackButtonClicked());
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.add(this.back);
        add(backPanel);
        /*
         * EMPTY panel in the middle
         */
        JPanel emptyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(emptyPanel);
        /*
         * NEXT,FINISH on the right
         */
        JPanel nextFinishPanel  = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // finish
        finish = new JButton(Lang.l().finishButtonLabel());
        finish.setVisible(false);
        finish.addActionListener(new FinishButtonClicked());
        nextFinishPanel.add(this.finish);
        // next
        next = new JButton(Lang.l().nextButtonLabel());
        next.addActionListener(new NextButtonClicked());
        nextFinishPanel.add(this.next);
        add(nextFinishPanel);
    }

    /**
     * <p>Getter for the field <code>instance</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.view.BackNextPanel} object.
     */
    public static BackNextPanel getInstance() {
        if (instance == null) {
            instance = new BackNextPanel();
        }
        // workaround for language switch
        instance.back.setText(Lang.l().backButtonLabel());
        instance.finish.setText(Lang.l().finishButtonLabel());
        instance.next.setText(Lang.l().nextButtonLabel());
        return instance;
    }

    /**
     * <p>setBackButtonVisible.</p>
     *
     * @param flag a boolean.
     */
    public void setBackButtonVisible(boolean flag) {
        back.setVisible(flag);
    }

    /**
     * <p>changeNextToFinish.</p>
     */
    public void changeNextToFinish() {
        next.setVisible(false);
        finish.setVisible(true);
    }

    /**
     * <p>changeFinishToNext.</p>
     */
    public void changeFinishToNext() {
        finish.setVisible(false);
        next.setVisible(true);
    }

    /**
     * <p>setFinishButtonEnabled.</p>
     *
     * @param aFlag a boolean.
     */
    public void setFinishButtonEnabled(boolean aFlag) {
        finish.setEnabled(aFlag);
    }

    /**
     * <p>setNextButtonEnabled.</p>
     *
     * @param isNextEnabled a boolean.
     */
    public void setNextButtonEnabled(boolean isNextEnabled) {
        next.setEnabled(isNextEnabled);
    }

    private static class BackButtonClicked implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            BackNextController.getInstance().backButtonClicked();
        }
    }

    private static class NextButtonClicked implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            BackNextController.getInstance().nextButtonClicked();
        }
    }

    private static class FinishButtonClicked implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            BackNextController.getInstance().finishButtonClicked();
        }
    }
}
