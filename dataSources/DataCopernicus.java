package com.onescience.journal.dataSources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onescience.journal.schema_journal.Issn;
import com.onescience.journal.schema_journal.Journal;
import com.opencsv.CSVReader;

public class DataCopernicus {
	
	public DataCopernicus(){
	}
	
	// import the journals in Copernicus csv list into a json file
	public void csvtojson(String csvfile, String jsonfile){
		try {
			String line[];
			String ligne;
			int nb_journal = 0;
			ObjectMapper mapper = new ObjectMapper();
			FileWriter writer = new FileWriter(jsonfile, false);
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(csvfile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, ';', '"', 1);
			while ((line = read_csv.readNext()) != null) {
				nb_journal++;
				Journal journal = new Journal();
				Set<String> source = new HashSet<String>();
				source.add("Copernicus");
				journal.setSource(source);
				
				Set<Issn> issns = new HashSet<Issn>();
				if (!line[8].equals("")){
					issns.add(new Issn("ISSN-electronic", line[8]));
				}
				if (!line[39].equals("")){
					issns.add(new Issn("ISSN-print", line[39]));
				}
				if (issns.size()>0){
					journal.setIssns(issns);
				}
				
				Set<String> titles = new HashSet<String>();
				int[] colttl = {1, 3, 42};
				for (int i=0; i<colttl.length; i++){
					int col = colttl[i];
					if (!line[col].equals("")){
						if (line[col].length()>5){
							titles.add(line[col]);
						}
					}
				}
				if (titles.size()>0){
					journal.setTitles(titles);
				}
				
				String[] textlang = line[16].trim().replace("Other", "").replace("|", "#").split("#", -1);
				Set<String> textlanguages = new HashSet<String>(Arrays.asList(textlang));
				textlanguages.remove("");
				if (textlanguages.size()>0){
					journal.setTextLanguages(textlanguages);
				}
				
				String[] abslang = line[17].trim().replace("|", "#").split("#", -1);
				Set<String> abslanguages = new HashSet<String>(Arrays.asList(abslang));
				abslanguages.remove("");
				if (abslanguages.size()>0){
					journal.setAbstractLanguages(abslanguages);
				}
				
				ligne  = mapper.writeValueAsString(journal);
				writer.append(ligne + "\n");
			}
			read_csv.close();
			in_csv.close();
			writer.flush();
			writer.close();
			System.out.println("nb journal in Copernicus: " + nb_journal);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
