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
package org.n52.sos.importer.model.table;

import java.util.HashSet;

import org.n52.sos.importer.controller.TableController;

/**
 * represents a column in the table
 *
 * @author Raimund
 */
public class Column extends TableElement {

    private int number = -1;
    private int firstLineWithData = -1;

    /**
     * <p>Constructor for Column.</p>
     *
     * @param number a int.
     * @param firstLineWithData a int.
     */
    public Column(int number, int firstLineWithData) {
        this.number = number;
        this.firstLineWithData = firstLineWithData;
    }

    /**
     * <p>Setter for the field <code>number</code>.</p>
     *
     * @param number a int.
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * <p>Getter for the field <code>number</code>.</p>
     *
     * @return a int.
     */
    public int getNumber() {
        return number;
    }

    /**
     * <p>mark.</p>
     */
    @Override
    public void mark() {
        TableController.getInstance().mark(this);
    }

    @Override
    public String getValueFor(Cell c) {
        return TableController.getInstance().getValueAt(c.getRow(), this.getNumber());
    }

    @Override
    public Cell getCellFor(Cell c) {
        return new Cell(c.getRow(), this.getNumber());
    }

    @Override
    public HashSet<String> getValues() {
        HashSet<String> values = new HashSet<>();
        for (int i = this.firstLineWithData; i < TableController.getInstance().getRowCount(); i++) {
            String value = TableController.getInstance().getValueAt(i, this.getNumber());
            if (value != null && !value.equalsIgnoreCase("")) {
                values.add(value);
            }
        }
        return values;
    }

    @Override
    public String toString() {
        return "column[#:" + number + "; fLWD:" + firstLineWithData + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + number;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Column)) {
            return false;
        }
        Column other = (Column) obj;
        return number == other.number;
    }

    /**
     * <p>Getter for the field <code>firstLineWithData</code>.</p>
     *
     * @return a int.
     */
    public int getFirstLineWithData() {
        return firstLineWithData;
    }

    /**
     * <p>Setter for the field <code>firstLineWithData</code>.</p>
     *
     * @param firstLineWithData the firstLineWithData to set
     */
    public void setFirstLineWithData(int firstLineWithData) {
        this.firstLineWithData = firstLineWithData;
    }
}
