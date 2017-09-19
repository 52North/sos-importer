package org.n52.sos.importer.feeder.util;

import java.io.File;
import java.io.IOException;

import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.DataFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WebClient {

    protected static final Logger LOG = LoggerFactory.getLogger(WebClient.class);
    
    protected File file;
    
    protected Configuration config;
    
    public WebClient(Configuration config) {
        this.config = config;
    }
    
    public abstract DataFile download();
    
    protected void createTempFile() {
        final String fileName = config.getFileName();
        try {
            file = File.createTempFile(fileName, ".csv");
        } catch (IOException e1) {
            LOG.error("could not create TempFile '{}.csv'", fileName);
        }
    }

    public boolean deleteDownloadedFile() {
        return file != null && file.delete();
    }
}