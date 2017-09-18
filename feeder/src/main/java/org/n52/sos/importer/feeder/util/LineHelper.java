package org.n52.sos.importer.feeder.util;

public class LineHelper {

    public static void trimValues(String[] values) {
        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i++) {
                if (values[i] != null && !values[i].isEmpty()) {
                    values[i] = values[i].trim();
                }
            }
        }
    }

}
