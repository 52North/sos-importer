package org.n52.sos.importer.feeder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

import org.apache.xmlbeans.XmlException;
import org.junit.Ignore;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ExceptionReport;

public class TestFeatureOmParameter {
    
    @Test
    @Ignore("requires running SOS service instance")
    public void integrationTestForOmParameter() throws IllegalArgumentException, MalformedURLException, IOException,
            OXFException, XmlException, ParseException, ExceptionReport {
        Configuration configuration = new Configuration("src/test/resources/feature_om-parameter/configuration.xml");
        DataFile dataFile = new DataFile(configuration, new File("src/test/resources/feature_om-parameter/data.csv"));
        new SensorObservationService(configuration).importData(dataFile);
    }
    
}
