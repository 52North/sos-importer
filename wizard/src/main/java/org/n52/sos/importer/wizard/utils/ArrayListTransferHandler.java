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
package org.n52.sos.importer.wizard.utils;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parts from http://www.java2s.com/Code/Java/Swing-JFC/DragListDemo.htm
 * <br>
 * and from http://java.sun.com/docs/books/tutorial/index.html
 * <br>
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 * <br>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <br>
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 * <br>
 * -Redistribution in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * <br>
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * <br>
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
 * <br>
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 *
 * <p>ArrayListTransferHandler class.</p>
 *
 * @author e.h.juerrens@52north.org
 */
public class ArrayListTransferHandler extends TransferHandler {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ArrayListTransferHandler.class);

    private DataFlavor localArrayListFlavor;

    private DataFlavor serialArrayListFlavor;

    private String localArrayListType = DataFlavor.javaJVMLocalObjectMimeType
            + ";class=java.util.ArrayList";

    @SuppressWarnings("rawtypes")
    private JList source;

    private int[] indices;

    //Location where items were added
    private int addIndex = -1;

    //Number of items added
    private int addCount;

    /**
     * <p>Constructor for ArrayListTransferHandler.</p>
     */
    public ArrayListTransferHandler() {
        try {
            localArrayListFlavor = new DataFlavor(localArrayListType);
        } catch (final ClassNotFoundException e) {
            logger.error(
                    "ArrayListTransferHandler: unable to create data flavor",
                    e);
        }
        serialArrayListFlavor = new DataFlavor(ArrayList.class, "ArrayList");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public boolean importData(final JComponent c, final Transferable t) {
        JList target = null;
        ArrayList<?> alist = null;
        if (!canImport(c, t.getTransferDataFlavors())) {
            return false;
        }
        try {
            target = (JList) c;
            if (hasLocalArrayListFlavor(t.getTransferDataFlavors())) {
                alist = (ArrayList<?>) t.getTransferData(localArrayListFlavor);
            } else if (hasSerialArrayListFlavor(t.getTransferDataFlavors())) {
                alist = (ArrayList<?>) t.getTransferData(serialArrayListFlavor);
            } else {
                return false;
            }
        } catch (final UnsupportedFlavorException ufe) {
            logger.error("importData: unsupported data flavor", ufe);
            return false;
        } catch (final IOException ioe) {
            logger.error("importData: I/O exception", ioe);
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

        final DefaultListModel listModel = (DefaultListModel) target.getModel();
        final int max = listModel.getSize();
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

    @SuppressWarnings("rawtypes")
    @Override
    protected void exportDone(final JComponent c, final Transferable data, final int action) {
        if ((action == MOVE) && (indices != null)) {
            final DefaultListModel model = (DefaultListModel) source.getModel();

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
            for (int i = indices.length - 1; i >= 0; i--) {
                model.remove(indices[i]);
            }
        }
        indices = null;
        addIndex = -1;
        addCount = 0;
    }

    private boolean hasLocalArrayListFlavor(final DataFlavor[] flavors) {
        if (localArrayListFlavor == null) {
            return false;
        }

        for (final DataFlavor flavor : flavors) {
            if (flavor.equals(localArrayListFlavor)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSerialArrayListFlavor(final DataFlavor[] flavors) {
        if (serialArrayListFlavor == null) {
            return false;
        }

        for (final DataFlavor flavor : flavors) {
            if (flavor.equals(serialArrayListFlavor)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canImport(final JComponent c, final DataFlavor[] flavors) {
        if (hasLocalArrayListFlavor(flavors)) {
            return true;
        }
        if (hasSerialArrayListFlavor(flavors)) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Transferable createTransferable(final JComponent c) {
        if (c instanceof JList) {
            source = (JList) c;
            indices = source.getSelectedIndices();
            final List values = source.getSelectedValuesList();
            if (values == null || values.isEmpty()) {
                return null;
            }
            final ArrayList<String> alist = new ArrayList<String>(values.size());
            for (final Object o : values) {
                String str = o.toString();
                alist.add(str);
            }
            return new ArrayListTransferable(alist);
        }
        return null;
    }

    @Override
    public int getSourceActions(final JComponent c) {
        return COPY_OR_MOVE;
    }

    public class ArrayListTransferable implements Transferable {
        private ArrayList<?> data;

        public ArrayListTransferable(final ArrayList<?> alist) {
            data = alist;
        }

        @Override
        public Object getTransferData(final DataFlavor flavor)
                throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return data;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { localArrayListFlavor,
                serialArrayListFlavor, };
        }

        @Override
        public boolean isDataFlavorSupported(final DataFlavor flavor) {
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
