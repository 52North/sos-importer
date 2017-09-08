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
package org.n52.sos.importer.view.combobox;

import javax.swing.DefaultComboBoxModel;

/**
 * contains all models of comboboxes which can be edited by the user
 *
 * @author Raimund
 */
public final class EditableComboBoxItems {

    private static EditableComboBoxItems instance;

    private DefaultComboBoxModel<String> columnSeparators;

    private DefaultComboBoxModel<String> commentIndicators;

    private DefaultComboBoxModel<String> textQualifiers;

    private DefaultComboBoxModel<String> dateAndTimePatterns;

    private DefaultComboBoxModel<String> positionPatterns;

    private DefaultComboBoxModel<String> EPSGCodes;

    private DefaultComboBoxModel<String> referenceSystemNames;

    private DefaultComboBoxModel<String> featureOfInterestNames;

    private DefaultComboBoxModel<String> observedPropertyNames;

    private DefaultComboBoxModel<String> unitOfMeasurementCodes;

    private DefaultComboBoxModel<String> sensorNames;

    private DefaultComboBoxModel<String> featureOfInterestURIs;

    private DefaultComboBoxModel<String> observedPropertyURIs;

    private DefaultComboBoxModel<String> unitOfMeasurementURIs;

    private DefaultComboBoxModel<String> sensorURIs;

    private DefaultComboBoxModel<String> sosURLs;

    private EditableComboBoxItems() {
        setColumnSeparators(new DefaultComboBoxModel<>(
                ComboBoxItems.getInstance().getColumnSeparators()));
        setCommentIndicators(new DefaultComboBoxModel<>(
                ComboBoxItems.getInstance().getCommentIndicators()));
        setTextQualifiers(new DefaultComboBoxModel<>(
                ComboBoxItems.getInstance().getTextQualifiers()));
        setDateAndTimePatterns(new DefaultComboBoxModel<>(
                ComboBoxItems.getInstance().getDateAndTimePatterns()));
        setPositionPatterns(new DefaultComboBoxModel<>(
                ComboBoxItems.getInstance().getPositionPatterns()));
        setEPSGCodes(new DefaultComboBoxModel<>(
                ComboBoxItems.getInstance().getEpsgCodes()));
        setReferenceSystemNames(new DefaultComboBoxModel<>(
                ComboBoxItems.getInstance().getReferenceSystemNames()));
        setSosURLs(new DefaultComboBoxModel<>(
                ComboBoxItems.getInstance().getSosURLs()));

        setFeatureOfInterestNames(new DefaultComboBoxModel<>(
                ComboBoxItems.getInstance().getFeatureOfInterestNames()));
        setObservedPropertyNames(new DefaultComboBoxModel<>(
                ComboBoxItems.getInstance().getObservedPropertyNames()));
        setUnitOfMeasurementCodes(new DefaultComboBoxModel<>(
                ComboBoxItems.getInstance().getUnitOfMeasurementCodes()));
        setSensorNames(new DefaultComboBoxModel<>(
                ComboBoxItems.getInstance().getSensorNames()));
        setFeatureOfInterestURIs(new DefaultComboBoxModel<>(
                ComboBoxItems.getInstance().getFeatureOfInterestURIs()));
        setObservedPropertyURIs(new DefaultComboBoxModel<>(
                ComboBoxItems.getInstance().getObservedPropertyURIs()));
        setUnitOfMeasurementURIs(new DefaultComboBoxModel<>(
                ComboBoxItems.getInstance().getUnitOfMeasurementURIs()));
        setSensorURIs(new DefaultComboBoxModel<>(
                ComboBoxItems.getInstance().getSensorURIs()));
    }

    /**
     * <p>Getter for the field <code>instance</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.view.combobox.EditableComboBoxItems} object.
     */
    public static EditableComboBoxItems getInstance() {
        if (instance == null) {
            instance = new EditableComboBoxItems();
        }
        return instance;
    }

