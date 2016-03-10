package com.onescience.journal.clean1journal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.onescience.journal.methods.Disambiguation_methods;
import com.onescience.journal.methods.Journal_methods;
import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Journal;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class ImportLanguage {

	public static void main(String[] args) {
		
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		
		
		ImportLanguage ie = new ImportLanguage();
		
		
		
		
		
		//ie.importLanguageUlrich();
		//ie.importLanguageDoaj();
		//ie.importLanguageMedline();
		/*
		String jsonfile2 = prefix + "1journal/1journal_language2.json";
		String adatfile = prefix + "Adat/ADAT-List_2.json";
		String jsonfile3 = prefix + "1journal/1journal_language3.json";
		String cajfile = prefix + "CAJ/CAJ.json";
		String jsonfile4 = prefix + "1journal/1journal_language4.json";
		String copernicusfile = prefix + "Copernicus/copernicus.json";
		String jsonfile5 = prefix + "1journal/1journal_language5.json";
		
		String kargerfile = prefix + "Karger/karger.json";
		String jsonfile6 = prefix + "1journal/1journal_language6.json";
		String srpfile = prefix + "SRP/SRP.json";
		String jsonfile7 = prefix + "1journal/1journal_language7.json";
		String britishfile = prefix + "British_Library/British_Library.json";
		String jsonfile8 = prefix + "1journal/1journal_language8.json";
		String roadfile = prefix + "Road/road.json";
		String jsonfile9 = prefix + "1journal/1journal_language9.json";
		String locfile = prefix + "Loc/loc.json";
		String jsonfile10 = prefix + "1journal/1journal_language10.json";
		*/
		//ie.importLanguageSource(jsonfile2, jsonfile3, adatfile, "Adat");
		//ie.importLanguageSource(jsonfile3, jsonfile4, cajfile, "Caj");
		//ie.importLanguageSource(jsonfile4, jsonfile5, copernicusfile, "Copernicus");
		//ie.importLanguageSource(jsonfile5, jsonfile6, kargerfile, "Karger");
		//ie.importLanguageSource(jsonfile6, jsonfile7, srpfile, "SRP");
		//ie.importLanguageSource(jsonfile7, jsonfile8, britishfile, "British Library");
		//ie.importLanguageSource(jsonfile8, jsonfile9, roadfile, "ROAD");
		//ie.importLanguageSource(jsonfile9, jsonfile10, locfile, "LOC");
		//ie.statsLanguage(jsonfile10);
		
		//ie.statsLanguage(prefix + "1journal/1journal_issn.json");
		//ie.statsLanguage(prefix + "1journal/newjournal_issn.json");
		
		
		//ie.importLanguage();
		ie.importlinks();
		//ie.tocsv();
		
		
	}
	
	
	public void importLanguage(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		String journalfile = prefix + "1journal/newjournal_deduplicated.json";
		String languagedfile = prefix + "1journal/newjournal_lang.json";
		String normefile = prefix + "1journal/languages.csv";
		String exceptionfile = prefix + "1journal/lang_exception.txt";
		String normalizedfile = prefix + "1journal/newjournal_normlang.json";
		
		List<String> sources = new ArrayList<String>();
		sources.add(prefix+"1journal/reference_manuelle.json");
		
		sources.add(prefix+"Ulrich/Ulrich_modified.json");
		sources.add(prefix+"DOAJ/doaj_20160111.json");
		sources.add(prefix+"Medjour/medjour_modified.json");
		sources.add(prefix+"jstage/jstage.json");
		sources.add(prefix+"Adat/ADAT-List_2.json");
		sources.add(prefix+"CAJ/CAJ.json");
		sources.add(prefix+"Copernicus/copernicus.json");
		sources.add(prefix+"Karger/karger.json");
		sources.add(prefix+"SRP/SRP.json");
		sources.add(prefix+"British_Library/British_Library_modified.json");
		sources.add(prefix+"Road/road.json");
		sources.add(prefix+"Loc/loc_modified.json");
		
		
		try{
			String[] line;
			Set<String> multilang = new HashSet<String>(Arrays.asList("mul"));
			List<Journal> jnls1 = new Jsonfile().readfile(journalfile);
			for (int nbs=0; nbs<sources.size(); nbs++){
				HashMultimap<String, Integer> issn_nb1 = HashMultimap.create();
				int nb_nolang = 0;
				for (int i=0; i<jnls1.size(); i++){
					Set<String> issns = new Journal_methods().getIssnValues(jnls1.get(i));
					Set<String> lang = jnls1.get(i).getTextLanguages();
					lang.remove("und");
					if (((lang.size()==0)||(lang.equals(multilang)))&&(issns.size()>0)){
						nb_nolang++;
						for (String issn : issns){
							issn_nb1.put(issn, i);
						}
					}
				}
				System.out.println("nb journals in 1journal: " + jnls1.size());
				System.out.println("nb journals with issn without languages: " + nb_nolang);
				System.out.println("**********************************************************");
				
				
				List<Journal> jnls2 = new Jsonfile().readfile(sources.get(nbs));
				HashMultimap<String, Integer> issn_nb2 = HashMultimap.create();
				int nb_lang = 0;
				for (int i=0; i<jnls2.size(); i++){
					Set<String> lang = jnls2.get(i).getTextLanguages();
					Set<String> issns = new Journal_methods().getIssnValues(jnls2.get(i));
					if ((lang.size()>0)&&(issns.size()>0)){
						nb_lang++;
						for (String issn : issns){
							issn_nb2.put(issn, i);
						}
					}
				}
				System.out.println("nb journals in source n°" + nbs + ": "+ jnls2.size());
				System.out.println("nb journals with issn with languages: " + nb_lang);
				
				
				int nb_issn_match = 0;
				int nb_journal_match = 0;
				int nb_get_lang = 0;
				int nb_notmatch = 0;
				int nb_repeat = 0;
				int nb_comflit_lang = 0;
				for (String issn : issn_nb2.keySet()){
					Set<Integer> js1nb = issn_nb1.get(issn);
					Set<Integer> js2nb = issn_nb2.get(issn);
					if (js1nb.size()>0){
						nb_issn_match++;
						for (int nb2 : js2nb){
							Set<String> lang2 = jnls2.get(nb2).getTextLanguages();
							lang2.remove("und");
							Set<String> ttls2 = jnls2.get(nb2).getTitles();
							for (int nb1 : js1nb){
								Set<String> lang1 = jnls1.get(nb1).getTextLanguages();
								Set<String> ttls1 = jnls1.get(nb1).getTitles();
								if (nbs == 0){
									jnls1.get(nb1).setTextLanguages(lang2);
									nb_journal_match++;
									nb_get_lang++;
								} else {
									if (new Disambiguation_methods().similarTitle(ttls1, ttls2, 1)){
										nb_journal_match++;									
										if ((lang1.size()==0)||(lang1.equals(multilang))){
											nb_get_lang++;
											jnls1.get(nb1).setTextLanguages(lang2);
											if ((lang1.equals(multilang))&&(!(lang2.equals(multilang)))){
												jnls1.get(nb1).getTextLanguages().add("?");
											}
										} else {
											nb_repeat++;
											if (!lang1.equals(lang2)){
												nb_comflit_lang++;
												/*
												System.out.println("\nLanguages different detected:");
												System.out.println("issn: " + issn);
												System.out.println("langs1: " + lang1);
												System.out.println("langs2: " + lang2);
												*/
											}
										}
									} else {
										nb_notmatch++;
										/*
										System.out.println("\nSame issn, not same journal detected:");
										System.out.println("issn: " + issn);
										System.out.println(ttls1);
										System.out.println(ttls2);
										*/
									}
								}
							}
						}
					}
				}
				System.out.println("-----------------------------------------------");
				System.out.println("	- nb issns matched: " + nb_issn_match);
				System.out.println("	- nb journals matched (including repeatation): " + nb_journal_match);
				System.out.println("	- nb journals get language: " + nb_get_lang);
				System.out.println("	- nb journals repeated: " + nb_repeat);
				System.out.println("	- nb journals repeated language pb: " + nb_comflit_lang);
				System.out.println("	- nb issn match, title not match: " + nb_notmatch);
				System.out.println("\n\n");	
			}
			new Jsonfile().writefile(languagedfile, jnls1);
			
			
			
			HashMap<String, String> normes2B = new HashMap<String, String>();
			InputStreamReader in_norme = new InputStreamReader(new FileInputStream(normefile), "UTF-8");
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
			normes2B.remove("");
			read_norme.close();
			in_norme.close();
			
			String ligne;
			BufferedReader reader = new BufferedReader(new FileReader(new File(exceptionfile)));
			while ((ligne = reader.readLine()) != null) {
				line = ligne.split("#", -1);
				normes2B.put(line[0], line[1]);
			}
			reader.close();
			
			List<Journal> jnls = new Jsonfile().readfile(languagedfile);
			for (int i=0; i<jnls.size(); i++){
				Set<String> langs = jnls.get(i).getTextLanguages();
				Set<String> normedlangs = new HashSet<String>();
				for (String lang : langs){
					lang = lang.toLowerCase().trim();
					if (normes2B.containsKey(lang)){
						normedlangs.add(normes2B.get(lang));
					} else {
						System.out.println(lang);
					}
				}
				jnls.get(i).setTextLanguages(normedlangs);
			}
			new Jsonfile().writefile(normalizedfile, jnls);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void importlinks(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String normalizedfile = prefix + "newjournal_normlang.json";
		String linkfile = prefix + "links.csv";
		String linkedfile = prefix + "newjournal_normlang_complete.json";
		try {
			String[] line;
			
			HashMap<String, String> links = new HashMap<String, String>();
			InputStreamReader in_link = new InputStreamReader(new FileInputStream(linkfile), "UTF-8");
			CSVReader read_link = new CSVReader(in_link, ';', '"', 0);
			while ((line = read_link.readNext()) != null) {
				links.put(line[0], line[1]);
			}
			read_link.close();
			in_link.close();
			
			List<Journal> jnls = new Jsonfile().readfile(normalizedfile);
			HashMultimap<String, Integer> issn_nb = HashMultimap.create();
			for (int i=0; i<jnls.size(); i++){
				Set<String> issns = new Journal_methods().getIssnValues(jnls.get(i));
				for (String issn : issns){
					issn_nb.put(issn, i);
				}
			}
			
			int nb_issngetlang = 0;
			int nb_jnlgetlang = 0;
			int nb_nolang = 0;
			int nb_2lang = 0;
			Set<String> setvide = new HashSet<String>();
			for (String issn1 : links.keySet()){
				String issn2 = links.get(issn1);
				Set<Integer> nbs1 = issn_nb.get(issn1);
				Set<Integer> nbs2 = issn_nb.get(issn2);
				Set<Set<String>> langs = new HashSet<Set<String>>();
				for (int nb2 : nbs2){
					langs.add(jnls.get(nb2).getTextLanguages());
				}
				langs.remove(setvide);
				if (langs.size()==1){
					nb_issngetlang++;
					for (int nb1 : nbs1){
						nb_jnlgetlang++;
						jnls.get(nb1).setTextLanguages(langs.iterator().next());
					}
				}
				if (langs.size()<1){
					nb_nolang++;
					System.out.println("issn no language: " + issn2);
				}
				if (langs.size()>1){
					nb_2lang++;
					System.out.println("issn with more than 1 language: " + issn2 + " : " + langs);
				}
			}
			System.out.println("nb issns get language: " + nb_issngetlang);
			System.out.println("nb journals get language: " + nb_jnlgetlang);
			System.out.println("nb problem no language: " + nb_nolang);
			System.out.println("nb problem: issn with more than 1 language: " + nb_2lang);
			new Jsonfile().writefile(linkedfile, jnls);
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	public void statsLanguage(String jsonfile){
		try {
			List<Journal> jnls = new Jsonfile().readfile(jsonfile);
			Multiset<Set<String>> langs = HashMultiset.create();
			int unlang = 0;
			int nb_journal_lang = 0;
			for (int i=0; i<jnls.size(); i++){
				Set<String> lang = jnls.get(i).getTextLanguages();
				if (lang.size()>0){
					nb_journal_lang++;
					langs.add(jnls.get(i).getTextLanguages());
				}
				if (lang.size()==1){
					unlang++;
				}
			}
			double per1 = langs.size()* 1.0 /jnls.size();
			double per2 = unlang* 1.0 /jnls.size();
			System.out.println("	- Stats:");
			System.out.println("	- nb journals: " + jnls.size());
			System.out.println("	- nb journals with language: " + nb_journal_lang);
			System.out.println("	- nb journals with languages: " + langs.size() + " (" + per1 + ")");
			System.out.println("	- nb type languages: " + langs.elementSet().size());
			System.out.println("	- nb journal with one language: " + unlang + " (" + per2 + ")");
			for (Set<String> lang: Multisets.copyHighestCountFirst(langs).elementSet()){
				if (langs.count(lang)>(langs.size()*0.01)){
					System.out.println("	- " + langs.count(lang) + ": " + lang);
				}
			}
			
			System.out.println("**********************************************************");
			HashMultimap<String, Set<String>> issn_lang = HashMultimap.create();
			for (int i=0; i<jnls.size(); i++){
				Set<String> issns = new Journal_methods().getIssnValues(jnls.get(i));
				for (String issn: issns){
					issn_lang.put(issn, jnls.get(i).getTextLanguages());
				}
			}
			Set<String> setvide = new HashSet<String>();
			for (String issn : issn_lang.keySet()){
				Set<Set<String>> all_lang = issn_lang.get(issn);
				if (all_lang.size()>1){
					all_lang.remove(setvide);
					if (all_lang.size()>1){
						System.out.println(issn + ": " + all_lang);
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public void tocsv(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		//String jsonfile = prefix + "newjournal_normlang.json";
		//String csvfile = prefix + "newjournal_normlang.csv";
		String jsonfile = prefix + "newjournal_normlang_complete.json";
		String csvfile = prefix + "newjournal_normlang_complete.csv";
		
		try {
			List<Journal> journals = new Jsonfile().readfile(jsonfile);
			OutputStreamWriter out_csv = new OutputStreamWriter(new FileOutputStream(csvfile), "UTF-8");
            CSVWriter write_csv = new CSVWriter(out_csv, '$', CSVWriter.NO_QUOTE_CHARACTER);
            int nb = 0;
			for (int i=0; i<journals.size(); i++){
				nb++;
				Journal j = journals.get(i);
				String[] line = new String[4];
				
				line[0] = (Integer.toString(nb));
				
				Set<String> issns = new Journal_methods().getIssnValues(j);
				line[1] = String.join("#", issns.toArray(new String[0]));
				
				Set<String> titles = j.getTitles();
				line[2] = String.join("#", titles.toArray(new String[0]));
				
				Set<String> langs = j.getTextLanguages();
				line[3] = String.join("#", langs.toArray(new String[0]));
				
				write_csv.writeNext(line);
                write_csv.flush();
			}
			write_csv.close();
			out_csv.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void importLanguageUlrich(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		String journal1 = prefix + "1journal/1journal_deduplicated2.json";
		String ulrich = prefix + "Ulrich/Ulrich_modified.json";
		String languagefile = prefix + "1journal/1journal_language0.json";
		
		try {
			
			List<Journal> jnls1 = new Jsonfile().readfile(journal1);
			List<Journal> jnls2 = new Jsonfile().readfile(ulrich);
			
			HashMultimap<String, Integer> issn_jnls1 = HashMultimap.create();
			HashMap<String, Journal> issn_jnls2 = new HashMap<String, Journal>();
			List<Integer> noissn_jnls1 = new ArrayList<Integer>();
			List<Integer> noissn_jnls2 = new ArrayList<Integer>();
			
			for (int i=0; i<jnls1.size(); i++){
				Set<String> issns = new Journal_methods().getIssnValues(jnls1.get(i));
				if (issns.size()>0){
					for (String issn: issns){
						issn_jnls1.put(issn, i);
					}
				} else {
					noissn_jnls1.add(i);
				}
			}
			for (int i=0; i<jnls2.size(); i++){
				Set<String> issns = new Journal_methods().getIssnValues(jnls2.get(i));
				if (issns.size()>0){
					for (String issn: issns){
						issn_jnls2.put(issn, jnls2.get(i));
					}
				} else {
					noissn_jnls2.add(i);
				}
				
			}
			System.out.println("	- 1journal: ");
			System.out.println("	- nb journals: " + jnls1.size());
			System.out.println("	- nb issn-journal: " + issn_jnls1.size());
			System.out.println("	- nb different issn: " + issn_jnls1.keySet().size());
			System.out.println("	- nb journals without issn: " + noissn_jnls1.size());
			System.out.println("	- =====================================================");
			System.out.println("	- Ulrich: ");
			System.out.println("	- nb journals: " + jnls2.size());
			System.out.println("	- nb issn-journal: " + issn_jnls2.size());
			System.out.println("	- nb journals without issn: " + noissn_jnls2.size());
			System.out.println("*****************************************************");
			
			
			int nb_issn_match = 0;
			int nb_journal_match = 0;
			int nb_get_lang = 0;
			int nb_notmatch = 0;
			int nb_repeat = 0;
			int nb_comflit_lang = 0;
			for (String issn : issn_jnls2.keySet()){
				Journal julrich = issn_jnls2.get(issn);
				Set<Integer> js1nb = issn_jnls1.get(issn);
				Set<String> lang2T = julrich.getTextLanguages();
				Set<String> lang2A = julrich.getAbstractLanguages();
				Set<String> titles2 = julrich.getTitles();
				boolean issn_match = false;
				for (Integer nb : js1nb){
					Journal j1 = jnls1.get(nb);
					Set<String> titles1 = j1.getTitles();
					if (new Disambiguation_methods().similarTitle(titles2, titles1, 1)){
						issn_match = true;
						nb_journal_match++;
						Set<String> lang1T = jnls1.get(nb).getTextLanguages();
						Set<String> lang1A = jnls1.get(nb).getAbstractLanguages();
						if (lang1T.size()>0){
							nb_repeat++;
							if ((!lang1T.equals(lang2T)) || (!lang1A.equals(lang2A))){
								nb_comflit_lang++;
								
								System.out.println(nb_comflit_lang + "° issn: " + issn);
								System.out.println(julrich.getTitles());
								System.out.println(j1.getTitles());
								System.out.println("text lang: " + lang1T + "  .vs.  " + lang2T);
								System.out.println("abs lang: " + lang1A + "  .vs.  " + lang2A);
								
								lang1T.addAll(lang2T);
								lang1A.addAll(lang2A);
								jnls1.get(nb).setTextLanguages(lang1T);
								jnls1.get(nb).setAbstractLanguages(lang1A);
								titles1.addAll(titles2);
								jnls1.get(nb).setTitles(titles1);
							}
						} else {
							jnls1.get(nb).setTextLanguages(lang2T);
							jnls1.get(nb).setAbstractLanguages(lang2A);
							titles1.addAll(titles2);
							jnls1.get(nb).setTitles(titles1);
							nb_get_lang++;
						}
					}
				}
				if (issn_match){
					nb_issn_match++;
				} else {
					if (js1nb.size()>0){
						nb_notmatch++;
						System.out.println("n° " + nb_notmatch + ": " + issn);
						System.out.println(julrich.getTitles());
						for (int nb : js1nb){
							Journal j1 = jnls1.get(nb);
							System.out.println(j1.getTitles());
						}
					}
				}
			}
			System.out.println("*****************************************************");
			System.out.println("	- for issn and title match:");
			System.out.println("	- nb issns matched: " + nb_issn_match);
			System.out.println("	- nb journals matched (including repeatation): " + nb_journal_match);
			System.out.println("	- nb journals get language: " + nb_get_lang);
			System.out.println("	- nb journals repeated: " + nb_repeat);
			System.out.println("	- nb journals repeated language pb: " + nb_comflit_lang);
			System.out.println("	- nb issn match, title not match: " + nb_notmatch);
			
			
			int nb_match_title = 0;
			for (int i=0; i<noissn_jnls1.size(); i++){
				int indice1 = noissn_jnls1.get(i);
				Set<String> ttls1 = jnls1.get(indice1).getTitles();
				boolean found = false;
				int j = 0;
				while ((!found)&&(j<noissn_jnls2.size())){
					int indice2 = noissn_jnls2.get(j);
					Set<String> ttls2 = jnls2.get(indice2).getTitles();
					if (new Disambiguation_methods().similarTitle(ttls1, ttls2, 2)){
						ttls1.addAll(ttls2);
						jnls1.get(indice1).setTitles(ttls1);
						jnls1.get(indice1).setTextLanguages(jnls2.get(indice2).getTextLanguages());
						jnls1.get(indice1).setAbstractLanguages(jnls2.get(indice2).getAbstractLanguages());
						nb_match_title++;
						found = true;
					}
					j++;
				}
			}
			System.out.println("*****************************************************");
			System.out.println("	- nb title match: " + nb_match_title);
			System.out.println("	- nb journals match total: " + (nb_get_lang+nb_match_title));
			
			new Jsonfile().writefile(languagefile, jnls1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void importLanguageDoaj(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		String journal1 = prefix + "1journal/1journal_language0.json";
		String doaj = prefix + "DOAJ/doaj_20160111.json";
		String languagefile = prefix + "1journal/1journal_language1.json";
		
		try{
			List<Journal> jnls1 = new Jsonfile().readfile(journal1);
			List<Journal> jnls2 = new Jsonfile().readfile(doaj);
			
			List<Integer> nblist = new ArrayList<Integer>();
			HashMultimap<String, Integer> issn_nb1 = HashMultimap.create();
			HashMultimap<String, Integer> issn_nb2 = HashMultimap.create();
			for (int i=0; i<jnls1.size(); i++){
				Set<String> lang = jnls1.get(i).getTextLanguages();
				Set<String> issns = new Journal_methods().getIssnValues(jnls1.get(i));
				if (lang.size()==0){
					if (issns.size()>0){
						nblist.add(i);
						for (String issn : issns){
							issn_nb1.put(issn, i);
						}
					}
				}
			}
			int nb_lang_doaj = 0;
			for (int i=0; i<jnls2.size(); i++){
				Set<String> lang = jnls2.get(i).getLanguages();
				Set<String> issns = new Journal_methods().getIssnValues(jnls2.get(i));
				if (lang.size()>0){
					nb_lang_doaj++;
					for (String issn : issns){
						issn_nb2.put(issn, i);
					}
				}
			}
			System.out.println("	- nb journals total in 1journal: " + jnls1.size());
			System.out.println("	- nb journals with issn, without languages: " + nblist.size());
			System.out.println("	- nb issn-journal without language in 1journal: " + issn_nb1.size());
			System.out.println("	- =====================================================");
			System.out.println("	- nb journals in Doaj: " + jnls2.size());
			System.out.println("	- nb journals with language in Doaj: " + nb_lang_doaj);
			System.out.println("	- nb issn-journal with language in Doaj: " + issn_nb2.size());
			
			int nb_issn_match = 0;
			int nb_journal_match = 0;
			int nb_get_lang = 0;
			int nb_notmatch = 0;
			int nb_repeat = 0;
			int nb_comflit_lang = 0;
			for (String issn : issn_nb2.keySet()){
				Set<Integer> js1nb = issn_nb1.get(issn);
				Set<Integer> js2nb = issn_nb2.get(issn);
				if (js1nb.size()>0){
					nb_issn_match++;
					for (int nb2 : js2nb){
						Set<String> lang2 = jnls2.get(nb2).getLanguages();
						Set<String> ttls2 = jnls2.get(nb2).getTitles();
						for (int nb1 : js1nb){
							Set<String> ttls1 = jnls1.get(nb1).getTitles();
							if (new Disambiguation_methods().similarTitle(ttls1, ttls2, 1)){
								nb_journal_match++;
								Set<String> lang1 = jnls1.get(nb1).getTextLanguages();
								if (lang1.size()==0){
									jnls1.get(nb1).setTextLanguages(lang2);
									nb_get_lang++;
								} else {
									nb_repeat++;
									if (!lang1.equals(lang2)){
										nb_comflit_lang++;
										//System.out.println("Languages different detected:");
										//System.out.println("issn: " + issn);
										//System.out.println("langs1: " + lang1);
										//System.out.println("langs2: " + lang2);
									}
								}
							} else {
								nb_notmatch++;
								//System.out.println("Same issn, not same journal detected:");
								//System.out.println("issn: " + issn);
								//System.out.println(ttls1);
								//System.out.println(ttls2);
							}
						}
					}
					
				}
			}
			System.out.println("	- nb issn matched: " + nb_issn_match);
			System.out.println("	- nb journals matched (including repeatation): " + nb_journal_match);
			System.out.println("	- nb journals get language: " + nb_get_lang);
			System.out.println("	- nb journals repeated: " + nb_repeat);
			System.out.println("	- nb journals repeated language pb: " + nb_comflit_lang);
			System.out.println("	- nb journals with same issn not matched: " + nb_notmatch);
			
			new Jsonfile().writefile(languagefile, jnls1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void importLanguageMedline(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		String journal1 = prefix + "1journal/1journal_language1.json";
		String medline = prefix + "Medjour/medjour_modified.json";
		String languagefile = prefix + "1journal/1journal_language2.json";
		
		try{
			List<Journal> jnls1 = new Jsonfile().readfile(journal1);
			List<Journal> jnls2 = new Jsonfile().readfile(medline);
			
			List<Integer> nblist = new ArrayList<Integer>();
			HashMultimap<String, Integer> issn_nb1 = HashMultimap.create();
			HashMultimap<String, Integer> issn_nb2 = HashMultimap.create();
			List<Integer> noissn_jnls1 = new ArrayList<Integer>();
			List<Integer> noissn_jnls2 = new ArrayList<Integer>();
			for (int i=0; i<jnls1.size(); i++){
				Set<String> lang = jnls1.get(i).getTextLanguages();
				Set<String> issns = new Journal_methods().getIssnValues(jnls1.get(i));
				if (lang.size()==0){
					if (issns.size()>0){
						nblist.add(i);
						for (String issn : issns){
							issn_nb1.put(issn, i);
						}
					} else {
						noissn_jnls1.add(i);
					}
				}
			}
			int nb_lang_medline = 0;
			for (int i=0; i<jnls2.size(); i++){
				Set<String> lang = jnls2.get(i).getLanguages();
				Set<String> issns = new Journal_methods().getIssnValues(jnls2.get(i));
				if (lang.size()>0){
					nb_lang_medline++;
					if (issns.size()>0){
						for (String issn : issns){
							issn_nb2.put(issn, i);
						}
					} else {
						noissn_jnls2.add(i);
					}
				}
			}
			System.out.println("	- nb journals total in 1journal: " + jnls1.size());
			System.out.println("	- nb journals with issn, without languages: " + nblist.size());
			System.out.println("	- nb issn-journal without languages in 1journal: " + issn_nb1.size());
			System.out.println("	- nb journals without issn, without languages in 1journal: " + noissn_jnls1.size());
			System.out.println("	- =====================================================");
			System.out.println("	- nb journals in Medline: " + jnls2.size());
			System.out.println("	- nb journals with language in Medline: " + nb_lang_medline);
			System.out.println("	- nb issn-journal with language in Medline: " + issn_nb2.size());
			System.out.println("	- nb journals without issn, with languages in Medline: " + noissn_jnls2.size());
			
			int nb_issn_match = 0;
			int nb_journal_match = 0;
			int nb_get_lang = 0;
			int nb_notmatch = 0;
			int nb_repeat = 0;
			int nb_comflit_lang = 0;
			for (String issn : issn_nb2.keySet()){
				Set<Integer> js1nb = issn_nb1.get(issn);
				Set<Integer> js2nb = issn_nb2.get(issn);
				if (js1nb.size()>0){
					nb_issn_match++;
					for (int nb2 : js2nb){
						Set<String> lang2 = jnls2.get(nb2).getLanguages();
						Set<String> ttls2 = jnls2.get(nb2).getTitles();
						for (int nb1 : js1nb){
							Set<String> ttls1 = jnls1.get(nb1).getTitles();
							if (new Disambiguation_methods().similarTitle(ttls1, ttls2, 1)){
								nb_journal_match++;
								Set<String> lang1 = jnls1.get(nb1).getTextLanguages();
								if (lang1.size()==0){
									ttls1.addAll(ttls2);
									jnls1.get(nb1).setTitles(ttls1);
									jnls1.get(nb1).setTextLanguages(lang2);
									nb_get_lang++;
								} else {
									nb_repeat++;
									if (!lang1.equals(lang2)){
										nb_comflit_lang++;
										ttls1.addAll(ttls2);
										jnls1.get(nb1).setTitles(ttls1);
										lang1.addAll(lang2);
										jnls1.get(nb1).setTextLanguages(lang1);
										
										//System.out.println("Languages different detected (1 journal in 1journal matches with 2 journals in medline):");
										//System.out.println("issn: " + issn);
										//System.out.println("langs1: " + lang1);
										//System.out.println("langs2: " + lang2);
									}
								}
							} else {
								nb_notmatch++;
								//System.out.println("Same issn, not same journal detected:");
								//System.out.println("issn: " + issn);
								//System.out.println(ttls1);
								//System.out.println(ttls2);
							}
						}
					}
					
				}
			}
			System.out.println("	- for issn and title match:");
			System.out.println("	- nb issn matched: " + nb_issn_match);
			System.out.println("	- nb journals matched (including repeatation): " + nb_journal_match);
			System.out.println("	- nb journals get language: " + nb_get_lang);
			System.out.println("	- nb journals repeated: " + nb_repeat);
			System.out.println("	- nb journals repeated language pb: " + nb_comflit_lang);
			System.out.println("	- nb journals with same issn not matched: " + nb_notmatch);
			
			
			
			int nb_match_title = 0;
			for (int i=0; i<noissn_jnls1.size(); i++){
				int indice1 = noissn_jnls1.get(i);
				Set<String> ttls1 = jnls1.get(indice1).getTitles();
				boolean found = false;
				int j = 0;
				while ((!found)&&(j<noissn_jnls2.size())){
					int indice2 = noissn_jnls2.get(j);
					Set<String> ttls2 = jnls2.get(indice2).getTitles();
					if (new Disambiguation_methods().similarTitle(ttls1, ttls2, 2)){
						ttls1.addAll(ttls2);
						jnls1.get(indice1).setTitles(ttls1);
						jnls1.get(indice1).setTextLanguages(jnls2.get(indice2).getLanguages());
						jnls1.get(indice1).setAbstractLanguages(jnls2.get(indice2).getAbstractLanguages());
						nb_match_title++;
						found = true;
					}
					j++;
				}
			}
			System.out.println("*****************************************************");
			System.out.println("	- nb title match: " + nb_match_title);
			System.out.println("	- nb journals match total: " + (nb_get_lang+nb_match_title));
			new Jsonfile().writefile(languagefile, jnls1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void importLanguageSource(String originfile, String writefile, String sourcefile, String sourcename){
		
		try{
			List<Journal> jnls1 = new Jsonfile().readfile(originfile);
			List<Journal> jnls2 = new Jsonfile().readfile(sourcefile);
			
			List<Integer> nblist = new ArrayList<Integer>();
			HashMultimap<String, Integer> issn_nb1 = HashMultimap.create();
			HashMultimap<String, Integer> issn_nb2 = HashMultimap.create();
			List<Integer> noissn_jnls1 = new ArrayList<Integer>();
			List<Integer> noissn_jnls2 = new ArrayList<Integer>();
			for (int i=0; i<jnls1.size(); i++){
				Set<String> lang = jnls1.get(i).getTextLanguages();
				Set<String> issns = new Journal_methods().getIssnValues(jnls1.get(i));
				if (lang.size()==0){
					if (issns.size()>0){
						nblist.add(i);
						for (String issn : issns){
							issn_nb1.put(issn, i);
						}
					} else {
						noissn_jnls1.add(i);
					}
				}
			}
			int nb_lang_source = 0;
			for (int i=0; i<jnls2.size(); i++){
				Set<String> lang = jnls2.get(i).getTextLanguages();
				Set<String> issns = new Journal_methods().getIssnValues(jnls2.get(i));
				if (lang.size()>0){
					nb_lang_source++;
					if (issns.size()>0){
						for (String issn : issns){
							issn_nb2.put(issn, i);
						}
					} else {
						noissn_jnls2.add(i);
					}
				}
			}
			System.out.println("	- nb journals total in origin json file: " + jnls1.size());
			System.out.println("	- nb journals with issn, without languages: " + nblist.size());
			System.out.println("	- nb issn-journal without languages: " + issn_nb1.size());
			System.out.println("	- nb journals without issn, without languages: " + noissn_jnls1.size());
			System.out.println("	- =====================================================");
			System.out.println("	- nb journals in " + sourcename + ": " + jnls2.size());
			System.out.println("	- nb journals with language: " + nb_lang_source);
			System.out.println("	- nb issn-journal with language: " + issn_nb2.size());
			System.out.println("	- nb journals without issn, with languages: " + noissn_jnls2.size());
			
			int nb_issn_match = 0;
			int nb_journal_match = 0;
			int nb_get_lang = 0;
			int nb_notmatch = 0;
			int nb_repeat = 0;
			int nb_comflit_lang = 0;
			for (String issn : issn_nb2.keySet()){
				Set<Integer> js1nb = issn_nb1.get(issn);
				Set<Integer> js2nb = issn_nb2.get(issn);
				if (js1nb.size()>0){
					nb_issn_match++;
					for (int nb2 : js2nb){
						Set<String> lang2 = jnls2.get(nb2).getTextLanguages();
						Set<String> ttls2 = jnls2.get(nb2).getTitles();
						for (int nb1 : js1nb){
							Set<String> ttls1 = jnls1.get(nb1).getTitles();
							if (new Disambiguation_methods().similarTitle(ttls1, ttls2, 1)){
								nb_journal_match++;
								Set<String> lang1 = jnls1.get(nb1).getTextLanguages();
								if (lang1.size()==0){
									ttls1.addAll(ttls2);
									jnls1.get(nb1).setTitles(ttls1);
									jnls1.get(nb1).setTextLanguages(lang2);
									nb_get_lang++;
								} else {
									nb_repeat++;
									if (!lang1.equals(lang2)){
										nb_comflit_lang++;
										ttls1.addAll(ttls2);
										jnls1.get(nb1).setTitles(ttls1);
										lang1.addAll(lang2);
										jnls1.get(nb1).setTextLanguages(lang1);
										//System.out.println("Languages different detected (1 journal in 1journal matches with 2 journals in source file):");
										//System.out.println("issn: " + issn);
										//System.out.println("langs1: " + lang1);
										//System.out.println("langs2: " + lang2);
									}
								}
							} else {
								nb_notmatch++;
								//System.out.println("Same issn, not same journal detected:");
								//System.out.println("issn: " + issn);
								//System.out.println(ttls1);
								//System.out.println(ttls2);
							}
						}
					}
					
				}
			}
			System.out.println("	- for issn and title match:");
			System.out.println("	- nb issn matched: " + nb_issn_match);
			System.out.println("	- nb journals matched (including repeatation): " + nb_journal_match);
			System.out.println("	- nb journals get language: " + nb_get_lang);
			System.out.println("	- nb journals repeated: " + nb_repeat);
			System.out.println("	- nb journals repeated language pb: " + nb_comflit_lang);
			System.out.println("	- nb journals with same issn not matched: " + nb_notmatch);
			
			
			int nb_match_title = 0;
			for (int i=0; i<noissn_jnls1.size(); i++){
				int indice1 = noissn_jnls1.get(i);
				Set<String> ttls1 = jnls1.get(indice1).getTitles();
				boolean found = false;
				int j = 0;
				while ((!found)&&(j<noissn_jnls2.size())){
					int indice2 = noissn_jnls2.get(j);
					Set<String> ttls2 = jnls2.get(indice2).getTitles();
					if (new Disambiguation_methods().similarTitle(ttls1, ttls2, 2)){
						ttls1.addAll(ttls2);
						jnls1.get(indice1).setTitles(ttls1);
						jnls1.get(indice1).setTextLanguages(jnls2.get(indice2).getTextLanguages());
						jnls1.get(indice1).setAbstractLanguages(jnls2.get(indice2).getAbstractLanguages());
						nb_match_title++;
						found = true;
					}
					j++;
				}
			}
			System.out.println("*****************************************************");
			System.out.println("	- nb title match: " + nb_match_title);
			System.out.println("	- nb journals match total: " + (nb_get_lang+nb_match_title));
			
			
			new Jsonfile().writefile(writefile, jnls1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
