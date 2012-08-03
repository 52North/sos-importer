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
package org.n52.sos.importer.feeder.task;

import java.io.File;
import java.io.FileFilter;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.n52.sos.importer.feeder.Configuration;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class RepeatedFeeder extends TimerTask{
	
	private static final Logger logger = Logger.getLogger(RepeatedFeeder.class);

	private Configuration configuration;
	private File file;
	
	final private static Lock oneFeederLock = new ReentrantLock(true);
	
	private static File lastUsedDateFile;
	
	public RepeatedFeeder(Configuration c) {
		this(c,c.getDataFile());
	}

	public RepeatedFeeder(Configuration c, File f) {
		configuration = c;
		file = f;
	}

	@Override
	public void run() {
		File datafile;
		oneFeederLock.lock(); // used to sync access to lastUsedDateFile and to not have more than one feeder at a time.
		try {
			// if file is a directory, get latest from file list
			if (file.isDirectory()) {
				File[] files = file.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						return pathname.isFile();
					}
				});
				if (files != null) {

					File newestFile = files[0];
					for (int i = 1; i < files.length; i++) {
						if (files[i].lastModified() > newestFile.lastModified()) {
							newestFile = files[i];
						}
					}
					datafile = newestFile;
					if (datafile.equals(lastUsedDateFile)) {
						logger.error(String.format("No new file found in directory \"%s\". Last used file was \"%s\".",
								file.getAbsolutePath(),
								lastUsedDateFile.getName()));
						return;
					} else {
						lastUsedDateFile = datafile;
					}
				} else {
					logger.fatal(String.format("No file found in directory \"%s\"",file.getAbsolutePath()));
					return;
				}
			} else {
				datafile = file;
			}
			// otf with file override used not as thread
			new OneTimeFeeder(configuration, datafile).run();
		} finally {
			oneFeederLock.unlock();
		}
	}

}
