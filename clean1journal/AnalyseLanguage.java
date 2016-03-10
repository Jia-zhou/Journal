package com.onescience.journal.clean1journal;

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

import uk.ac.shef.wit.simmetrics.similaritymetrics.SoundexTest;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.onescience.journal.methods.Disambiguation_methods;
import com.onescience.journal.methods.Journal_methods;
import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Journal;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class AnalyseLanguage {

	public static void main(String[] args) {
		
		AnalyseLanguage ie = new AnalyseLanguage();
		//ie.saisielanguages();
		//ie.normalizelanguages();
		//ie.comparelanguages();
		//ie.compareReference();
		//ie.analyseonelanguage();
		ie.manips();
		
	}
	
	
	public void saisielanguages(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		//String journal = prefix + "1journal/1journal_deduplicated2.json";
		String journal = prefix + "1journal/journal_issn/1journal_issn.json";
		String ulrichfile = prefix + "Ulrich/Ulrich_modified.json";
		List<String> sources = new ArrayList<String>();
		sources.add(prefix+"Ulrich/Ulrich_modified.json");
		sources.add(prefix+"DOAJ/doaj_20160111.json");
		sources.add(prefix+"Medjour/medjour_modified.json");
		sources.add(prefix+"Adat/ADAT-List_2.json");
		sources.add(prefix+"CAJ/CAJ.json");
		sources.add(prefix+"Copernicus/copernicus.json");
		sources.add(prefix+"Karger/karger.json");
		sources.add(prefix+"SRP/SRP.json");
		sources.add(prefix+"British_Library/British_Library_modified.json");
		sources.add(prefix+"Road/road.json");
		sources.add(prefix+"Loc/loc_modified.json");
		//String resultfile = prefix + "1journal/languages/languages_sources2.csv";
		String resultfile = prefix + "1journal/journal_issn/languages_sources.csv";
		
		
		try{
			List<Journal> jnls1 = new Jsonfile().readfile(journal);
			String[] all_langs = new String[jnls1.size()];
			HashMultimap<String, Integer> issn_nb1 = HashMultimap.create();
			for (int i=0; i<jnls1.size(); i++){
				Set<String> issns = new Journal_methods().getIssnValues(jnls1.get(i));
				if (issns.size()>0){
					for (String issn : issns){
						issn_nb1.put(issn, i);
					}
				}
				String title = String.join("#", jnls1.get(i).getTitles().toArray(new String[0]));
				String issn = String.join("#", issns.toArray(new String[0]));
				all_langs[i] = issn + "$" + title;
			}
			System.out.println("nb journals in 1journal: " + jnls1.size());
			
			for (int nbs=0; nbs<sources.size(); nbs++){
				List<Journal> jnls2 = new Jsonfile().readfile(sources.get(nbs));
				HashMultimap<String, Integer> issn_nb2 = HashMultimap.create();
				int nb_lang = 0;
				for (int i=0; i<jnls2.size(); i++){
					Set<String> lang = jnls2.get(i).getTextLanguages();
					Set<String> issns = new Journal_methods().getIssnValues(jnls2.get(i));
					if (lang.size()>0){
						nb_lang++;
						for (String issn : issns){
							issn_nb2.put(issn, i);
						}	
					}
				}
				System.out.println("\nsource nb: " + nbs);
				System.out.println("nb journals: " + jnls2.size());
				System.out.println("nb journals with languages: " + nb_lang);
				
				
				String[] source_langs = new String[jnls1.size()];
				for (int i=0; i<jnls1.size(); i++){
					source_langs[i] = "";
				}
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
							String lang2 = String.join("#", jnls2.get(nb2).getTextLanguages().toArray(new String[0]));
							Set<String> ttls2 = jnls2.get(nb2).getTitles();
							for (int nb1 : js1nb){
								Set<String> ttls1 = jnls1.get(nb1).getTitles();
								if (new Disambiguation_methods().similarTitle(ttls1, ttls2, 1)){
									nb_journal_match++;
									String lang1 = source_langs[nb1];
									if (lang1.equals("")){
										source_langs[nb1] = lang2;
										nb_get_lang++;
									} else {
										nb_repeat++;
										String[] langs = lang1.split(" .vs. ", -1);
										boolean found = false;
										for (int i=0; i<langs.length; i++){
											if (langs[i].equals(lang2)){
												found = true;
											}
										}
										if (!found){
											nb_comflit_lang++;
											source_langs[nb1] = source_langs[nb1] + " .vs. " + lang2;
										}
									}
								} else {
									nb_notmatch++;
								}
							}
						}
					}
				}
				
				System.out.println("nb issn matched: " + nb_issn_match);
				System.out.println("nb journals matched (including repeatation): " + nb_journal_match);
				System.out.println("nb journals get language: " + nb_get_lang);
				System.out.println("nb journals repeated: " + nb_repeat);
				System.out.println("nb journals repeated language pb: " + nb_comflit_lang);
				System.out.println("nb journals with same issn not matched: " + nb_notmatch);
				
				for (int i=0; i<jnls1.size(); i++){
					all_langs[i] = all_langs[i] + "$" + source_langs[i];
				}
			}
			
			// Ulrich abstract langauges
			List<Journal> jnls2 = new Jsonfile().readfile(ulrichfile);
			HashMultimap<String, Integer> issn_nb2 = HashMultimap.create();
			int nb_lang = 0;
			for (int i=0; i<jnls2.size(); i++){
				Set<String> lang = jnls2.get(i).getAbstractLanguages();
				Set<String> issns = new Journal_methods().getIssnValues(jnls2.get(i));
				if (lang.size()>0){
					nb_lang++;
					for (String issn : issns){
						issn_nb2.put(issn, i);
					}	
				}
			}
			System.out.println("\nabstract languages in Ulrich: ");
			System.out.println("nb journals: " + jnls2.size());
			System.out.println("nb journals with abstract languages: " + nb_lang);
			
			String[] source_langs = new String[jnls1.size()];
			for (int i=0; i<jnls1.size(); i++){
				source_langs[i] = "";
			}
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
						String lang2 = String.join("#", jnls2.get(nb2).getAbstractLanguages().toArray(new String[0]));
						Set<String> ttls2 = jnls2.get(nb2).getTitles();
						for (int nb1 : js1nb){
							Set<String> ttls1 = jnls1.get(nb1).getTitles();
							if (new Disambiguation_methods().similarTitle(ttls1, ttls2, 1)){
								nb_journal_match++;
								String lang1 = source_langs[nb1];
								if (lang1.equals("")){
									source_langs[nb1] = lang2;
									nb_get_lang++;
								} else {
									nb_repeat++;
									String[] langs = lang1.split(" .vs. ", -1);
									boolean found = false;
									for (int i=0; i<langs.length; i++){
										if (langs[i].equals(lang2)){
											found = true;
										}
									}
									if (!found){
										nb_comflit_lang++;
										source_langs[nb1] = source_langs[nb1] + " .vs. " + lang2;
									}
								}
							} else {
								nb_notmatch++;
							}
						}
					}
				}
			}
			
			System.out.println("nb issn matched: " + nb_issn_match);
			System.out.println("nb journals matched (including repeatation): " + nb_journal_match);
			System.out.println("nb journals get language: " + nb_get_lang);
			System.out.println("nb journals repeated: " + nb_repeat);
			System.out.println("nb journals repeated language pb: " + nb_comflit_lang);
			System.out.println("nb journals with same issn not matched: " + nb_notmatch);
			
			for (int i=0; i<jnls1.size(); i++){
				all_langs[i] = all_langs[i] + "$" + source_langs[i];
			}
			
			
			
			
			FileWriter writer = new FileWriter(resultfile, false);
			String head = "issn$titles$Ulrich$DOAJ$Medjour$Adat$CAJ$Copernicus$Karger$SRP$British_Library$Road$Loc$Ulrich(abs)";
			writer.append(head + "\n");
			writer.flush();
			for (int i=0; i<jnls1.size(); i++){
				writer.append(all_langs[i] + "\n");
				writer.flush();
			}
			writer.close();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void normalizelanguages(){
		//String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/languages/";
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/journal_issn/";
		String languaguefile = prefix + "languages_sources.csv";
		String normalizedfile = prefix + "languages_sources_normalized.csv";
		String normefile = prefix + "languages.csv";
		String exceptionfile = prefix + "lang_exception.txt";
		
		try {
			String line[];
			
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
			
			
			Set<String> exceptions = new HashSet<String>();
			OutputStreamWriter out_csv = new OutputStreamWriter(new FileOutputStream(normalizedfile), "UTF-8");
            CSVWriter write_csv = new CSVWriter(out_csv, '$', '¸');
            InputStreamReader in_csv = new InputStreamReader(new FileInputStream(languaguefile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, '$', '¸', 0);
			line = read_csv.readNext();
			write_csv.writeNext(line);
            write_csv.flush();
            int nb = 0;
			while (((line = read_csv.readNext()) != null) ) {
				nb++;
				for (int i=2; i<line.length; i++){
					String norlangs = line[i].toLowerCase();
					String[] langs = norlangs.split(" .vs. ", -1);
					for (int j=0; j<langs.length; j++){
						String[] lang = langs[j].split("#", -1);
						for (int l=0; l<lang.length; l++){
							String c = lang[l].trim();
							if (normes2B.get(c) != null){
								norlangs = norlangs.replaceAll(c,normes2B.get(c));
							} else {
								if (!c.equals("")){
									System.out.println("nb: " + nb + " - " + c);
									exceptions.add(c);
								}
							}
						}
					}
					line[i] = norlangs;
				}
				write_csv.writeNext(line);
                write_csv.flush();
                
			}
			read_csv.close();
			in_csv.close();
			write_csv.close();
			out_csv.close();
			
			System.out.println("languages not identified: ");
			for (String exc : exceptions){
				System.out.println(exc);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void comparelanguages(){
		//String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/languages/";
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/journal_issn/";
		String languaguefile = prefix + "languages_sources_normalized.csv";
		String comflitfile = prefix + "comflit_langs_sources.csv";
		String comflitfile2 = prefix + "comflit_langs_2sources.csv";
		String comparefile = prefix + "comparaison.txt";
		
		try {
			String line[];
			List<String> sources = new ArrayList<String>(Arrays.asList("Ulrich", "Doaj", "Medline", "Adat", "Caj", "Copernicus", "Karger", "Srp", "BL", "Road", "Loc"));
			int nbsrcs = sources.size();
			List<String> compareType = new ArrayList<String>(Arrays.asList("same", "no sense", "no conflict", "almost same", "partially same", "commun element", "conflict"));
			int nbtype = compareType.size();
			List<Integer> nb_sources = new ArrayList<Integer>();
			int[] jnl_nbs = new int[nbsrcs+1];
			int[] jnl_1source = new int[nbsrcs];
			int[] intra_comflit = new int[nbsrcs];
			int[] intra_ncomflit = new int[nbsrcs];
			int[][] intra_comflitType = new int[nbsrcs][nbtype];
			int[][][] inter_complitType = new int[nbsrcs][nbsrcs][nbtype];
			int nb_pb = 0;
			int nb_ligne = 0;
			FileWriter comparaison = new FileWriter(comparefile, true);
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(languaguefile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, '$', '¸', 1);
			while (((line = read_csv.readNext()) != null) ) {
				nb_ligne++;
				int nbs = 0;
				int last_source = 0;
				for (int i=2; i<nbsrcs+2; i++){
					if (!line[i].equals("")){
						nbs++;
						last_source = i;
						String[] langs = line[i].split(" .vs. ", -1);
						if (langs.length>1){
							intra_comflit[i-2]++;
							if (langs.length<3){
								Set<String> lang1 = new HashSet<String>(Arrays.asList(langs[0]));
								Set<String> lang2 = new HashSet<String>(Arrays.asList(langs[1]));
								int type = new Journal_methods().sameSetlanguages(lang1, lang2);
								intra_comflitType[i-2][type]++;
							} else {
								intra_ncomflit[i-2]++;
								System.out.println("conflict (>2) intra-source: -" + sources.get(i-2) + "- " + line[i]);
							}
						}
					}
				}
				nb_sources.add(nbs);
				jnl_nbs[nbs]++;
				if (nbs == 1){
					jnl_1source[last_source-2]++;
				}
				
				
				if (nbs == 2){
					String[] all_langs = Arrays.copyOfRange(line, 2, nbsrcs+3);
					for (int i=0; i<nbsrcs; i++){
						if (!all_langs[i].equals("")){
							Set<String> lang1 = new HashSet<String>();
							String[] lan1 = all_langs[i].split(" .vs. ", -1);
							for (int nb=0; nb<lan1.length; nb++){
								lang1.addAll(Arrays.asList(lan1[nb].split("#", -1)));
							}
							for (int j=i+1; j<nbsrcs; j++){
								if (!all_langs[j].equals("")){
									Set<String> lang2 = new HashSet<String>();
									String[] lan2 = all_langs[j].split(" .vs. ", -1);
									for (int nb=0; nb<lan2.length; nb++){
										lang2.addAll(Arrays.asList(lan2[nb].split("#", -1)));
									}
									int type = new Journal_methods().sameSetlanguages(lang1, lang2);
									inter_complitType[i][j][type]++;
								}
							}
						}
					}
				}
				
				
				
				//for (int nbconf=nbsrcs; nbconf>1; nbconf--){
					if (nbs == 2){
						comparaison.append("\n" + nb_ligne + " - nb sources: " + nbs + "\n");
						comparaison.append(line[0] + "$" + line[1] + "\n");
						String[] all_langs = Arrays.copyOfRange(line, 2, nbsrcs+3);
						HashMultimap<Set<String>, String> langs_source = HashMultimap.create();
						Set<Set<String>> all_setlang = new HashSet<Set<String>>();
						
						for (int i=0; i<nbsrcs; i++){
							if (!all_langs[i].equals("")){
								//Set<String> lang1 = new HashSet<String>();
								String[] lan1 = all_langs[i].split(" .vs. ", -1);
								for (int nb=0; nb<lan1.length; nb++){
									//lang1.addAll(Arrays.asList(lan1[nb].split("#", -1)));
									Set<String> lang = new HashSet<String>(Arrays.asList(lan1[nb].split("#", -1)));
									all_setlang.add(lang);
									langs_source.put(lang, sources.get(i));
								}
								//all_setlang.add(lang1);
								//langs_source.put(lang1, sources.get(i));	
							}
						}
						comparaison.append(langs_source.keySet().size() + " set de langauges differnents\n");
						for (Set<String> langs : langs_source.keySet()){
							comparaison.append(langs_source.get(langs) + " : " + langs + "\n");
							comparaison.flush();
						}
						
					}
				//}
				
				
				
				
				
				
				
				
				
				
				
				
				
			}
			read_csv.close();
			in_csv.close();
			comparaison.close();
			
			
			/*
			int nb_jnls = 0;
			System.out.println("\nnb sources by journals:");
			for (int i=0; i<jnl_nbs.length; i++){
				System.out.println(i + " sources by journal: " + jnl_nbs[i] + " journals");
				nb_jnls = nb_jnls + jnl_nbs[i];
			}
			System.out.println("nb journals total: " + nb_jnls);
			
			System.out.println("\nfor journal language from one only source:");
			for (int i=0; i<nbsrcs; i++){
				if (jnl_1source[i]>0){
					System.out.println(sources.get(i) + ": " + jnl_1source[i] + " journals");
				}
			}
			*/
			
			/*
			System.out.println("\nlanguages conflicts intra-source:");
			for (int i=0; i<nbsrcs; i++){
				if (intra_ncomflit[i]>0){
					System.out.println(sources.get(i) + ": " + intra_comflit[i] + " conflicts; more than 2 conflicts: " + intra_ncomflit[i]);
				} else {
					System.out.println(sources.get(i) + ": " + intra_comflit[i]);
				}
			}
			
			System.out.println("\nlanguages conflicts type intra-source:");
			for (int i=0; i<nbsrcs; i++){
				System.out.println("\nSource: " + sources.get(i) + ", nb conflicts: " + intra_comflit[i]);
				for (int j=0; j<nbtype; j++){
					System.out.println(compareType.get(j) + ": " + intra_comflitType[i][j]);
				}
			}
			
			System.out.println("\nlanguages conflicts type inter-sources:");
			for (int i=0; i<nbsrcs; i++){
				for (int j=i+1; j<nbsrcs; j++){
					System.out.println("\n");
					System.out.println("Compare sources : " + sources.get(i) + " et " + sources.get(j));
					for (int nb=0; nb<nbtype; nb++){
						System.out.print(compareType.get(nb) + " - ");
					}
					System.out.print("\n");
					for (int nb=0; nb<nbtype; nb++){
						System.out.print(inter_complitType[i][j][nb] + "-");
					}
				}
			}
			*/
			
			/*
			String[][] comflits = new String[nbsrcs+1][nbsrcs+1];
			for (int i=0; i<nbsrcs; i++){
				String intracom = "";
				for (int nb=0; nb<nbtype; nb++){
					intracom = intracom + "-" + intra_comflitType[i][nb];
				}
				comflits[i+1][i+1] = intracom.substring(1);
			}
			for (int i=0; i<nbsrcs; i++){
				for (int j=i+1; j<nbsrcs; j++){
					String intercom = "";
					for (int nb=0; nb<nbtype; nb++){
						intercom = intercom + "-" + inter_complitType[i][j][nb];
					}
					comflits[i+1][j+1] = intercom.substring(1);
				}
			}
			for (int i=0; i<nbsrcs; i++){
				comflits[0][i+1] = sources.get(i);
				comflits[i+1][0] = sources.get(i);
			}
			
			FileWriter writer = new FileWriter(comflitfile2, false);
			for (int i=0; i<nbsrcs+1; i++){
				String ligne = "";
				for (int j=0; j<nbsrcs+1; j++){
					if (comflits[i][j] == null){
						ligne = ligne + ";";
					} else {
						ligne = ligne + ";" + comflits[i][j];
					}
				}
				writer.append(ligne.substring(1) + "\n");
				writer.flush();
			}
			writer.close();
			*/
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	public void compareReference(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String languaguefile = prefix + "languages/languages_sources2_normalized.csv";
		String referencefile = prefix + "journal_issn/journal_languages_manual.csv";
		String writefile = prefix + "journal_issn/ref_lang_analyse.txt";
		
		try {
			String[] line;
			List<String> sources = new ArrayList<String>(Arrays.asList("Ulrich", "Doaj", "Medline", "Adat", "Caj", "Copernicus", "Karger", "Srp", "BL", "Road", "Loc"));
			int nbsrcs = sources.size();
			
			Multiset<String> issn_ref = HashMultiset.create();
			HashMap<String, Set<String>> ref_lang = new HashMap<String, Set<String>>();
			HashMap<String, String> result = new HashMap<String, String>();
			InputStreamReader in_ref = new InputStreamReader(new FileInputStream(referencefile), "UTF-8");
			CSVReader read_ref = new CSVReader(in_ref, ';', '¸', 0);
			while (((line = read_ref.readNext()) != null) ) {
				String[] issns = line[0].split("#", -1);
				Set<String> langs = new HashSet<String>(Arrays.asList(line[2].split("#", -1)));
				for (int i=0; i<issns.length; i++){
					ref_lang.put(issns[i], langs);
					issn_ref.add(issns[i]);
					result.put(issns[i], (issns[i] + " : " + line[1] + "\n"));
				}
			}
			read_ref.close();
			in_ref.close();
			
			/*
			System.out.println("nb issns: " + issn_ref.size());
			System.out.println("nb issns differents: " + issn_ref.elementSet().size());
			System.out.println("nb issn_journal: " + ref_lang.size());
			for (String issn : issn_ref.elementSet()){
				if (issn_ref.count(issn)>1){
					System.out.println(issn);
				}
			}
			*/
			
			
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(languaguefile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, '$', '¸', 1);
			while (((line = read_csv.readNext()) != null) ) {
				String[] issns = line[0].split("#", -1);
				for (int j=0; j<issns.length; j++){
					if (ref_lang.containsKey(issns[j])){
						String[] all_langs = Arrays.copyOfRange(line, 2, nbsrcs+3);
						HashMultimap<Set<String>, String> langs_source = HashMultimap.create();
						Set<Set<String>> all_setlang = new HashSet<Set<String>>();
						
						for (int i=0; i<nbsrcs; i++){
							if (!all_langs[i].equals("")){
								String[] lan1 = all_langs[i].split(" .vs. ", -1);
								for (int nb=0; nb<lan1.length; nb++){
									Set<String> lang = new HashSet<String>(Arrays.asList(lan1[nb].split("#", -1)));
									all_setlang.add(lang);
									langs_source.put(lang, sources.get(i));
								}
							}
						}
						
						String oldmess = result.get(issns[j]);
						String newmess = oldmess + "reference : " + ref_lang.get(issns[j]) + "\n";
						newmess = newmess + langs_source.keySet().size() + " set de langauges differnents\n";
						for (Set<String> langs : langs_source.keySet()){
							newmess = newmess + (langs_source.get(langs) + " : " + langs + "\n");
						}
						result.remove(issns[j], oldmess);
						result.put(issns[j], newmess);
					}
				}
				
			}
			read_csv.close();
			in_csv.close();
			
			
			FileWriter writer = new FileWriter(writefile, true);
			for (String issn : result.keySet()){
				writer.append(result.get(issn) + "\n");
				writer.append("==================================\n");
				writer.flush();
			}
			writer.close();
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void analyseonelanguage(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String reflangfile = prefix + "journal_language_norm.csv";
		String langIsofile = prefix + "languages/languages.csv";
		String sourcelangfile = prefix + "languages/languages_sources_normalized.csv";
		String analysefile = prefix + "languages/onelangref.txt";
		
		try {
			String[] line;
			List<String> sources = new ArrayList<String>(Arrays.asList("Ulrich", "Doaj", "Medline", "Adat", "Caj", "Copernicus", "Karger", "Srp", "BL", "Road", "Loc"));
			int nbsrcs = sources.size();
			HashMap<Integer, String> lang_ref = new HashMap<Integer, String>();
			Set<String> langIso = new HashSet<String>();
			/*
			InputStreamReader in_iso = new InputStreamReader(new FileInputStream(langIsofile), "UTF-8");
			CSVReader read_iso = new CSVReader(in_iso, '\t', '"', 1);
			while (((line = read_iso.readNext()) != null) ) {
				langIso.add(line[5].trim());
			}
			read_iso.close();
			in_iso.close();
			*/
			langIso.add("dut");
			InputStreamReader in_ref = new InputStreamReader(new FileInputStream(reflangfile), "UTF-8");
			CSVReader read_ref = new CSVReader(in_ref, ',', '"', 0);
			int numb = 0;
			int nb_jnls = 0;
			int nb_onelang = 0;
			while (((line = read_ref.readNext()) != null) ) {
				int numbj = Integer.parseInt(line[0]);
				if (numbj != numb){
					nb_jnls++;
					if (langIso.contains(line[4].trim())){
						nb_onelang++;
						lang_ref.put(numbj, line[4].trim());
					}
					numb = numbj;
				}
			}
			read_ref.close();
			in_ref.close();
			System.out.println("nb journals: " + nb_jnls);
			System.out.println("nb journals with one language: " + nb_onelang);
			System.out.println("nb number-jounral with one language: " + lang_ref.size());
			
			FileWriter writer = new FileWriter(analysefile, false);
			InputStreamReader in_source = new InputStreamReader(new FileInputStream(sourcelangfile), "UTF-8");
			CSVReader read_source = new CSVReader(in_source, '$', '¸', 1);
			int numbl = 0;
			int nb_pbs = 0;
			int real_pbs = 0;
			while (((line = read_source.readNext()) != null) ) {
				numbl++;
				if (lang_ref.containsKey(numbl)){
					//System.out.println("OK found");
					int nb_oui = 0;
					int nb_non = 0;
					for (int i=2; i<nbsrcs+2; i++){
						if (!line[i].equals("")){
							if (line[i].equals(lang_ref.get(numbl))){
								nb_oui++;
							} else {
								nb_non++;
							}
						}
					}
					if ((nb_non>0) || (nb_oui==1)){
						nb_pbs++;
						
						if (nb_non>0){
							real_pbs++;
							
							
							
						}
						String[] all_langs = Arrays.copyOfRange(line, 2, nbsrcs+3);
						HashMultimap<Set<String>, String> langs_source = HashMultimap.create();
						Set<Set<String>> all_setlang = new HashSet<Set<String>>();
						
						for (int i=0; i<nbsrcs; i++){
							if (!all_langs[i].equals("")){
								String[] lan1 = all_langs[i].split(" .vs. ", -1);
								for (int nb=0; nb<lan1.length; nb++){
									Set<String> lang = new HashSet<String>(Arrays.asList(lan1[nb].split("#", -1)));
									all_setlang.add(lang);
									langs_source.put(lang, sources.get(i));
								}
							}
						}
						
						writer.append(nb_pbs + " - " + line[0] + " - " +line[1] + "\n");
						writer.append("ref: " + lang_ref.get(numbl) + "\n");
						writer.append(langs_source.keySet().size() + " set de langauges differnents\n");
						for (Set<String> langs : langs_source.keySet()){
							writer.append(langs_source.get(langs) + " : " + langs + "\n");
						}
						writer.append("\n");
						writer.flush();
						
					}
				}
			}
			read_source.close();
			in_source.close();
			writer.close();
			
			System.out.println("nb problems: " + nb_pbs);
			System.out.println("nb real problems: " + real_pbs);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public void manips(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String readfile = prefix + "reference_manuelle.csv";
		//String readfile = prefix + "other_journal/scielojournalslist_modif.csv";
		try {
			String[] line;
			Multiset<String> issns = HashMultiset.create();
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(readfile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, ',', '"', 0);
			while (((line = read_csv.readNext()) != null) ) {
				issns.add(line[0]);
			}
			read_csv.close();
			in_csv.close();
			System.out.println("nb issns: " + issns.size());
			System.out.println("nb issns differents: " + issns.elementSet().size());
			
			for (String issn: issns){
				if (issns.count(issn)>1){
					System.out.println(issn);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	
	public void manips2(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String readfile = prefix + "other_journal/others_deduplicated.json";
		
		try {
			List<Journal> journals = new Jsonfile().readfile(readfile);
			Multiset<String> issns = HashMultiset.create();
			for (int i=0; i<journals.size(); i++){
				Set<String> issn = new Journal_methods().getIssnValues(journals.get(i));
				for (String is : issn){
					issns.add(is);
				}
			}
			System.out.println("nb issns: " + issns.size());
			System.out.println("nb issns differents: " + issns.elementSet().size());
			
			for (String issn: issns){
				if (issns.count(issn)>1){
					System.out.println(issn);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	
	public void manips3(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String readfile = prefix + "newjournal_normlang.csv";
		try {
			String[] line;
			int maxlength = 0;
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(readfile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, '$', CSVWriter.NO_QUOTE_CHARACTER, 0);
			while (((line = read_csv.readNext()) != null) ) {
				int length = line[2].length();
				if (length>maxlength){
					maxlength = length;
				}
			}
			read_csv.close();
			in_csv.close();
			System.out.println("max length : " + maxlength);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
