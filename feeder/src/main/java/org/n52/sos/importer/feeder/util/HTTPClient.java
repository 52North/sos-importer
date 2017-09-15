package org.n52.sos.importer.feeder.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.DataFile;

public class HTTPClient extends WebClient {

    private DataFile dataFile = null;
    private File file = null;
    
    public HTTPClient(Configuration config) {
        super(config);
    }

    @Override
    public DataFile download() {
        // HttpClient
        CloseableHttpClient client = HttpClientBuilder.create().build();
        
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
        
        try {
            CloseableHttpResponse response;
            response = client.execute(
                    new HttpGet(config.getRemoteFileURL()));
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                FileOutputStream fos = new FileOutputStream(file);
                entity.writeTo(fos);
            }
        } catch (ClientProtocolException e) {
            LOG.error("A HTTP Protocol error occured '{}'", e);
            e.printStackTrace();
        } catch (IOException e) {
            LOG.error("A IO error occured '{}'", e);
            e.printStackTrace();
        }
        dataFile = new DataFile(config, file);
        return dataFile;
    }

    @Override
    public boolean deleteDownloadedFile()
    {
        // TODO Auto-generated method stub
        return false;
    }

}
