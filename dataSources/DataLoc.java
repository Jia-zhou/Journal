package com.onescience.journal.dataSources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Journal;

public class DataLoc {
	
	public DataLoc(){
	}
	
	// merge several json files in a new single json file
	public void mergejsonfiles(List<String> filelist, String jsonfile){
		try {
			String ligne;
			FileWriter writer = new FileWriter(jsonfile, false);
			for (int i=0; i<filelist.size(); i++){
				BufferedReader reader = new BufferedReader(new FileReader(new File(filelist.get(i))));
				while ((ligne = reader.readLine()) != null) {
					ligne = ligne.replace("\"languages\":", "\"textLanguages\":");
					writer.append(ligne.replace("\"source\": \"Loc\"", "\"source\": [\"Loc\"]") + "\n");
					writer.flush();
				}
				reader.close();
			}
			writer.close();
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
