package org.n52.sos.importer.test;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;

public class SandBox {
public static void main(String[] args) {
	String position = "52.3° n.Br. 7.3° ö.L.";
	MessageFormat mf = new MessageFormat("{0} n.Br. {2} ö.L.");
	Object[] o = null;
	try {
		o = mf.parse(position);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println(Arrays.toString(o));
}
}
