package org.n52.sos.importer.feeder.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPHTTPClient;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.DataFile;

public class FTPClient extends WebClient {

    private static final String PROXY_PORT = "proxyPort";
    private DataFile dataFile = null;
    private File file = null;

    public FTPClient(Configuration config) {
        super(config);
    }

    @Override
    public DataFile download() {
        // ftp client
        org.apache.commons.net.ftp.FTPClient client;
        
        // proxy
        final String pHost = System.getProperty("proxyHost", "proxy");
        int pPort = -1;
        if (System.getProperty(PROXY_PORT) != null) {
            pPort = Integer.parseInt(System.getProperty(PROXY_PORT));
        }
        final String pUser = System.getProperty("http.proxyUser");
        final String pPassword = System.getProperty("http.proxyPassword");
        if (pHost != null && pPort != -1) {
            LOG.info("Using proxy for FTP connection!");
            if (pUser != null && pPassword != null) {
                client = new FTPHTTPClient(pHost, pPort, pUser, pPassword);
            } else {
                client = new FTPHTTPClient(pHost, pPort);
            }
        } else {
            LOG.info("Using no proxy for FTP connection!");
            client = new org.apache.commons.net.ftp.FTPClient();
        }

        // get first file
        final String fileName = config.getFileName();
        try {
            file = File.createTempFile(fileName, ".csv");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        // if back button was used: delete old file
        if (file != null && file.exists()) {
            if (!file.delete()) {
                LOG.error("Could not delete file '{}'", file.getAbsolutePath());
            }
        }

        FileOutputStream fos = null;
        
        try {
            client.connect(config.getRemoteFileURL());
            client.login(config.getUser(), config.getPassword());
            client.enterLocalPassiveMode();
            fos = new FileOutputStream(file);
            client.retrieveFile(fileName, fos);
            final boolean logout = client.logout();
            if (!logout) {
                LOG.info("FTP: cannot logout!");
            }
            fos.flush();
        } catch (final IOException e) {
            LOG.error("The file you specified cannot be obtained.");
            return null;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LOG.error(""+e);
                }
            }
        }
        dataFile = new DataFile(config, file);
        return dataFile;
    }

    @Override
    public boolean deleteDownloadedFile()
    {
        if (file != null) {
            file.delete();
            return true;
        }
        return false;
    }

}
