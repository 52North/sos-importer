package org.n52.sos.importer.feeder.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.DataFile;

public class FtpClient extends WebClient {

    private static final String PROXY_PORT = "proxyPort";
    private File file = null;

    public FtpClient(Configuration config) {
        super(config);
    }

    @Override
    public DataFile download() {
        // ftp client
        FTPClient client;
        
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
            client = new FTPClient();
        }

        createTempFile();
        
        if (file == null) {
            return null;
        }
        
        try (FileOutputStream fos = new FileOutputStream(file);) {
            client.connect(config.getRemoteFileURL());
            if (config.areRemoteFileCredentialsSet()) {
                client.login(config.getUser(), config.getPassword());
            }
            client.enterLocalPassiveMode();
            URL remoteFileURL = new URL(config.getRemoteFileURL());
            client.retrieveFile(remoteFileURL.getFile(), fos);
            if (config.areRemoteFileCredentialsSet() && !client.logout()) {
                LOG.info("FTP: cannot logout!");
            }
            fos.flush();
        } catch (final IOException e) {
            LOG.error("The file you specified cannot be obtained.");
            return null;
        } 
        return new DataFile(config, file);
    }
}
