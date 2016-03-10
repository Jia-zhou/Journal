package com.onescience.journal.build1journal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.onescience.journal.methods.Disambiguation_methods;
import com.onescience.journal.methods.Journal_methods;
import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Journal;
import com.opencsv.CSVReader;

public class Addlanguage {
	
	public Addlanguage(){
	}
	
	/**
	 * Import languages from different sources to 1journal list
	 * first source is the manual reference, and the priority of sources decreases in the list
	 * import condition: for manual reference: same Issn value; for other sources: same Issn value and silimar title (level=1)
	 * journals without Issn will not be treated
	 */
	public void importLanguage(String jnlsfile, List<String> sources, String languagedfile){
		try{
			Set<String> multilang = new HashSet<String>(Arrays.asList("mul"));
			List<Journal> jnls1 = new Jsonfile().readfile(jnlsfile);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// normalize the languages by language Iso2B
	public void normalizeLanguage(String jnlsfile, String normfile, String exceptionfile, String normalizedfile){
		try{
			String[] line;
			HashMap<String, String> norm2B = new HashMap<String, String>();
			InputStreamReader in_norm = new InputStreamReader(new FileInputStream(normfile), "UTF-8");
			CSVReader read_norm = new CSVReader(in_norm, '\t', '"', 1);
			while ((line = read_norm.readNext()) != null) {
				String n1 = line[3].trim();
				String n2T = line[4].trim();
				String n2B = line[5].trim();
				norm2B.put(n1, n2B);
				norm2B.put(n2T, n2B);
				norm2B.put(n2B, n2B);
				String[] noms1 = line[1].split(",", -1);
				for (int i=0; i<noms1.length; i++){
					norm2B.put(noms1[i].toLowerCase().trim(), n2B);
				}
				String[] noms2 = line[2].split(",", -1);
				for (int i=0; i<noms2.length; i++){
					norm2B.put(noms2[i].toLowerCase().trim(), n2B);
				}
			}
			norm2B.remove("");
			read_norm.close();
			in_norm.close();
			
			String ligne;
			BufferedReader reader = new BufferedReader(new FileReader(new File(exceptionfile)));
			while ((ligne = reader.readLine()) != null) {
				line = ligne.split("#", -1);
				norm2B.put(line[0], line[1]);
			}
			reader.close();
			
			List<Journal> jnls = new Jsonfile().readfile(jnlsfile);
			for (int i=0; i<jnls.size(); i++){
				Set<String> langs = jnls.get(i).getTextLanguages();
				Set<String> normedlangs = new HashSet<String>();
				for (String lang : langs){
					lang = lang.toLowerCase().trim();
					if (norm2B.containsKey(lang)){
						normedlangs.add(norm2B.get(lang));
					} else {
						System.out.println(lang);
					}
				}
				jnls.get(i).setTextLanguages(normedlangs);
			}
			new Jsonfile().writefile(normalizedfile, jnls);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// complete journals languages by Issn links (for a issn-eissn, if journal(issn) have language, journal(eissn) will take the same language)
	public void completeLanguage(String jnlsfile, String linkfile, String linkedfile){
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
			
			List<Journal> jnls = new Jsonfile().readfile(jnlsfile);
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
	

}
