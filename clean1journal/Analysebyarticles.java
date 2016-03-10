package com.onescience.journal.clean1journal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.onescience.journal.methods.Journal_methods;
import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Journal;
import com.onescience.utils.SetUtil;
import com.opencsv.CSVReader;

public class Analysebyarticles {

	public static void main(String[] args) {
		
		Analysebyarticles ie = new Analysebyarticles();
		//ie.analyseMedline();
		//ie.normeWoslang();
		//ie.analyseMedlinelang();
		//ie.analyseWoslang();
		//ie.analyselang();
		
		
	}
	
	
	public void analyseMedline(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/Pubmed/";
		String datapath = prefix + "data";
		String langfile = prefix + "issn_lang.csv";
		String linkfile = prefix + "issn_occ.csv";
		
		try {
			String[] line;
			int nb_file = 0;
			int nb_noissn = 0;
			Multiset<String> issn_lang = HashMultiset.create();
			Multiset<String> issn_occ = HashMultiset.create();
			
			
			File f = new File(datapath);
	        File[] files = f.listFiles();
	        for (File file : files) {
	        	nb_file++;
	        	System.out.println(file.getAbsolutePath());
	        	
	        	System.out.println("n° file: " + nb_file);
	        	InputStreamReader in_csv = new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF-8");
    			CSVReader read_csv = new CSVReader(in_csv, ';', '"', 0);
    			while ((line = read_csv.readNext()) != null) {
    				String issn = "";
    				if ((!line[0].equals(""))&&(!line[1].equals(""))){
    					issn = line[0] + "#" + line[1];
    				} else {
    					if (!line[0].equals("")){
    						issn = line[0];
    					}
    					if (!line[1].equals("")){
    						issn = line[1];
    					}
    				}
    				issn_occ.add(issn);
    				
    				
    				if (!line[0].equals("")){
    					issn_lang.add(line[0] + " : " + line[5]);
    					if (!line[1].equals("")){
    						issn_lang.add(line[1] + " : " + line[5]);
    					} else {
    						issn_lang.add(line[0] + " : " + line[5]);
    					}
    				} else {
    					if (!line[1].equals("")){
    						issn_lang.add(line[1] + " : " + line[5]);
    						issn_lang.add(line[1] + " : " + line[5]);
    					} else {
    						nb_noissn++;
    					}
    				}
    				
    			}
    			read_csv.close();
    			in_csv.close();
	        }
	        issn_lang.removeAll(Arrays.asList("ISSN : Language", "ISSNLinking : Language"));
	        
	        
	        FileWriter writer = new FileWriter(langfile, false);
	        for (String issn : Multisets.copyHighestCountFirst(issn_lang).elementSet()){
	        	String[] ss = issn.split(" : ", -1);
	        	writer.append(ss[0] + ";" + ss[1] + ";" + issn_lang.count(issn)*1.0/2 + "\n");
	        	writer.flush();
	        }
	        writer.close();
	        System.out.println("nb lines different: " + issn_lang.size()/2);
	        System.out.println("nb artilces without issn: " + nb_noissn);
	        System.out.println("nb files: " + nb_file);
	        
	        
	        
	        
	        System.out.println(issn_occ.contains("ISSN#ISSNLinking"));
	        issn_occ.remove("ISSN#ISSNLinking");
	        System.out.println(issn_occ.contains("ISSN#ISSNLinking"));
	        
	        FileWriter writer2 = new FileWriter(linkfile, false);
	        for (String issn : Multisets.copyHighestCountFirst(issn_occ).elementSet()){
	        	writer2.append(issn + ":" + issn_occ.count(issn) + "\n");
	        	writer2.flush();
	        }
	        writer2.close();
	        System.out.println("nb lines: " + issn_occ.size());
	        System.out.println("nb issns different: " + issn_occ.elementSet().size());
	        
	        
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void normeWoslang(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		String langfile = prefix + "SM1/language_wos.txt";
		String normfile = prefix + "SM1/normlang_wos.txt";
		String langnormfile = prefix + "1journal/languages.csv";
		
		try{
			String[] line;
			
			HashMap<String, String> normes2B = new HashMap<String, String>();
			InputStreamReader in_norme = new InputStreamReader(new FileInputStream(langnormfile), "UTF-8");
			CSVReader read_norme = new CSVReader(in_norme, '\t', '"', 1);
			while ((line = read_norme.readNext()) != null) {
				String n1 = line[3].trim();
				String n2T = line[4].trim();
				String n2B = line[5].trim();
				normes2B.put(n1, n2B);
				normes2B.put(n2T, n2B);
				normes2B.put(n2B, n2B);
				String[] noms1 = line[1].split(",", -1);
				for (int i=0; i<noms1.length; i++){
					normes2B.put(noms1[i].toLowerCase().trim(), n2B);
				}
				String[] noms2 = line[2].split(",", -1);
				for (int i=0; i<noms2.length; i++){
					normes2B.put(noms2[i].toLowerCase().trim(), n2B);
				}
			}
			
			read_norme.close();
			in_norme.close();
			normes2B.remove("");
			normes2B.put("multiple languages", "mul");
			normes2B.put("multi-language", "mul");
			normes2B.put("provencal", "und");
			normes2B.put("rumanian", "und");
			normes2B.put("byelorussian", "und");
			normes2B.put("serbo-croatian", "und");
			normes2B.put("romansch", "und");
			
			int nb = 0;
			Set<String> exception = new HashSet<String>();
			FileWriter writer = new FileWriter(normfile, false);
			InputStreamReader in_lang = new InputStreamReader(new FileInputStream(langfile), "UTF-8");
			CSVReader read_lang = new CSVReader(in_lang, '\t', '"', 0);
			while ((line = read_lang.readNext()) != null) {
				nb++;
				String lang = line[1].toLowerCase();
				if (normes2B.containsKey(lang)){
					writer.append(line[0] + ";" + normes2B.get(lang) + ";" + line[2] + "\n");
					writer.flush();
				} else {
					//System.out.println("language not detected: " + lang + "  --  " + "issn: " + line[0]);
					System.out.println("OK");
					exception.add(lang);
				}
			}
			read_lang.close();
			in_lang.close();
			writer.close();
			
			System.out.println("nb lines: " + nb);
			System.out.println(exception.size());
			for (String lang : exception){
				System.out.println(lang);
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public void analyseMedlinelang(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		String langfile = prefix + "Pubmed/issn_lang.csv";
		String jnlsfile = prefix + "1journal/newjournal_normlang_complete.json";
		String occfile = prefix + "1journal/1db_issns_occ.csv";
		String comparefile = prefix + "Pubmed/compare_lang.txt"; 
		
		try {
			String[] line;
			List<Journal> jnls = new Jsonfile().readfile(jnlsfile);
			HashMultimap<String, Integer> issn_nb1 = HashMultimap.create();
			for (int i=0; i<jnls.size(); i++){
				Set<String> issns = new Journal_methods().getIssnValues(jnls.get(i));
				for (String issn : issns){
					issn_nb1.put(issn, i);
				}
			}
			
			int nb_articles = 0;
			HashMap<String, Double> issn_occ = new HashMap<String, Double>();
			InputStreamReader in_occ = new InputStreamReader(new FileInputStream(occfile), "UTF-8");
			CSVReader read_occ = new CSVReader(in_occ, ',', '"', 0);
			while ((line = read_occ.readNext()) != null) {
				int nb_atls = Integer.parseInt(line[1].trim());
				nb_articles = nb_articles + nb_atls;
				if (!line[0].equals("")){
					String[] issns = line[0].split("#", -1);
					double m = nb_atls*1.0/issns.length;
					for (int i=0; i<issns.length; i++){
						if (!issn_occ.containsKey(issns[i])){
							issn_occ.put(issns[i], m);
						} else {
							double valeur = issn_occ.get(issns[i]);
							issn_occ.remove(issns[i]);
							issn_occ.put(issns[i], valeur+m);
						}
					}
				}
			}
			read_occ.close();
			in_occ.close();
			issn_occ = new SetUtil().triAvecValeur(issn_occ);
			
			HashMultimap<String, String> issn_lang = HashMultimap.create();
			InputStreamReader in_lang = new InputStreamReader(new FileInputStream(langfile), "UTF-8");
			CSVReader read_lang = new CSVReader(in_lang, ';', '"', 0);
			while ((line = read_lang.readNext()) != null) {
				issn_lang.put(line[0], line[1] + " -- " + line[2]);
			}
			read_lang.close();
			in_lang.close();
			
			FileWriter writer = new FileWriter(comparefile, false);
			for (String issn : issn_occ.keySet()){
				Set<Integer> nbs = issn_nb1.get(issn);
				Set<String> medline = issn_lang.get(issn);
				if (nbs.size()==0){
					if (medline.size()==0){
						writer.append(issn + ":" + issn_occ.get(issn) + ":not in 1journal:" + ":not in Medline:\n");
						writer.flush();
					} else {
						writer.append(issn + ":" + issn_occ.get(issn) + ":not in 1journal:" + ":Medline:" + medline + "\n");
						writer.flush();
					}
					
				} else {
					Set<Set<String>> langs = new HashSet<Set<String>>();
					for (int nb : nbs){
						langs.add(jnls.get(nb).getTextLanguages());
					}
					if (medline.size()==0){
						writer.append(issn + ":" + issn_occ.get(issn) + ":1journal: " + langs + ":not in Medline:\n");
						writer.flush();
					} else {
						writer.append(issn + ":" + issn_occ.get(issn) + ":1journal: " + langs + ":Medline:" + medline + "\n");
						writer.flush();
					}
					
				}
				
			}
			writer.close();
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void analyseWoslang(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		String langfile = prefix + "SM1/normlang_wos.txt";
		String jnlsfile = prefix + "1journal/newjournal_normlang_complete.json";
		String occfile = prefix + "1journal/1db_issns_occ.csv";
		String comparefile = prefix + "SM1/compare_lang.txt";
		
		try {
			String[] line;
			List<Journal> jnls = new Jsonfile().readfile(jnlsfile);
			HashMultimap<String, Integer> issn_nb1 = HashMultimap.create();
			for (int i=0; i<jnls.size(); i++){
				Set<String> issns = new Journal_methods().getIssnValues(jnls.get(i));
				for (String issn : issns){
					issn_nb1.put(issn, i);
				}
			}
			
			int nb_articles = 0;
			HashMap<String, Double> issn_occ = new HashMap<String, Double>();
			InputStreamReader in_occ = new InputStreamReader(new FileInputStream(occfile), "UTF-8");
			CSVReader read_occ = new CSVReader(in_occ, ',', '"', 0);
			while ((line = read_occ.readNext()) != null) {
				int nb_atls = Integer.parseInt(line[1].trim());
				nb_articles = nb_articles + nb_atls;
				if (!line[0].equals("")){
					String[] issns = line[0].split("#", -1);
					double m = nb_atls*1.0/issns.length;
					for (int i=0; i<issns.length; i++){
						if (!issn_occ.containsKey(issns[i])){
							issn_occ.put(issns[i], m);
						} else {
							double valeur = issn_occ.get(issns[i]);
							issn_occ.remove(issns[i]);
							issn_occ.put(issns[i], valeur+m);
						}
					}
				}
			}
			read_occ.close();
			in_occ.close();
			issn_occ = new SetUtil().triAvecValeur(issn_occ);
			
			HashMultimap<String, String> issn_lang = HashMultimap.create();
			InputStreamReader in_lang = new InputStreamReader(new FileInputStream(langfile), "UTF-8");
			CSVReader read_lang = new CSVReader(in_lang, ';', '"', 0);
			while ((line = read_lang.readNext()) != null) {
				issn_lang.put(line[0], line[1] + " -- " + line[2]);
			}
			read_lang.close();
			in_lang.close();
			
			FileWriter writer = new FileWriter(comparefile, false);
			for (String issn : issn_occ.keySet()){
				Set<Integer> nbs = issn_nb1.get(issn);
				Set<String> wos = issn_lang.get(issn);
				if (nbs.size()==0){
					if (wos.size()==0){
						writer.append(issn + ":" + issn_occ.get(issn) + ":not in 1journal:" + ":not in Wos:\n");
						writer.flush();
					} else {
						writer.append(issn + ":" + issn_occ.get(issn) + ":not in 1journal:" + ":Wonew HashMap<String, String>()s:" + wos + "\n");
						writer.flush();
					}
					
				} else {
					Set<Set<String>> langs = new HashSet<Set<String>>();
					for (int nb : nbs){
						langs.add(jnls.get(nb).getTextLanguages());
					}
					if (wos.size()==0){
						writer.append(issn + ":" + issn_occ.get(issn) + ":1journal: " + langs + ":not in Wos:\n");
						writer.flush();
					} else {
						writer.append(issn + ":" + issn_occ.get(issn) + ":1journal: " + langs + ":Wos:" + wos + "\n");
						writer.flush();
					}
					
				}
				
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void analyselang(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		String langfile1 = prefix + "Pubmed/issn_lang.csv";
		String langfile2 = prefix + "SM1/normlang_wos.txt";
		String jnlsfile = prefix + "1journal/newjournal_normlang_complete.json";
		String occfile = prefix + "1journal/1db_issns_occ.csv";
		String comparefile = prefix + "1journal/compare_lang.txt";
		String probfile = prefix + "1journal/compare_lang_prob.csv";
		
		try {
			String[] line;
			int nb_lim = 25;
			double seuil_lim = 0.01;
			
			List<Journal> jnls = new Jsonfile().readfile(jnlsfile);
			HashMultimap<String, Integer> issn_nb1 = HashMultimap.create();
			for (int i=0; i<jnls.size(); i++){
				Set<String> issns = new Journal_methods().getIssnValues(jnls.get(i));
				for (String issn : issns){
					issn_nb1.put(issn, i);
				}
			}
			
			int nb_articles = 0;
			HashMap<String, Double> issn_occ = new HashMap<String, Double>();
			InputStreamReader in_occ = new InputStreamReader(new FileInputStream(occfile), "UTF-8");
			CSVReader read_occ = new CSVReader(in_occ, ',', '"', 0);
			while ((line = read_occ.readNext()) != null) {
				int nb_atls = Integer.parseInt(line[1].trim());
				nb_articles = nb_articles + nb_atls;
				if (!line[0].equals("")){
					String[] issns = line[0].split("#", -1);
					double m = nb_atls*1.0/issns.length;
					for (int i=0; i<issns.length; i++){
						if (!issn_occ.containsKey(issns[i])){
							issn_occ.put(issns[i], m);
						} else {
							double valeur = issn_occ.get(issns[i]);
							issn_occ.remove(issns[i]);
							issn_occ.put(issns[i], valeur+m);
						}
					}
				}
			}
			read_occ.close();
			in_occ.close();
			issn_occ = new SetUtil().triAvecValeur(issn_occ);
			
			HashMultimap<String, String> issn_lang1 = HashMultimap.create();
			InputStreamReader in_lang1 = new InputStreamReader(new FileInputStream(langfile1), "UTF-8");
			CSVReader read_lang1 = new CSVReader(in_lang1, ';', '"', 0);
			while ((line = read_lang1.readNext()) != null) {
				issn_lang1.put(line[0], line[1] + " -- " + line[2]);
			}
			read_lang1.close();
			in_lang1.close();
			HashMap<String, HashMap<String, Double>> issn_medline = new HashMap<String, HashMap<String, Double>>();
			for (String issn : issn_occ.keySet()){
				HashMap<String, Double> lang_nb = new HashMap<String, Double>();
				Set<String> langs = issn_lang1.get(issn);
				double som = 0.0;
				for (String lang : langs){
					som = som + Double.parseDouble(lang.split(" -- ", -1)[1]);
				}
				double nb_lim2 = Math.ceil(som * seuil_lim);
				for (String lang : langs){
					double nb_art = Double.parseDouble(lang.split(" -- ", -1)[1]);
					if ((nb_art>nb_lim)||(nb_art>nb_lim2)){
						lang_nb.put(lang.split(" -- ", -1)[0], nb_art);
					}
				}
				issn_medline.put(issn, new SetUtil().triAvecValeur(lang_nb));
			}
			
			
			HashMultimap<String, String> issn_lang2 = HashMultimap.create();
			InputStreamReader in_lang2 = new InputStreamReader(new FileInputStream(langfile2), "UTF-8");
			CSVReader read_lang2 = new CSVReader(in_lang2, ';', '"', 0);
			while ((line = read_lang2.readNext()) != null) {
				issn_lang2.put(line[0], line[1] + " -- " + line[2]);
			}
			read_lang2.close();
			in_lang2.close();
			HashMap<String, HashMap<String, Double>> issn_wos = new HashMap<String, HashMap<String, Double>>();
			for (String issn : issn_occ.keySet()){
				HashMap<String, Double> lang_nb = new HashMap<String, Double>();
				Set<String> langs = issn_lang2.get(issn);
				int som = 0;
				for (String lang : langs){
					som = som + Integer.parseInt(lang.split(" -- ", -1)[1]);
				}
				double nb_lim2 = Math.ceil(som * seuil_lim);
				for (String lang : langs){
					int nb_art = Integer.parseInt(lang.split(" -- ", -1)[1]);
					if ((nb_art>nb_lim)||(nb_art>nb_lim2)){
						lang_nb.put(lang.split(" -- ", -1)[0], nb_art*1.0);
					}
				}
				issn_wos.put(issn, new SetUtil().triAvecValeur(lang_nb));
			}
			
			/*
			FileWriter writer = new FileWriter(comparefile, false);
			for (String issn : issn_occ.keySet()){
				Set<Integer> nbs = issn_nb1.get(issn);
				Set<String> medline = issn_lang1.get(issn);
				Set<String> wos = issn_lang2.get(issn);
				if (nbs.size()==0){
					if (medline.size()==0){
						if (wos.size()==0){
							writer.append(issn + ":" + issn_occ.get(issn) + ":::\n");
							writer.flush();
						} else {
							writer.append(issn + ":" + issn_occ.get(issn) + "::" + wos + "\n");
							writer.flush();
						}
					} else {
						if (wos.size()==0){
							writer.append(issn + ":" + issn_occ.get(issn) + ":" + medline + ":\n");
							writer.flush();
						} else {
							writer.append(issn + ":" + issn_occ.get(issn) + ":" + medline + ":" + wos + "\n");
							writer.flush();
						}
					}
				} else {
					Set<Set<String>> langs = new HashSet<Set<String>>();
					for (int nb : nbs){
						langs.add(jnls.get(nb).getTextLanguages());
					}
					if (medline.size()==0){
						if (wos.size()==0){
							writer.append(issn + ":" + issn_occ.get(issn) + ":" + langs + "::\n");
							writer.flush();
						} else {
							writer.append(issn + ":" + issn_occ.get(issn) + ":" + langs + "::" + wos + "\n");
							writer.flush();
						}
					} else {
						if (wos.size()==0){
							writer.append(issn + ":" + issn_occ.get(issn) + ":" + langs + ":" + medline + ":\n");
							writer.flush();
						} else {
							writer.append(issn + ":" + issn_occ.get(issn) + ":" + langs + ":" + medline + ":" + wos + "\n");
							writer.flush();
						}
					}
					
				}
				
			}
			writer.close();
			*/
			
			
			
			String ligne;
			Set<String> setvide = new HashSet<String>();
			FileWriter writer2 = new FileWriter(probfile, false);
			BufferedReader reader = new BufferedReader(new FileReader(new File(comparefile)));
			while ((ligne = reader.readLine()) != null) {
				String issn = ligne.substring(0, 9);
				Set<Set<String>> langs = new HashSet<Set<String>>();
				Set<Integer> nbs = issn_nb1.get(issn);
				for (int nb : nbs){
					langs.add(jnls.get(nb).getTextLanguages());
				}
				langs.remove(setvide);
				
				Set<String> langs_medline = issn_medline.get(issn).keySet();
				Set<String> langs_wos = issn_wos.get(issn).keySet();

				if (langs.size()==1){
					boolean diff_med = false;
					boolean diff_wos = false;
					if (langs_medline.size()>0){
						if (!langs.iterator().next().equals(langs_medline)){
							diff_med = true;
						}
					}
					if (langs_wos.size()>0){
						if (!langs.iterator().next().equals(langs_wos)){
							diff_wos = true;
						}
					}
					if (diff_med && diff_wos){
						writer2.append(ligne + ":" + langs_medline + ":" + langs_wos + ":medline&wos\n");
						writer2.flush();
					} else {
						if (diff_med){
							writer2.append(ligne + ":" + langs_medline + ":" + langs_wos + ":medline\n");
							writer2.flush();
						}
						if (diff_wos){
							writer2.append(ligne + ":" + langs_medline + ":" + langs_wos + ":wos\n");
							writer2.flush();
						}
					}
				} else {
					writer2.append(ligne + ":" + langs_medline + ":" + langs_wos + ":1journal\n");
					writer2.flush();
				}
			}
			reader.close();
			writer2.close();
			
			
			
			
			
			
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
