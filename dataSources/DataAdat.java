package com.onescience.journal.dataSources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Issn;
import com.onescience.journal.schema_journal.Journal;
import com.opencsv.CSVReader;

public class DataAdat {
	
	public DataAdat(){	
	}
	
	// import the journals with language in Adat csv list into a json file
	public void csvtojson(String csvfile, String jsonfile){
		try {
			String line[];
			List<Journal> jnls = new ArrayList<Journal>();
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(csvfile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, ';', '"', 1);
			while ((line = read_csv.readNext()) != null) {
				String issn = line[2].trim();
				String title = line[1].trim();
				String clang = line[3].replace("Greek, Modern (1453-)", "Greek").replace("Celtic (Other)", "Celtic").replace("Occitan (post 1500)", "Occitan")
						.replace("Slavic (Other)", "Slavic").replace("Multiple languages", "Multi-languages").replace("Northern Frisian", "Frisian");
				String lang = clang.replaceAll("\\s+", " ").trim();
				String[] langs = lang.split(" ", -1);
				
				Set<String> languages = new HashSet<String>();
				for (int i=0; i<langs.length; i++){
					languages.add(langs[i]);
				}
				languages.remove("");
				
				if (languages.size()>0){
					Journal journal = new Journal();
					Set<String> source = new HashSet<String>();
					source.add("Adat");
					journal.setSource(source);
					
					Set<String> titles = new HashSet<String>();
					titles.add(title);
					journal.setTitles(titles);
					
					Set<Issn> issns = new HashSet<Issn>();
					if (!issn.equals("")){
						issns.add(new Issn("ISSN", issn));
					}
					if (issns.size()>0){
						journal.setIssns(issns);
					}
					
					journal.setTextLanguages(languages);
					jnls.add(journal);
				}
			}
			read_csv.close();
			in_csv.close();
			new Jsonfile().writefile(jsonfile, jnls);
			System.out.println("nb journals with languages in Adat: " + jnls.size());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
