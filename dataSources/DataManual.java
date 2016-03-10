package com.onescience.journal.dataSources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultimap;
import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Issn;
import com.onescience.journal.schema_journal.Journal;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class DataManual {
	
	public DataManual(){
	}
	
	
	// translate the manual language reference from csv file to json file
	public void reflangtojson(String csvfile, String jsonfile){
		try {
			String[] line;
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
				source.add("Manual");
				journal.setSource(source);
				
				Set<Issn> issns = new HashSet<Issn>();
				issns.add(new Issn("ISSN", line[0]));
				journal.setIssns(issns);
				
				Set<String> titles = new HashSet<String>();
				String[] ttls = line[1].split("#", -1);
				for (int i=0; i<ttls.length; i++){
					titles.add(ttls[i].trim());
				}
				if (titles.size()>0){
					journal.setTitles(titles);
				}
				
				String[] textlang = line[2].trim().split("#", -1);
				Set<String> textlanguages = new HashSet<String>(Arrays.asList(textlang));
				textlanguages.remove("");
				if (textlanguages.size()>0){
					journal.setTextLanguages(textlanguages);
				}
				
				ligne  = mapper.writeValueAsString(journal);
				writer.append(ligne + "\n");
				writer.flush();
			}
			read_csv.close();
			in_csv.close();
			
			writer.close();
			System.out.println("nb journals: " + nb_journal);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// translate the manual new PR journal list from csv file to json file
	public void newjourtojson(List<String> csvfilelist, String jsonfile){
		try {
			String[] line;
			String ligne;
			int nb_journal = 0;
			ObjectMapper mapper = new ObjectMapper();
			FileWriter writer = new FileWriter(jsonfile, false);
			for (int i=0; i<csvfilelist.size(); i++){
				String filename = csvfilelist.get(i);
				InputStreamReader in_csv = new InputStreamReader(new FileInputStream(filename), "UTF-8");
				CSVReader read_csv = new CSVReader(in_csv, ',', '"', 0);
				while ((line = read_csv.readNext()) != null) {
					nb_journal++;
					Journal journal = new Journal();
					Set<String> source = new HashSet<String>();
					source.add("Manual");
					journal.setSource(source);
					
					Set<Issn> issns = new HashSet<Issn>();
					issns.add(new Issn("ISSN", line[0]));
					journal.setIssns(issns);
					
					Set<String> titles = new HashSet<String>();
					String[] ttls = line[1].trim().split("#", -1);
					for (int j=0; j<ttls.length; j++){
						titles.add(ttls[j].trim());
					}
					titles.remove("");
					journal.setTitles(titles);
					
					ligne  = mapper.writeValueAsString(journal);
					writer.append(ligne + "\n");
					writer.flush();
				}
				read_csv.close();
				in_csv.close();
			}
			writer.close();
			System.out.println("nb journals: " + nb_journal);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// deduplicate scielo journal csv list
	public void dedupscielocsv(String scielofile, String dedupfile){
		try {
			String[] line;
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(scielofile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, ',', '"', 0);
			HashMultimap<String, String> issn_title = HashMultimap.create();
			int nb_line = 0;
			while ((line = read_csv.readNext()) != null) {
				nb_line++;
				issn_title.put(line[0].trim(), line[1].trim());
			}
			read_csv.close();
			in_csv.close();
			
			System.out.println("nb journals before:" + nb_line);
			System.out.println("nb journals after: " + issn_title.size());
			
			OutputStreamWriter out_csv = new OutputStreamWriter(new FileOutputStream(dedupfile), "UTF-8");
            CSVWriter write_csv = new CSVWriter(out_csv, ',', '"');
            for (String issn : issn_title.keySet()){
            	Set<String> titles = issn_title.get(issn);
            	for (String title : titles){
            		String[] newline = new String[2];
                	newline[0] = issn;
                	newline[1] = title;
                	write_csv.writeNext(newline);
                	write_csv.flush();
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
	
	// translate the manual same-journal list from csv file to json file
	public void samejourtojson(String manualfile, String jsonfile, String fauxtitle){
		try {
			String ligne;
			int nb_jour = 0;
			BufferedReader reader0 = new BufferedReader(new FileReader(new File(manualfile)));
			while ((ligne = reader0.readLine()) != null) {
				if(ligne.startsWith("issn: ")){
					nb_jour++;
				}
			}
			reader0.close();
			System.out.println("nb issns : " + nb_jour);
			
			List<Journal> jnls = new ArrayList<Journal>();
			for (int i=0; i<nb_jour; i++){
				Journal journal = new Journal();
				jnls.add(journal);
			}
			
			String issn = "";
			Set<Issn> issns = new HashSet<Issn>();
			Set<String> ttls = new HashSet<String>();
			FileWriter writer = new FileWriter(fauxtitle, false);
			BufferedReader reader = new BufferedReader(new FileReader(new File(manualfile)));
			nb_jour = 0;
			int new_issn = 0;
			HashMap<Integer, Issn> nbjour_issn = new HashMap<Integer, Issn>();
			while ((ligne = reader.readLine()) != null) {
				if(ligne.startsWith("issn: ")){
					if (nb_jour>0){
						Set<Issn> is = new HashSet<Issn>();
						for (Issn element : issns){
							is.add(element);
						}
						ttls.remove("");
						Set<String> ts = new HashSet<String>();
						for (String title : ttls){
							ts.add(title);
						}
						jnls.get(nb_jour-1).setIssns(is);
						jnls.get(nb_jour-1).setTitles(ts);
						issns.clear();
						ttls.clear();
					}
					issn = ligne.substring(6).trim();
					issns.add(new Issn("ISSN", issn));
					nb_jour++;
				} else {
					if (ligne.startsWith("+")){
						if ((ligne.startsWith("+eissn: "))||(ligne.startsWith("+issn: "))){
							if (ligne.startsWith("+eissn: ")){
								//issns.add(new Issn("ISSN-electronic", ligne.substring(8).trim()));
								new_issn++;
								nbjour_issn.put(nb_jour, new Issn("ISSN-electronic", ligne.substring(8).trim()));
							} else {
								//issns.add(new Issn("ISSN", ligne.substring(8).trim()));
								new_issn++;
								nbjour_issn.put(nb_jour, new Issn("ISSN", ligne.substring(8).trim()));
							}
						} else {
							ttls.addAll(Arrays.asList(ligne.substring(1).split("#", -1)));
						}
					} else {
						if (ligne.startsWith("-")){
							writer.append(issn + "$" + ligne + "\n");
							writer.flush();
						} else {
							ttls.addAll(Arrays.asList(ligne.split("#", -1)));
						}
					}
				}
			}
			
			Set<Issn> is = new HashSet<Issn>();
			for (Issn element : issns){
				is.add(element);
			}
			Set<String> ts = new HashSet<String>();
			for (String title : ttls){
				ts.add(title);
			}
			jnls.get(nb_jour-1).setIssns(is);
			jnls.get(nb_jour-1).setTitles(ts);
			reader.close();
			writer.flush();
			writer.close();
			System.out.println("nb new issns : " + new_issn);
			
			for (int nb : nbjour_issn.keySet()){
				Journal journal = new Journal();
				Set<Issn> issns2 = new HashSet<Issn>();
				issns2.add(nbjour_issn.get(nb));
				journal.setIssns(issns2);
				journal.setTitles(jnls.get(nb-1).getTitles());
				jnls.add(journal);
			}
			new Jsonfile().writefile(jsonfile, jnls);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
