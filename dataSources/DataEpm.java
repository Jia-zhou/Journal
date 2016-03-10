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

public class DataEpm {
	
	public DataEpm(){
	}
	
	// import the journals in Epm csv list into a json file
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
				source.add("EPM");
				journal.setSource(source);
				
				Set<Issn> issns = new HashSet<Issn>();
				if (!line[0].equals("")){
					issns.add(new Issn("ISSN", line[0]));
				}
				if (!line[1].equals("")){
					issns.add(new Issn("ISSN-electronic", line[1]));
				}
				if (issns.size()>0){
					journal.setIssns(issns);
				}
				
				Set<String> titles = new HashSet<String>();
				String title = line[4].trim();
				if (!title.equals("")){
					titles.add(title);
				}
				if (titles.size()>0){
					journal.setTitles(titles);
				}
				
				String textlang = line[3];
				Set<String> textlanguages = new HashSet<String>(Arrays.asList(textlang));
				if (!textlang.equals("")){
					textlanguages.add(textlang);
				}

				if (textlanguages.size()>0){
					journal.setTextLanguages(textlanguages);
				}
				ligne  = mapper.writeValueAsString(journal);
				writer.append(ligne + "\n");
			}
			read_csv.close();
			in_csv.close();
			writer.flush();
			writer.close();
			System.out.println("nb journal in EPM: " + nb_journal);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
