package org.n52.sos.importer.feeder.util;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.DataFile;

public class HTTPClient extends WebClient {
    
    public HTTPClient(Configuration config) {
        super(config);
    }

    @Override
    public DataFile download() {
        // TODO: Add support of proxyconfigurations
        
        // HttpClient
        CloseableHttpClient client = HttpClients.createMinimal();
        
        createTempFile();
        
        if (file == null) {
            return null;
        }
        
        try (FileOutputStream fos = new FileOutputStream(file);) {
            CloseableHttpResponse response = client.execute(new HttpGet(config.getRemoteFileURL()));
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                entity.writeTo(fos);
            }
        } catch (ClientProtocolException e) {
            LOG.error("A HTTP Protocol error occured '{}'", e);
            return null;
        } catch (IOException e) {
            LOG.error("A IO error occured '{}'", e);
            return null;
        }
        return new DataFile(config, file);
    }
}
