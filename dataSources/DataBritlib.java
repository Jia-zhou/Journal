package com.onescience.journal.dataSources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Issn;
import com.onescience.journal.schema_journal.Journal;
import com.opencsv.CSVReader;

public class DataBritlib {
	
	public DataBritlib(){
	}
	
	// import the journals in British Library csv list into a json file
	public void csvtojson(String csvfile, String jsonfile){
		try {
			String line[];
			String ligne;
			int nb_journal = 0;
			ObjectMapper mapper = new ObjectMapper();
			FileWriter writer = new FileWriter(jsonfile, false);
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(csvfile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, ';', '"', 0);
			while ((line = read_csv.readNext()) != null) {
				nb_journal++;
				Journal journal = new Journal();
				Set<String> source = new HashSet<String>();
				source.add("British Library");
				journal.setSource(source);
				
				Set<Issn> issns = new HashSet<Issn>();
				String[] issn = line[2].trim().split("#", -1);
				for (int i=0; i<issn.length; i++){
					issns.add(new Issn("ISSN", issn[i]));
				}
				if (issns.size()>0){
					journal.setIssns(issns);
				}
				
				Set<String> titles = new HashSet<String>();
				String[] title = line[1].trim().split("#", -1);
				for (int i=0; i<title.length; i++){
					if (!title[i].equals("")){
						titles.add(title[i]);
					}
				}
				if (titles.size()>0){
					journal.setTitles(titles);
				}
				
				String[] textlang = line[3].trim().split("#", -1);
				Set<String> textlanguages = new HashSet<String>(Arrays.asList(textlang));
				textlanguages.remove("");
				if (textlanguages.size()>0){
					journal.setTextLanguages(textlanguages);
				}
				
				ligne = mapper.writeValueAsString(journal);
				writer.append(ligne + "\n");
			}
			read_csv.close();
			in_csv.close();
			writer.flush();
			writer.close();
			System.out.println("nb journal in British Library: " + nb_journal);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// separate the language string into set<String> in json file
	public void separatelangs(String jsonfile, String modifiedfile){
		try {
			List<Journal> jnls = new Jsonfile().readfile(jsonfile);
			for (int i=0; i<jnls.size(); i++){
				Set<String> lang = jnls.get(i).getTextLanguages();
				Set<String> langs = new HashSet<String>();
				for (String chain: lang){
					String c = chain;
					int l = c.length();
					if (l>3){
						while (c.length()>2){
							langs.add(c.substring(0,3));
							c = c.substring(3);
						}
						if (!c.equals("")){
							System.out.println("exception:");
							System.out.println(chain);
						}
					} else {
						langs.add(c);
					}
				}
				jnls.get(i).setTextLanguages(langs);
			}
			new Jsonfile().writefile(modifiedfile, jnls);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
