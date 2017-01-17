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
package org.n52.sos.importer.controller;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step4aModel;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.Step4Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Solves ambiguities in case there is more than one date&amp;time column.
 *
 * @author Raimund
 * @version $Id: $Id
 */
public class Step4aController extends StepController {

	private static final Logger logger = LoggerFactory.getLogger(Step4aController.class);

	private Step4aModel step4aModel;

	private Step4Panel step4Panel;

	private final TableController tableController;

	/**
	 * <p>Constructor for Step4aController.</p>
	 *
	 * @param step4bModel a {@link org.n52.sos.importer.model.Step4aModel} object.
	 */
	public Step4aController(final Step4aModel step4bModel) {
		step4aModel = step4bModel;
		tableController = TableController.getInstance();
	}

	/** {@inheritDoc} */
	@Override
	public void loadSettings() {
		String text = step4aModel.getDescription();
		final String orientation = tableController.getOrientationString();
		final int fLWData = step4aModel.getFirstLineWithData();
		text = text.replaceAll(Constants.STRING_REPLACER, orientation);
		step4Panel = new Step4Panel(text);

		tableController.setTableSelectionMode(TableController.COLUMNS);
		tableController.addMultipleSelectionListener(new SelectionChanged(fLWData));

		final int[] selectedRowsOrColumns = step4aModel.getSelectedRowsOrColumns();
		for (final int number: selectedRowsOrColumns) {
			final Column c = new Column(number,fLWData);
			final MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAt(c);
			mv.setDateAndTime(null);
			tableController.selectColumn(number);
		}

		final DateAndTimeController dateAndTimeController = new DateAndTimeController(step4aModel.getDateAndTimeModel());
		dateAndTimeController.markComponents();
	}

	/** {@inheritDoc} */
	@Override
	public void saveSettings() {
		final int[] selectedRowsOrColumns = tableController.getSelectedColumns();
		final DateAndTime dateAndTime = step4aModel.getDateAndTimeModel();
		step4aModel.setSelectedRowsOrColumns(selectedRowsOrColumns);
		final int fLWData = step4aModel.getFirstLineWithData();

		for (final int number: selectedRowsOrColumns) {
			final Column c = new Column(number,fLWData);
			final MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAt(c);
			mv.setDateAndTime(dateAndTime);
		}

		tableController.clearMarkedTableElements();
		tableController.deselectAllColumns();
		tableController.setTableSelectionMode(TableController.CELLS);
		tableController.removeMultipleSelectionListener();
		step4Panel = null;
	}

	/** {@inheritDoc} */
	@Override
	public void back() {
		tableController.clearMarkedTableElements();
		tableController.deselectAllColumns();
		tableController.setTableSelectionMode(TableController.CELLS);
		tableController.removeMultipleSelectionListener();

		step4Panel = null;
	};

	/** {@inheritDoc} */
	@Override
	public String getDescription() {
		return Lang.l().step4aDescription();
	}

	/** {@inheritDoc} */
	@Override
	public JPanel getStepPanel() {
		return step4Panel;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isNecessary() {
		final int dateAndTimes = ModelStore.getInstance().getDateAndTimes().size();

		if (dateAndTimes == 0) {
			logger.info("Skip Step 4a since there are not any Date&Times");
			return false;
		}
		if (dateAndTimes == 1) {
			final DateAndTime dateAndTime = ModelStore.getInstance().getDateAndTimes().get(0);
			logger.info("Skip Step 4a since there is just " + dateAndTime);

			for (final MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
				mv.setDateAndTime(dateAndTime);
			}

			return false;
		}

		final DateAndTime dtm = getNextUnassignedDateAndTime();
		step4aModel = new Step4aModel(dtm,step4aModel.getFirstLineWithData());
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public StepController getNext() {
		final DateAndTime dtm = getNextUnassignedDateAndTime();
		if (dtm != null) {
			// this method is called after a Step4aController has existed before
			final Step4aModel step4aModel = new Step4aModel(dtm,this.step4aModel.getFirstLineWithData());
			return new Step4aController(step4aModel);
		}

		return null;
	}

	/** {@inheritDoc} */
	@Override
	public StepController getNextStepController() {
		return new Step4bController(step4aModel.getFirstLineWithData());
	}

	private DateAndTime getNextUnassignedDateAndTime() {
		final boolean unassignedMeasuredValues = areThereAnyUnassignedMeasuredValuesLeft();
		if (!unassignedMeasuredValues) {
			return null;
		}

		for (final DateAndTime dateAndTime: ModelStore.getInstance().getDateAndTimes()) {
			if (!isAssignedToMeasuredValue(dateAndTime)) {
				return dateAndTime;
			}
		}
		return null;
	}

	private boolean areThereAnyUnassignedMeasuredValuesLeft() {
		for (final MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
			if (mv.getDateAndTime() == null) {
				return true;
			}
		}
		return false;
	}

	private boolean isAssignedToMeasuredValue(final DateAndTime dateAndTime) {
		for (final MeasuredValue mv: ModelStore.getInstance().getMeasuredValues()) {
			if (dateAndTime.equals(mv.getDateAndTime())) {
				return true;
			}
		}
		return false;
	}

	private class SelectionChanged implements TableController.MultipleSelectionListener {

		private final int firstLineWithData;

		public SelectionChanged(final int fLWData) {
			firstLineWithData = fLWData;
		}

		@Override
		public void columnSelectionChanged(final int[] selectedColumns) {
			for (final int number: selectedColumns) {
				final Column c = new Column(number,firstLineWithData);
				final MeasuredValue mv = ModelStore.getInstance().getMeasuredValueAt(c);
				if (mv == null) {
					JOptionPane.showMessageDialog(null,
						    Lang.l().step4aInfoMeasuredValue(),
						    Lang.l().errorDialogTitle(),
						    JOptionPane.ERROR_MESSAGE);
					tableController.deselectColumn(number);
					return;
				}

				//measured value has already assigned a date&time
				if (mv.getDateAndTime() != null) {
					JOptionPane.showMessageDialog(null,
						    Lang.l().step4aInfoDateAndTime(),
						    Lang.l().infoDialogTitle(),
						    JOptionPane.INFORMATION_MESSAGE);
					tableController.deselectColumn(number);
					return;
				}
			}
		}

		@Override
		public void rowSelectionChanged(final int[] selectedRows) {
			// Do nothing here!
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean isFinished() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public StepModel getModel() {
		return step4aModel;
	}
}
