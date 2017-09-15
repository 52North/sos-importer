package org.n52.sos.importer.feeder.util;

import java.io.File;

import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.DataFile;
import org.n52.sos.importer.feeder.FeedingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WebClient {

    protected static final Logger LOG = LoggerFactory.getLogger(WebClient.class);

    protected Configuration config;
    
    public WebClient(Configuration config) {
        this.config = config;
    }
    
    public abstract DataFile download();
    
    public abstract boolean deleteDownloadedFile();
}