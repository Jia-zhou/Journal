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

public class DataCaj {
	
	public DataCaj(){
	}
	
	// import the journals in Caj csv list into a json file
	public void csvtojson(String csvfile, String jsonfile){
		try {
			String line[];
			String ligne;
			int nb_line = 0;
			int nb_journal = 0;
			ObjectMapper mapper = new ObjectMapper();
			FileWriter writer = new FileWriter(jsonfile, false);
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(csvfile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, ';', '"', 1);
			while ((line = read_csv.readNext()) != null) {
				nb_line++;
				//if (line[22].equals("是")){
					nb_journal++;
					Journal journal = new Journal();
					Set<String> source = new HashSet<String>();
					source.add("Caj");
					journal.setSource(source);
					
					Set<Issn> issns = new HashSet<Issn>();
					if (!line[7].equals("")){
						issns.add(new Issn("ISSN", line[7]));
					}
					if (issns.size()>0){
						journal.setIssns(issns);
					}
					
					Set<String> titles = new HashSet<String>();
					int[] colttl = {1, 2, 3, 4};
					for (int i=0; i<colttl.length; i++){
						int col = colttl[i];
						if (!line[col].equals("")){
							String[] ttls = line[col].split(";", -1);
							for (int j=0; j<ttls.length; j++){
								if (!ttls[j].equals("")){
									titles.add(ttls[j]);
								}
							}
						}
					}
					if (titles.size()>0){
						journal.setTitles(titles);
					}
					
					String langchain = line[9].replace("中文", "Chinese").replace("英文", "English").replace("德文", "German").replace("日文", "Japanese");
					langchain = langchain.replace("5中", "Chinese").replace("70中", "Chinese");
					String[] langs = langchain.split(";");
					Set<String> languages = new HashSet<String>(Arrays.asList(langs));
					languages.remove("");
					if (languages.size()>0){
						journal.setTextLanguages(languages);
					}
					
					ligne  = mapper.writeValueAsString(journal);
					writer.append(ligne + "\n");
				//}
				
			}
			read_csv.close();
			in_csv.close();
			writer.flush();
			writer.close();
			
			System.out.println("nb journal in Caj: " + nb_line);
			System.out.println("nb journal acamedic in Caj: " + nb_journal);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
