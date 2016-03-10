package com.onescience.journal.clean1journal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.Sets;
import com.onescience.journal.methods.Journal_methods;
import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Journal;
import com.onescience.utils.SetUtil;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class AddThesaurus {

	public static void main(String[] args) {
		
		AddThesaurus ie = new AddThesaurus();
		//ie.Medlinetitles();
		//ie.buildnormedtitle();
		//ie.compare1journal();
		ie.buildvariants();
		
		
	}
	
	public void Medlinetitles(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/Pubmed/";
		String datapath = prefix + "data";
		String ttlsfile = prefix + "medline_issn_title_occ.csv";
		
		try {
			String[] line;
			int nb_file = 0;
			Multiset<String> issn_ttls = HashMultiset.create();
			
			File f = new File(datapath);
	        File[] files = f.listFiles();
	        for (File file : files) {
	        	nb_file++;
	        	System.out.println("n° file: " + nb_file + " -- path : " + file.getAbsolutePath());
	        	
	        	InputStreamReader in_csv = new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF-8");
    			CSVReader read_csv = new CSVReader(in_csv, ';', '"', 0);
    			while ((line = read_csv.readNext()) != null) {
    				if (!line[0].equals("ISSN")){
    					String ligne = line[0] + "\t" + line[1] + "\t" + line[2] + "\t" + line[3] + "\t" + line[4];
        				issn_ttls.add(ligne);
    				}
    			}
    			read_csv.close();
    			in_csv.close();
	        }
	        
	        System.out.println("nb lines: " + issn_ttls.size());
	        System.out.println("nb lines differents: " + issn_ttls.elementSet().size());
	        
	        FileWriter writer = new FileWriter(ttlsfile, false);
	        for (String ligne : Multisets.copyHighestCountFirst(issn_ttls).elementSet()){
	        	writer.append(ligne + "\t" + issn_ttls.count(ligne) + "\n");
	        	writer.flush();
	        }
			writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void buildnormedtitle(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		String sm1file = prefix + "SM1/sm1_issn_title_occ.csv";
		String sm2file = prefix + "SM2/sm2_issn_title_occ.csv";
		String medlinefile = prefix + "Pubmed/medline_issn_title_occ.csv";
		String normtitle = prefix + "Thesaurus/issn_title_normed.csv";
		
		try {
			String[] line;
			HashMap<String, HashMap<String, Integer>> issn_ttls = new HashMap<String, HashMap<String, Integer>>();
			
			// read the titles in SM1
			InputStreamReader in_SM1 = new InputStreamReader(new FileInputStream(sm1file), "UTF-8");
			CSVReader read_SM1 = new CSVReader(in_SM1, '\t', '"', 1);
			while ((line = read_SM1.readNext()) != null) {
				List<String> issns = new ArrayList<String>();
				if ((!line[0].equals(""))&&(!line[0].equals("issn"))){
					issns.add(line[0]);
				}
				if ((!line[1].equals(""))&&(!line[1].equals("issn"))){
					issns.add(line[1]);
				}
				for (int i=0; i<issns.size(); i++){
					String issn = issns.get(i);
					int nb_occ = Integer.parseInt(line[7]);
					List<Integer> cols = new ArrayList<Integer>(Arrays.asList(2));
					HashMap<String, Integer> ttls = new HashMap<String, Integer>();
					for (int j=0; j<cols.size(); j++){
						ttls = new SetUtil().addStringNb(ttls, line[cols.get(j)], nb_occ);
					}
					if (!issn_ttls.containsKey(issn)){
						issn_ttls.put(issn, ttls);
					} else {
						HashMap<String, Integer> ttls0 = issn_ttls.get(issn);
						issn_ttls.remove(issn);
						issn_ttls.put(issn, new SetUtil().mergeStringNb(ttls0, ttls));
					}
				}
			}
			read_SM1.close();
			in_SM1.close();
			
			// read the titles in SM2
			InputStreamReader in_SM2 = new InputStreamReader(new FileInputStream(sm2file), "UTF-8");
			CSVReader read_SM2 = new CSVReader(in_SM2, '\t', '"', 1);
			while ((line = read_SM2.readNext()) != null) {
				String issn = line[0];
				if (!issn.equals("")){
					int nb_occ = Integer.parseInt(line[3]);
					List<Integer> cols = new ArrayList<Integer>(Arrays.asList(1));
					HashMap<String, Integer> ttls = new HashMap<String, Integer>();
					for (int i=0; i<cols.size(); i++){
						ttls = new SetUtil().addStringNb(ttls, line[cols.get(i)], nb_occ);
					}
					
					if (!issn_ttls.containsKey(issn)){
						issn_ttls.put(issn, ttls);
					} else {
						HashMap<String, Integer> ttls0 = issn_ttls.get(issn);
						issn_ttls.remove(issn);
						issn_ttls.put(issn, new SetUtil().mergeStringNb(ttls0, ttls));
					}
				}	
			}
			read_SM2.close();
			in_SM2.close();
			
			// read the titles in Medline
			InputStreamReader in_med = new InputStreamReader(new FileInputStream(medlinefile), "UTF-8");
			CSVReader read_med = new CSVReader(in_med, '\t', CSVWriter.NO_QUOTE_CHARACTER, 0);
			while ((line = read_med.readNext()) != null) {
				List<String> issns = new ArrayList<String>();
				if (!line[0].equals("")){
					issns.add(line[0]);
				}
				if (!line[1].equals("")){
					issns.add(line[1]);
				}
				for (int i=0; i<issns.size(); i++){
					String issn = issns.get(i);
					int nb_occ = Integer.parseInt(line[5]);
					List<Integer> cols = new ArrayList<Integer>(Arrays.asList(3));
					HashMap<String, Integer> ttls = new HashMap<String, Integer>();
					for (int j=0; j<cols.size(); j++){
						ttls = new SetUtil().addStringNb(ttls, line[cols.get(j)], nb_occ);
					}
					if (!issn_ttls.containsKey(issn)){
						issn_ttls.put(issn, ttls);
					} else {
						HashMap<String, Integer> ttls0 = issn_ttls.get(issn);
						issn_ttls.remove(issn);
						issn_ttls.put(issn, new SetUtil().mergeStringNb(ttls0, ttls));
					}
				}
			}
			read_med.close();
			in_med.close();
			
			
			// count number of titles by issn
			Multiset<Integer> nbttls = HashMultiset.create();
			for (String issn : issn_ttls.keySet()){
				nbttls.add(issn_ttls.get(issn).size());
				if (issn_ttls.get(issn).size()>20){
					System.out.println("issn: " + issn + " -- " + issn_ttls.get(issn).size());
				}
			}
			for (int nb : Multisets.copyHighestCountFirst(nbttls).elementSet()){
				System.out.println(nb + " titles by issn: " + nbttls.count(nb));
			}
			
			// choose the title with most occurrence as the normed title by issn
			HashMap<String, String> issn_title = new HashMap<String, String>();
			HashMap<String, Integer> issn_nb = new HashMap<String, Integer>();
			for (String issn : issn_ttls.keySet()){
				HashMap<String, Integer> ttls = new SetUtil().triAvecValeurInt(issn_ttls.get(issn));
				Map.Entry<String, Integer> ttl_nb = ttls.entrySet().iterator().next();
				issn_title.put(issn, ttl_nb.getKey());
				issn_nb.put(issn, ttl_nb.getValue());
			}
			issn_nb = new SetUtil().triAvecValeurInt(issn_nb);
			System.out.println("nb issns: " + issn_ttls.keySet().size() + " or " + issn_title.size());
			
			FileWriter writer = new FileWriter(normtitle, false);
	        for (String issn : issn_nb.keySet()){
	        	writer.append(issn + "\t" + issn_title.get(issn) + "\t"+ issn_nb.get(issn) + "\n");
	        	writer.flush();
	        }
			writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void compare1journal(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		String normtitle = prefix + "Thesaurus/issn_title_normed.csv";
		String jnlsfile = prefix + "1journal/newjournal_deduplicated.json";
		
		try{
			String[] line;
			
			Set<String> issn1 = new HashSet<String>();
			List<Journal> jnls = new Jsonfile().readfile(jnlsfile);
			for (int i=0; i<jnls.size(); i++){
				Set<String> issns = new Journal_methods().getIssnValues(jnls.get(i));
				for (String issn : issns){
					issn1.add(issn);
				}
			}
			
			Set<String> issn2 = new HashSet<String>();
			InputStreamReader in_title = new InputStreamReader(new FileInputStream(normtitle), "UTF-8");
			CSVReader read_title = new CSVReader(in_title, '\t', CSVWriter.NO_QUOTE_CHARACTER, 0);
			while ((line = read_title.readNext()) != null) {
				issn2.add(line[0]);
			}
			read_title.close();
			in_title.close();
			
			Set<String> same_issn = Sets.intersection(issn1, issn2);
			System.out.println("nb same issns: " + same_issn.size());
			System.out.println("nb new issns: " + Sets.difference(issn2, issn1).size());
			System.out.println("nb abs issns: " + Sets.difference(issn1, issn2).size());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void buildvariants(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		String sm1file = prefix + "SM1/sm1_issn_title_occ.csv";
		String sm2file = prefix + "SM2/sm2_issn_title_occ.csv";
		String medlinefile = prefix + "Pubmed/medline_issn_title_occ.csv";
		String variantfile = prefix + "Thesaurus/issn_variants.csv";
		
		try {
			String[] line;
			HashMap<String, HashMap<String, Integer>> issn_ttls = new HashMap<String, HashMap<String, Integer>>();
			
			// read the titles in SM1
			InputStreamReader in_SM1 = new InputStreamReader(new FileInputStream(sm1file), "UTF-8");
			CSVReader read_SM1 = new CSVReader(in_SM1, '\t', '"', 1);
			while ((line = read_SM1.readNext()) != null) {
				List<String> issns = new ArrayList<String>();
				if ((!line[0].equals(""))&&(!line[0].equals("issn"))){
					issns.add(line[0]);
				}
				if ((!line[1].equals(""))&&(!line[1].equals("issn"))){
					issns.add(line[1]);
				}
				for (int i=0; i<issns.size(); i++){
					String issn = issns.get(i);
					int nb_occ = Integer.parseInt(line[7]);
					List<Integer> cols = new ArrayList<Integer>(Arrays.asList(2, 3, 4, 5, 6));
					HashMap<String, Integer> ttls = new HashMap<String, Integer>();
					for (int j=0; j<cols.size(); j++){
						ttls = new SetUtil().addStringNb(ttls, line[cols.get(j)], nb_occ);
					}
					if (!issn_ttls.containsKey(issn)){
						issn_ttls.put(issn, ttls);
					} else {
						HashMap<String, Integer> ttls0 = issn_ttls.get(issn);
						issn_ttls.remove(issn);
						issn_ttls.put(issn, new SetUtil().mergeStringNb(ttls0, ttls));
					}
				}
			}
			read_SM1.close();
			in_SM1.close();
			
			// read the titles in SM2
			InputStreamReader in_SM2 = new InputStreamReader(new FileInputStream(sm2file), "UTF-8");
			CSVReader read_SM2 = new CSVReader(in_SM2, '\t', '"', 1);
			while ((line = read_SM2.readNext()) != null) {
				String issn = line[0];
				if (!issn.equals("")){
					int nb_occ = Integer.parseInt(line[3]);
					List<Integer> cols = new ArrayList<Integer>(Arrays.asList(1,2));
					HashMap<String, Integer> ttls = new HashMap<String, Integer>();
					for (int i=0; i<cols.size(); i++){
						ttls = new SetUtil().addStringNb(ttls, line[cols.get(i)], nb_occ);
					}
					
					if (!issn_ttls.containsKey(issn)){
						issn_ttls.put(issn, ttls);
					} else {
						HashMap<String, Integer> ttls0 = issn_ttls.get(issn);
						issn_ttls.remove(issn);
						issn_ttls.put(issn, new SetUtil().mergeStringNb(ttls0, ttls));
					}
				}	
			}
			read_SM2.close();
			in_SM2.close();
			
			// read the titles in Medline
			int nb_line = 0;
			InputStreamReader in_med = new InputStreamReader(new FileInputStream(medlinefile), "UTF-8");
			CSVReader read_med = new CSVReader(in_med, '\t', CSVWriter.NO_QUOTE_CHARACTER, 0);
			while ((line = read_med.readNext()) != null) {
				nb_line++;
				if (line.length!=6){
					System.out.println(nb_line + ": nb:" + line.length + " : issn: " + line[0]);
				}
				List<String> issns = new ArrayList<String>();
				if (!line[0].equals("")){
					issns.add(line[0]);
				}
				if (!line[1].equals("")){
					issns.add(line[1]);
				}
				for (int i=0; i<issns.size(); i++){
					String issn = issns.get(i);
					int nb_occ = Integer.parseInt(line[5]);
					List<Integer> cols = new ArrayList<Integer>(Arrays.asList(2, 3, 4));
					HashMap<String, Integer> ttls = new HashMap<String, Integer>();
					for (int j=0; j<cols.size(); j++){
						ttls = new SetUtil().addStringNb(ttls, line[cols.get(j)], nb_occ);
					}
					if (!issn_ttls.containsKey(issn)){
						issn_ttls.put(issn, ttls);
					} else {
						HashMap<String, Integer> ttls0 = issn_ttls.get(issn);
						issn_ttls.remove(issn);
						issn_ttls.put(issn, new SetUtil().mergeStringNb(ttls0, ttls));
					}
				}
			}
			read_med.close();
			in_med.close();
			
			System.out.println("nb lines: " + nb_line);
			System.out.println("nb issns: " + issn_ttls.keySet().size());
			
			
			int nblim = 10;
			double pctlim = 0.01;
			HashMap<String, Set<String>> variants = new HashMap<String, Set<String>>();
			FileWriter writer = new FileWriter(variantfile, false);
			for (String issn : issn_ttls.keySet()){
				HashMap<String, Integer> ttls_nb = issn_ttls.get(issn);
				int nbtotal = 0;
				for (String title : ttls_nb.keySet()){
					nbtotal = nbtotal + ttls_nb.get(title);
				}
				int lim = Math.max(nblim, (int) (nbtotal*pctlim));
				Set<String> ttls = new HashSet<String>();
				for (String title : ttls_nb.keySet()){
					if (ttls_nb.get(title)>lim){
						ttls.add(title);
					}
				}
				if (ttls.size()>0){
					variants.put(issn, ttls);
					writer.append(issn + "$" + String.join("#", ttls.toArray(new String[0])) + "\n");
					writer.flush();
				}
			}
			writer.close();
			
			System.out.println("nb issns with titles (high occurrence): " + variants.size());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
