package com.onescience.journal.clean1journal;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.onescience.journal.methods.Journal_methods;
import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Issn;
import com.onescience.journal.schema_journal.Journal;
import com.opencsv.CSVWriter;

public class JsontoCsv0 {

	public static void main(String[] args) {
		
		JsontoCsv0 ie = new JsontoCsv0();
		//ie.tocsv();
		

	}
	
	public void tocsv(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String jsonfile = prefix + "1journal_language5.json";
		String csvfile = prefix + "journal_language.csv";
		
		try {
			
			List<Journal> jnls = new Jsonfile().readfile(jsonfile);
			int id = 0;
			int nb_1journal = 0;
			OutputStreamWriter out_csv = new OutputStreamWriter(new FileOutputStream(csvfile), "UTF-8");
            CSVWriter write_csv = new CSVWriter(out_csv, ';', '"');
            String[] headline = {"id", "id1journal", "issn", "p_issn", "e_issn", "titles", "subfields", "textLanguages", "abstractLanguages"};
            write_csv.writeNext(headline);
            write_csv.flush();
            
            for (int i=0; i<jnls.size(); i++){
            	id++;
            	Journal journal = jnls.get(i);
            	Set<String> id1journals = new Journal_methods().getIdValue(journal, "1journal");
            	
            	for (String id1journal : id1journals){
            		nb_1journal++;
            		String[] newline = new String[9];
            		newline[0] = String.valueOf(id);
            		newline[1] = id1journal;
            		newline[2] = String.join("#", new Journal_methods().getIssnValues(journal, "ISSN").toArray(new String[0]));
            		newline[3] = String.join("#", new Journal_methods().getIssnValues(journal, "ISSN-print").toArray(new String[0]));
            		newline[4] = String.join("#", new Journal_methods().getIssnValues(journal, "ISSN-electronic").toArray(new String[0]));
            		newline[5] = String.join("#", journal.getTitles().toArray(new String[0]));
            		newline[6] = String.join("#", journal.getSubfields().toArray(new String[0]));
            		newline[7] = String.join("#", journal.getTextLanguages().toArray(new String[0]));
            		newline[8] = String.join("#", journal.getAbstractLanguages().toArray(new String[0]));
                	write_csv.writeNext(newline);
                    write_csv.flush();
            	}
            }
            write_csv.close();
			out_csv.close();
			
			System.out.println("nb journals in json: " + id);
			System.out.println("nb journals in 1journal: " + nb_1journal);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
