package org.n52.sos.importer.feeder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.n52.sos.importer.feeder.csv.CsvParser;
import org.n52.sos.importer.feeder.csv.WrappedCSVParser;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CollectorSkeleton implements Collector {

    private static final Logger LOG = LoggerFactory.getLogger(CollectorSkeleton.class);

    protected Configuration configuration;

    protected DataFile dataFile;

    protected FeedingContext context;

    protected Timestamp newLastUsedTimestamp;

    protected int lineCounter;

    protected CsvParser parser;

    protected boolean stopped;

    public CollectorSkeleton() {
        super();
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setFeedingContext(FeedingContext context) {
        this.context = context;
    }

    @Override
    public void stopCollecting() {
        stopped = true;
    }

    protected void logExceptionThrownDuringParsing(Exception exception) {
        LOG.error("Could not retrieve all information required for insert observation because of parsing error:"
                + " {}: {}. Skipped this one.",
                exception.getClass().getName(),
                exception.getMessage());
        LOG.debug("Exception stack trace:", exception);
    }

    protected boolean isHeaderLine(String[] storedHeaderline, String[] lineToCheck) {
        boolean isHeaderLine = Arrays.equals(storedHeaderline, lineToCheck);
        if (isHeaderLine) {
            LOG.debug("Headerline found: '{}'", Arrays.toString(lineToCheck));
        }
        return isHeaderLine;
    }

    protected void skipLines(int skipCount) throws IOException {
        // get the number of lines to skip (coming from already read lines)
        String[] values;
        int skipLimit = parser.getSkipLimit();
        int linesToSkip = skipCount;
        while (linesToSkip > skipLimit) {
            values = parser.readNext();
            LOG.trace(String.format("\t\tSkip CSV line #%d: %s", lineCounter + 1, configuration.restoreLine(values)));
            linesToSkip--;
            lineCounter++;
        }
    }

    /**
     * Returns a CSVReader instance for the current DataFile using the configuration
     * including the defined values for: first line with data, separator, escape, and text qualifier.
     *
     * @return a <code>CSVReader</code> instance
     *
     * @throws java.io.IOException if any.
     */
    protected CsvParser getCSVReader(DataFile dataFile) throws IOException {
        LOG.trace("getCSVReader()");
        parser = null;
        if (configuration.isCsvParserDefined()) {
            String csvParser = configuration.getCsvParser();
            try {
                Class<?> clazz = Class.forName(csvParser);
                Constructor<?> constructor = clazz.getConstructor((Class<?>[]) null);
                Object instance = constructor.newInstance();
                if (CsvParser.class.isAssignableFrom(instance.getClass())) {
                    parser = (CsvParser) instance;
                }
            } catch (ClassNotFoundException |
                    NoSuchMethodException |
                    SecurityException |
                    InstantiationException |
                    IllegalAccessException |
                    IllegalArgumentException |
                    InvocationTargetException e) {
                String errorMsg = String.format("Could not load defined CsvParser implementation class '%s'. "
                        + "Cancel import",
                        csvParser);
                LOG.error(errorMsg);
                LOG.debug("Exception thrown: {}", e.getMessage(), e);
                throw new IllegalArgumentException(errorMsg, e);
            }
        }
        if (parser == null) {
            parser = new WrappedCSVParser();
        }
        Reader fr = new InputStreamReader(
                new FileInputStream(dataFile.getCanonicalPath()), Configuration.DEFAULT_CHARSET);
        BufferedReader br = new BufferedReader(fr);
        parser.init(br, configuration);
        return parser;
    }

    protected String[] readHeaderLine(DataFile dataFile)
            throws UnsupportedEncodingException, FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(
                                dataFile.getCanonicalPath()),
                                dataFile.getEncoding()))) {
            int counter = 1;
            for (String line; (line = br.readLine()) != null;) {
                if (counter == dataFile.getHeaderLine()) {
                    return line.split(Character.toString(configuration.getCsvSeparator()));
                } else {
                    counter++;
                }
            }
        }
        return null;
    }

}