/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.importer.view.position;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.tool.CursorTool;
import org.n52.sos.importer.Constants;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * 
 * Source for icon: http://openclipart.org/detail/16078/crosshairs-by-noxin (2012-07-27)
 *
 */
public class SelectPositionTool extends CursorTool {
	
	public static final String TOOL_NAME = Lang.l().step6cInfoToolName();
	
	public static final String TOOL_TIP = Lang.l().step6cInfoToolTooltip();
	
	public static final String CURSOR_IMAGE = Constants.WMS_VIEW_SELECT_TOOL_ICON_PNG_PATH;
	
	public static final Point CURSOR_HOTSPOT = new Point(0,0);
	
	public static final String ICON_IMAGE = Constants.WMS_VIEW_SELECT_TOOL_ICON_PNG_PATH;
	
	private MissingPositionPanel mpp;
	
	private Cursor cursor;
	
	public SelectPositionTool(MissingPositionPanel mpp) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        ImageIcon cursorIcon = new ImageIcon(getClass().getResource(CURSOR_IMAGE));
        cursor = tk.createCustomCursor(cursorIcon.getImage(), CURSOR_HOTSPOT, TOOL_TIP);
        this.mpp = mpp;
    }
	
	public Cursor getCursor() {
        return cursor;
    }

    public boolean drawDragBox() {
        return false;
    }
    
    public void onMouseClicked(MapMouseEvent ev) {
    	DirectPosition2D pos = ev.getWorldPos();
        mpp.setSelectedPosition(pos);
    }

}
