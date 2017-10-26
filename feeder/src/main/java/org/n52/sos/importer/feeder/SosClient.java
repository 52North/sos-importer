package org.n52.sos.importer.feeder;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.n52.sos.importer.feeder.model.RegisterSensor;
import org.n52.sos.importer.feeder.model.TimeSeries;

public interface SosClient {
    
    boolean isInstanceAvailable();
    
    boolean isInstanceTransactional();
    
    boolean isSensorRegistered(String sensorURI);
    
    String registerSensor(RegisterSensor rs) throws OXFException, XmlException, IOException;
    
    String insertObservation(InsertObservation io) throws IOException;
    
    String insertSweArrayObservation(TimeSeries timeSeries) throws IOException;
    
}