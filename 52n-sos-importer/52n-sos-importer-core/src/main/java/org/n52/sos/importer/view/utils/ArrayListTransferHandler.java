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
/* Parts from http://java.sun.com/docs/books/tutorial/index.html */
/*
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */
package org.n52.sos.importer.view.utils;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;

/**
 * @author e.h.juerrens@52north.org
 * {@link http://www.java2s.com/Code/Java/Swing-JFC/DragListDemo.htm}
 *
 */
/*
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ArrayListTransferHandler extends TransferHandler {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(ArrayListTransferHandler.class);
	
	DataFlavor localArrayListFlavor, serialArrayListFlavor;

	String localArrayListType = DataFlavor.javaJVMLocalObjectMimeType
			+ ";class=java.util.ArrayList";

	JList source = null;

	int[] indices = null;

	int addIndex = -1; //Location where items were added

	int addCount = 0; //Number of items added
	
	public ArrayListTransferHandler() {
		try {
			localArrayListFlavor = new DataFlavor(localArrayListType);
		} catch (ClassNotFoundException e) {
			logger.error(
					"ArrayListTransferHandler: unable to create data flavor",
					e);
		}
		serialArrayListFlavor = new DataFlavor(ArrayList.class, "ArrayList");
	}

	public boolean importData(JComponent c, Transferable t) {
		JList target = null;
		ArrayList alist = null;
		if (!canImport(c, t.getTransferDataFlavors())) {
			return false;
		}
		try {
			target = (JList) c;
			if (hasLocalArrayListFlavor(t.getTransferDataFlavors())) {
				alist = (ArrayList) t.getTransferData(localArrayListFlavor);
			} else if (hasSerialArrayListFlavor(t.getTransferDataFlavors())) {
				alist = (ArrayList) t.getTransferData(serialArrayListFlavor);
			} else {
				return false;
			}
		} catch (UnsupportedFlavorException ufe) {
			logger.error("importData: unsupported data flavor",ufe);
			return false;
		} catch (IOException ioe) {
			logger.error("importData: I/O exception",ioe);
			return false;
		}

		//At this point we use the same code to retrieve the data
		//locally or serially.

		//We'll drop at the current selected index.
		int index = target.getSelectedIndex();

		//Prevent the user from dropping data back on itself.
		//For example, if the user is moving items #4,#5,#6 and #7 and
		//attempts to insert the items after item #5, this would
		//be problematic when removing the original items.
		//This is interpreted as dropping the same data on itself
		//and has no effect.
		if (source.equals(target)) {
			if (indices != null && index >= indices[0] - 1
					&& index <= indices[indices.length - 1]) {
				indices = null;
				return true;
			}
		}

		DefaultListModel listModel = (DefaultListModel) target.getModel();
		int max = listModel.getSize();
		if (index < 0) {
			index = max;
		} else {
			index++;
			if (index > max) {
				index = max;
			}
		}
		addIndex = index;
		addCount = alist.size();
		for (int i = 0; i < alist.size(); i++) {
			listModel.add(index++, alist.get(i));
		}
		return true;
	}

	protected void exportDone(JComponent c, Transferable data, int action) {
		if ((action == MOVE) && (indices != null)) {
			DefaultListModel model = (DefaultListModel) source.getModel();

			//If we are moving items around in the same list, we
			//need to adjust the indices accordingly since those
			//after the insertion point have moved.
			if (addCount > 0) {
				for (int i = 0; i < indices.length; i++) {
					if (indices[i] > addIndex) {
						indices[i] += addCount;
					}
				}
			}
			for (int i = indices.length - 1; i >= 0; i--)
				model.remove(indices[i]);
		}
		indices = null;
		addIndex = -1;
		addCount = 0;
	}

	private boolean hasLocalArrayListFlavor(DataFlavor[] flavors) {
		if (localArrayListFlavor == null) {
			return false;
		}

		for (int i = 0; i < flavors.length; i++) {
			if (flavors[i].equals(localArrayListFlavor)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasSerialArrayListFlavor(DataFlavor[] flavors) {
		if (serialArrayListFlavor == null) {
			return false;
		}

		for (int i = 0; i < flavors.length; i++) {
			if (flavors[i].equals(serialArrayListFlavor)) {
				return true;
			}
		}
		return false;
	}

	public boolean canImport(JComponent c, DataFlavor[] flavors) {
		if (hasLocalArrayListFlavor(flavors)) {
			return true;
		}
		if (hasSerialArrayListFlavor(flavors)) {
			return true;
		}
		return false;
	}

	protected Transferable createTransferable(JComponent c) {
		if (c instanceof JList) {
			source = (JList) c;
			indices = source.getSelectedIndices();
			Object[] values = source.getSelectedValues();
			if (values == null || values.length == 0) {
				return null;
			}
			ArrayList alist = new ArrayList(values.length);
			for (int i = 0; i < values.length; i++) {
				Object o = values[i];
				String str = o.toString();
				if (str == null)
					str = "";
				alist.add(str);
			}
			return new ArrayListTransferable(alist);
		}
		return null;
	}

	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}

	public class ArrayListTransferable implements Transferable {
		ArrayList data;

		public ArrayListTransferable(ArrayList alist) {
			data = alist;
		}

		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			return data;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { localArrayListFlavor,
					serialArrayListFlavor };
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			if (localArrayListFlavor.equals(flavor)) {
				return true;
			}
			if (serialArrayListFlavor.equals(flavor)) {
				return true;
			}
			return false;
		}
	}
}