    /**
     * <p>Getter for the field <code>columnSeparators</code>.</p>
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public DefaultComboBoxModel<String> getColumnSeparators() {
        return columnSeparators;
    }

    /**
     * <p>Setter for the field <code>columnSeparators</code>.</p>
     *
     * @param columnSeparators a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public void setColumnSeparators(DefaultComboBoxModel<String> columnSeparators) {
        this.columnSeparators = columnSeparators;
    }

    /**
     * <p>Getter for the field <code>commentIndicators</code>.</p>
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public DefaultComboBoxModel<String> getCommentIndicators() {
        return commentIndicators;
    }

    /**
     * <p>Setter for the field <code>commentIndicators</code>.</p>
     *
     * @param commentIndicators a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public void setCommentIndicators(DefaultComboBoxModel<String> commentIndicators) {
        this.commentIndicators = commentIndicators;
    }

    /**
     * <p>Getter for the field <code>textQualifiers</code>.</p>
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public DefaultComboBoxModel<String> getTextQualifiers() {
        return textQualifiers;
    }

    /**
     * <p>Setter for the field <code>textQualifiers</code>.</p>
     *
     * @param textQualifiers a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public void setTextQualifiers(DefaultComboBoxModel<String> textQualifiers) {
        this.textQualifiers = textQualifiers;
    }

    /**
     * <p>Getter for the field <code>dateAndTimePatterns</code>.</p>
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public DefaultComboBoxModel<String> getDateAndTimePatterns() {
        return dateAndTimePatterns;
    }

    /**
     * <p>Setter for the field <code>dateAndTimePatterns</code>.</p>
     *
     * @param dateAndTimePatterns a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public void setDateAndTimePatterns(DefaultComboBoxModel<String> dateAndTimePatterns) {
        this.dateAndTimePatterns = dateAndTimePatterns;
    }

    /**
     * <p>getEPSGCodes.</p>
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public DefaultComboBoxModel<String> getEPSGCodes() {
        return EPSGCodes;
    }

    /**
     * <p>setEPSGCodes.</p>
     *
     * @param ePSGCodes a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public void setEPSGCodes(DefaultComboBoxModel<String> ePSGCodes) {
        EPSGCodes = ePSGCodes;
    }

    /**
     * <p>Setter for the field <code>sosURLs</code>.</p>
     *
     * @param sosURLs a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public void setSosURLs(DefaultComboBoxModel<String> sosURLs) {
        this.sosURLs = sosURLs;
    }

    /**
     * <p>Getter for the field <code>sosURLs</code>.</p>
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public DefaultComboBoxModel<String> getSosURLs() {
        return sosURLs;
    }

    /**
     * <p>Getter for the field <code>featureOfInterestNames</code>.</p>
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public DefaultComboBoxModel<String> getFeatureOfInterestNames() {
        return featureOfInterestNames;
    }

    /**
     * <p>Setter for the field <code>featureOfInterestNames</code>.</p>
     *
     * @param featureOfInterestNames a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public void setFeatureOfInterestNames(
            DefaultComboBoxModel<String> featureOfInterestNames) {
        this.featureOfInterestNames = featureOfInterestNames;
    }

    /**
     * <p>Getter for the field <code>observedPropertyNames</code>.</p>
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public DefaultComboBoxModel<String> getObservedPropertyNames() {
        return observedPropertyNames;
    }

    /**
     * <p>Setter for the field <code>observedPropertyNames</code>.</p>
     *
     * @param observedPropertyNames a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public void setObservedPropertyNames(DefaultComboBoxModel<String> observedPropertyNames) {
        this.observedPropertyNames = observedPropertyNames;
    }

    /**
     * <p>Getter for the field <code>unitOfMeasurementCodes</code>.</p>
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public DefaultComboBoxModel<String> getUnitOfMeasurementCodes() {
        return unitOfMeasurementCodes;
    }

    /**
     * <p>Setter for the field <code>unitOfMeasurementCodes</code>.</p>
     *
     * @param unitOfMeasurementCodes a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public void setUnitOfMeasurementCodes(
            DefaultComboBoxModel<String> unitOfMeasurementCodes) {
        this.unitOfMeasurementCodes = unitOfMeasurementCodes;
    }

    /**
     * <p>Getter for the field <code>sensorNames</code>.</p>
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public DefaultComboBoxModel<String> getSensorNames() {
        return sensorNames;
    }

    /**
     * <p>Setter for the field <code>sensorNames</code>.</p>
     *
     * @param sensorNames a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public void setSensorNames(DefaultComboBoxModel<String> sensorNames) {
        this.sensorNames = sensorNames;
    }

    /**
     * <p>Getter for the field <code>featureOfInterestURIs</code>.</p>
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public DefaultComboBoxModel<String> getFeatureOfInterestURIs() {
        return featureOfInterestURIs;
    }

    /**
     * <p>Setter for the field <code>featureOfInterestURIs</code>.</p>
     *
     * @param featureOfInterestURIs a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public void setFeatureOfInterestURIs(DefaultComboBoxModel<String> featureOfInterestURIs) {
        this.featureOfInterestURIs = featureOfInterestURIs;
    }

    /**
     * <p>Getter for the field <code>observedPropertyURIs</code>.</p>
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public DefaultComboBoxModel<String> getObservedPropertyURIs() {
        return observedPropertyURIs;
    }

    /**
     * <p>Setter for the field <code>observedPropertyURIs</code>.</p>
     *
     * @param observedPropertyURIs a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public void setObservedPropertyURIs(DefaultComboBoxModel<String> observedPropertyURIs) {
        this.observedPropertyURIs = observedPropertyURIs;
    }

    /**
     * <p>Getter for the field <code>unitOfMeasurementURIs</code>.</p>
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public DefaultComboBoxModel<String> getUnitOfMeasurementURIs() {
        return unitOfMeasurementURIs;
    }

    /**
     * <p>Setter for the field <code>unitOfMeasurementURIs</code>.</p>
     *
     * @param unitOfMeasurementURIs a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public void setUnitOfMeasurementURIs(DefaultComboBoxModel<String> unitOfMeasurementURIs) {
        this.unitOfMeasurementURIs = unitOfMeasurementURIs;
    }

    /**
     * <p>Getter for the field <code>sensorURIs</code>.</p>
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public DefaultComboBoxModel<String> getSensorURIs() {
        return sensorURIs;
    }

    /**
     * <p>Setter for the field <code>sensorURIs</code>.</p>
     *
     * @param sensorURIs a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public void setSensorURIs(DefaultComboBoxModel<String> sensorURIs) {
        this.sensorURIs = sensorURIs;
    }

    /**
     * <p>Setter for the field <code>positionPatterns</code>.</p>
     *
     * @param positionPatterns a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public void setPositionPatterns(DefaultComboBoxModel<String> positionPatterns) {
        this.positionPatterns = positionPatterns;
    }

    /**
     * <p>Getter for the field <code>positionPatterns</code>.</p>
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public DefaultComboBoxModel<String> getPositionPatterns() {
        return positionPatterns;
    }

    /**
     * <p>Setter for the field <code>referenceSystemNames</code>.</p>
     *
     * @param referenceSystemNames a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public void setReferenceSystemNames(DefaultComboBoxModel<String> referenceSystemNames) {
        this.referenceSystemNames = referenceSystemNames;
    }

    /**
     * <p>Getter for the field <code>referenceSystemNames</code>.</p>
     *
     * @return a {@link javax.swing.DefaultComboBoxModel} object.
     */
    public DefaultComboBoxModel<String> getReferenceSystemNames() {
        return referenceSystemNames;
    }
}
