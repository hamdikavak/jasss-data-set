package com.hamdikavak.data.retrieval.jasss;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import com.thoughtworks.xstream.XStream;

public class JASSSDataExporter {

	public static void exportArticle(JASSSArticle myArticle, String location) {
		
		XStream xstream = new XStream();
		xstream.alias("article", JASSSArticle.class);
		
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(location );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OutputStreamWriter writer = new OutputStreamWriter(outputStream, Charset.forName("UTF-8"));
		xstream.toXML(myArticle, writer);
	}
}
