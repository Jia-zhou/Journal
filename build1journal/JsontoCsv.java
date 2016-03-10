package com.onescience.journal.build1journal;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Set;

import com.onescience.journal.methods.Journal_methods;
import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Journal;
import com.opencsv.CSVWriter;

public class JsontoCsv {
	
	public JsontoCsv(){
	}
	
	// extract some journal fields (Issn, title, subfield, language) to a csv file
	public void csvProduction(String jsonfile, String csvfile){
		try{
			List<Journal> jnls = new Jsonfile().readfile(jsonfile);
			OutputStreamWriter out_csv = new OutputStreamWriter(new FileOutputStream(csvfile), "UTF-8");
            CSVWriter write_csv = new CSVWriter(out_csv, '$', CSVWriter.NO_QUOTE_CHARACTER);
            for (int i=0; i<jnls.size(); i++){
            	Set<String> issns = new Journal_methods().getIssnValues(jnls.get(i));
            	Set<String> titles = jnls.get(i).getTitles();
            	titles.remove("");
            	for (String issn : issns){
            		for (String title : titles){
            			String[] newline = new String[5];
            			newline[0] = Integer.toString(i+1);
            			newline[1] = issn;
            			newline[2] = title;
            			newline[3] = String.join("#", jnls.get(i).getSubfields().toArray(new String[0]));
            			newline[4] = String.join("#", jnls.get(i).getTextLanguages().toArray(new String[0]));
            			write_csv.writeNext(newline);
    	                write_csv.flush();
            		}
            	}
            }
            write_csv.close();
			out_csv.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
