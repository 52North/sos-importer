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
