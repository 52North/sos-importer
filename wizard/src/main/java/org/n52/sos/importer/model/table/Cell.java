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
 * represents a single cell in the table
 *
 * @author Raimund
 */
public class Cell extends TableElement {

    private int column;

    private int row;

    /**
     * <p>Constructor for Cell.</p>
     */
    public Cell() {
        column = -1;
        row = -1;
    }

    /**
     * <p>Constructor for Cell.</p>
     *
     * @param row a int.
     * @param column a int.
     */
    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * <p>Setter for the field <code>column</code>.</p>
     *
     * @param column a int.
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * <p>Getter for the field <code>column</code>.</p>
     *
     * @return a int.
     */
    public int getColumn() {
        return column;
    }

    /**
     * <p>Setter for the field <code>row</code>.</p>
     *
     * @param row a int.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * <p>Getter for the field <code>row</code>.</p>
     *
     * @return a int.
     */
    public int getRow() {
        return row;
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
        return TableController.getInstance().getValueAt(this);
    }

    @Override
    public Cell getCellFor(Cell c) {
        return this;
    }

    @Override
    public HashSet<String> getValues() {
        HashSet<String> values = new HashSet<>();
        String value = TableController.getInstance().getValueAt(this);
        values.add(value);
        return values;
    }

    @Override
    public String toString() {
        return "cell[" + row + "|" + column + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + column;
        result = prime * result + row;
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
        if (!(obj instanceof Cell)) {
            return false;
        }
        Cell other = (Cell) obj;
        if (column != other.column) {
            return false;
        }
        return row == other.row;
    }

}
