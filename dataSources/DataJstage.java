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

public class DataJstage {
	
	public DataJstage(){
	}
	
	// import the journals with issn in Jstage csv list into a json file
	public void csvtojson(String csvfile, String jsonfile){
		try {
			String line[];
			String ligne;
			int nb_journal = 0;
			int nb_lang = 0;
			ObjectMapper mapper = new ObjectMapper();
			FileWriter writer = new FileWriter(jsonfile, false);
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(csvfile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, '\t', '"', 2);
			while ((line = read_csv.readNext()) != null) {
				Set<Issn> issns = new HashSet<Issn>();
				if (!line[3].trim().equals("")){
					issns.add(new Issn("ISSN-print", line[3].trim()));
				}
				if (!line[4].trim().equals("")){
					issns.add(new Issn("ISSN-electronic", line[4].trim()));
				}
				if (issns.size()>0){
					nb_journal++;
					Journal journal = new Journal();
					Set<String> source = new HashSet<String>();
					source.add("Jstage");
					journal.setSource(source);
					
					journal.setIssns(issns);
					
					Set<String> titles = new HashSet<String>();
					titles.add(line[1].trim());
					if (titles.size()>0){
						journal.setTitles(titles);
					}
					
					String[] textlang = line[2].replace("Other", "").replace("English / Japanese etc.", "").trim().split(" and ", -1);
					Set<String> textlanguages = new HashSet<String>(Arrays.asList(textlang));
					textlanguages.remove("");
					if (textlanguages.size()>0){
						nb_lang++;
						journal.setTextLanguages(textlanguages);
					}
					
					ligne  = mapper.writeValueAsString(journal);
					writer.append(ligne + "\n");
					writer.flush();
				}
			}
			read_csv.close();
			in_csv.close();
			writer.close();
			System.out.println("nb journals with Issn in Jstage: " + nb_journal);
			System.out.println("nb journals with language: " + nb_lang);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
