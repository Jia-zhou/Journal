package com.onescience.journal.dataSources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.onescience.journal.methods.Issn_methods;
import com.onescience.journal.schema_journal.Issn;
import com.onescience.journal.schema_journal.Journal;
import com.onescience.journal.schema_journal.Publisher;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class DataUlrich {
	
	public DataUlrich(){
	}
	
	/**
	 *  analyze the languages in Ulrich journal csv list
	 *  extract some fields in a new csv file
	 *  deduplicate the new csv file
	 *  separate text and abstract languages
	 *  display all languages type
	 */
	
	// display all language strings in origin Ulrich csv file
	public void display_language(String csvfile){
		try {
			String[] line;
			int nb_line = 0;
			Multiset<String> m_langs = HashMultiset.create();
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(csvfile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, ';', '"', 1);
			while ((line = read_csv.readNext()) != null) {
				nb_line++;
				String lang = line[20];
				if ((!lang.equals(""))&&(!lang.equals("null"))){
					m_langs.add(line[20]);
				}
			}
			read_csv.close();
			in_csv.close();
			System.out.println("nb lignes: " + nb_line);
			System.out.println("nb languages: " + m_langs.size());
			System.out.println("nb languages differentes: " + m_langs.elementSet().size());
			for (String language : m_langs.elementSet()){
				System.out.println(language);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// select some fields (issn, title, publisher, languages,...) in a new csv file
	public void selectfield(String csvfile, String selectedfile){
		try {
			String[] line;
			int[] columns = {0, 1, 2, 6, 8, 9, 10, 15, 20, 32, 33, 53, 59};
			OutputStreamWriter out_csv = new OutputStreamWriter(new FileOutputStream(selectedfile), "UTF-8");
            CSVWriter write_csv = new CSVWriter(out_csv, ';', '"');
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(csvfile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, ';', '"', 0);
			while ((line = read_csv.readNext()) != null) {
				String[] champs = new String[columns.length];
				for (int i=0; i<columns.length; i++){
					String s = line[columns[i]];
					if (s.equals("null")){
						s = "";
					}
					champs[i] = s;
				}
				write_csv.writeNext(champs);
	            write_csv.flush();
			}
			read_csv.close();
			in_csv.close();
			write_csv.close();
			out_csv.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// display the duplication in the new csv file
	public void display_duplication(String selectedfile){
		try {
			String[] line;
			HashMultimap<String, String[]> id_journal = HashMultimap.create();
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(selectedfile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, ';', '"', 1);
			while ((line = read_csv.readNext()) != null) {
				id_journal.put(line[0], line);
			}
			read_csv.close();
			in_csv.close();
			for (String id : id_journal.keySet()){
				Set<String[]> js = id_journal.get(id);
				if (js.size()>1){
					System.out.println("id: " + id);
					String ligne = "";
					for (String[] journal : js){
						String jligne = "";
						for (int j=0; j<journal.length; j++){
							jligne = jligne + journal[j] + ";";
						}
						if (ligne.equals("")){
							ligne = jligne;
						} else {
							if (!jligne.equals(ligne)){
								System.out.println(ligne);
								System.out.println(jligne);
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// deduplicate the same journals in the new csv file
	public void csvdedup(String selectedfile, String dedupfile){
		try {
			String[] line;
			int nb_line = 0;
			Set<String> ids = new HashSet<String>();
			OutputStreamWriter out_csv = new OutputStreamWriter(new FileOutputStream(dedupfile), "UTF-8");
            CSVWriter write_csv = new CSVWriter(out_csv, ';', '"');
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(selectedfile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, ';', '"', 0);
			while ((line = read_csv.readNext()) != null) {
				nb_line++;
				if (!ids.contains(line[0])){
					ids.add(line[0]);
					write_csv.writeNext(line);
		            write_csv.flush();
				}
			}
			read_csv.close();
			in_csv.close();
			write_csv.close();
			out_csv.close();
			System.out.println("nb lines before: " + (nb_line-1));
			System.out.println("nb lines after: " + (ids.size()-1));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// separate the languages in text and abstract languages
	public void separate_language(String dedupfile, String modifiedfile){
		try {
			String[] line;
			int nb_line = 0;
			Set<String> all_langs = new HashSet<String>();
			Set<String> all_texts = new HashSet<String>();
			Set<String> all_abs = new HashSet<String>();
			List<String> texts = Arrays.asList("Text in ", "Text and summaries in ", "Text mainly in ", "Text occasionally in ",
					"Some issues in ", "Section in ");
			List<String> abstracts = Arrays.asList("Summaries in ", "Abstracts in ", "Text and summaries in ",
					"Abstracts occasionally in ", "Abstracts and contents page in ");
			List<String> others = Arrays.asList("Contents page in ", "Notes in ", "Prefatory materials in ");
			OutputStreamWriter out_csv = new OutputStreamWriter(new FileOutputStream(modifiedfile), "UTF-8");
            CSVWriter write_csv = new CSVWriter(out_csv, ';', '"');
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(dedupfile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, ';', '"', 0);
			while ((line = read_csv.readNext()) != null) {
				nb_line++;
				int nbc = line.length;
				String[] champs = new String[nbc+2];
				for (int i=0; i<nbc; i++){
					champs[i] = line[i];
				}
				if (nb_line == 1){
					champs[nbc] = "text_language";
					champs[nbc+1] = "abstract_language";
				} else {
					String lang = line[8];
					Set<String> langs = new HashSet<String>(Arrays.asList(lang.replace("|", "#").split(" # ")));
					Set<String> langtext = new HashSet<String>();
					Set<String> langabs = new HashSet<String>();
					for (String language : langs){
						boolean found = false;
						for (String begin : texts){
							if (language.startsWith(begin)){
								langtext.add(language.substring(begin.length()));
								all_langs.add(language.substring(begin.length()));
								all_texts.add(language.substring(begin.length()));
								found = true;
							}
						}
						for (String begin : abstracts){
							if (language.startsWith(begin)){
								langabs.add(language.substring(begin.length()));
								all_langs.add(language.substring(begin.length()));
								all_abs.add(language.substring(begin.length()));
								found = true;
							}
						}
						for (String begin : others){
							if (language.startsWith(begin)){
								found = true;
							}
						}
						if (!found){
							System.out.println(language);
						}
					}
					champs[nbc] = String.join(";", langtext.toArray(new String[0]));
					champs[nbc+1] = String.join(";", langabs.toArray(new String[0]));
				}
				write_csv.writeNext(champs);
	            write_csv.flush();
			}
			read_csv.close();
			in_csv.close();
			write_csv.close();
			out_csv.close();
			System.out.println("nb text languages: " + all_texts.size());
			System.out.println("nb abstract languages: " + all_abs.size());
			System.out.println("nb languages: " + all_langs.size());
			for (String language : all_langs){
				System.out.println(language);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// display all language strings in new Ulrich csv file
	public void language_analyse(String modifiedfile){
		try {
			String[] line;
			Multiset<String> langs = HashMultiset.create();
			Multiset<String> langs_text = HashMultiset.create();
			HashMultimap<String, String> langs_id = HashMultimap.create();
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(modifiedfile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, ';', '"', 1);
			while ((line = read_csv.readNext()) != null) {
				langs.add(line[8]);
				langs_text.add(line[13]);
				langs_id.put(line[13], line[0]);
			}
			read_csv.close();
			in_csv.close();
			System.out.println("nb journals: " + langs.size());
			System.out.println("nb language types: " + langs.elementSet().size());
			for (String lang : Multisets.copyHighestCountFirst(langs).elementSet()){
				System.out.println(langs.count(lang) + " - " + lang);
			}
			System.out.println("nb lines with empty languages: " + langs.count(""));
			System.out.println("nb lines with empty text languages: " + langs_text.count(""));
			System.out.println("ids of journals with empty text languages: " + langs_id.get(""));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// analyse les issn format in Ulrich journal list
	public void issn_analyse(String csvfile){
		try {
			String line[];
			int nb = 0;
			Multiset<String> issns = HashMultiset.create();
			Multiset<String> issns2 = HashMultiset.create();
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(csvfile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, ';', '"', 1);
			while ((line = read_csv.readNext()) != null) {
				nb++;
				String issn = line[2];
				if (new Issn_methods().issnformat(issn)){
					issns.add(issn);
				} else {
					issns2.add(issn);
				}
			}
			read_csv.close();
			in_csv.close();
			System.out.println("nb lignes: " + nb);
			System.out.println("nb issn correct: " + issns.size());
			System.out.println("nb issn correct different: " + issns.elementSet().size());
			System.out.println("nb issn uncorrect: " + issns2.size());
			System.out.println("nb issn uncorrect different: " + issns2.elementSet().size());
			System.out.println("nb issn uncorrect: " +issns2.elementSet());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// translate Ulrich journal list from csv file to json file
	public void csvtojson(String csvfile, String jsonfile){
		try {
			String line[];
			String ligne;
			int nb = 0;
			ObjectMapper mapper = new ObjectMapper();
			FileWriter writer = new FileWriter(jsonfile, false);
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(csvfile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, ';', '"', 1);
			while ((line = read_csv.readNext()) != null && nb<1) {
				Journal journal = new Journal();
				Set<String> source = new HashSet<String>();
				source.add("Ulrich");
				journal.setSource(source);
				
				Set<Issn> issns = new HashSet<Issn>();
				if (!line[2].equals("")){
					issns.add(new Issn("ISSN", line[2]));
				}
				if (issns.size()>0){
					journal.setIssns(issns);
				}
				
				Set<String> titles = new HashSet<String>();
				int[] colttl = {1, 9, 12};
				for (int i=0; i<colttl.length; i++){
					int col = colttl[i];
					if (!line[col].equals("")){
						titles.add(line[col]);
					}
				}
				if (titles.size()>0){
					journal.setTitles(titles);
				}
				/*
				Set<Link> links = new HashSet<Link>();
				if (!line[7].equals("")){
					Link link = new Link();
					link.setHref(new URI(line[7].replace(" ", "").trim()));
					//link.setHref(new URI(line[1].replace("|", "/").trim()));
					link.setRel(Link.Rel.SELF);
					links.add(link);
				}
				if (links.size()>0){
					journal.setLinks(links);;
				}
				*/
				String name = line[4];
				String place = line[5];
				String date = line[6];
				if (!(name.equals("") && place.equals("") && date.equals(""))){
					Publisher publisher = new Publisher(null, name, place, date);
					journal.setPublisher(publisher);					
				}
				
				String[] subfds = line[3].replaceAll("\\s+", " ").trim().split(", ");
				Set<String> subfields = new HashSet<String>(Arrays.asList(subfds));
				subfields.remove("");
				if (subfields.size()>0){
					journal.setSubfields(subfields);
				}
				
				String frequency = line[11];
				if (frequency.equals("")){
					journal.setFrequency(frequency);
				}
				
				String langchain = line[13] + ";" + line[14];
				String[] langs = langchain.split(";");
				Set<String> languages = new HashSet<String>(Arrays.asList(langs));
				languages.remove("");
				if (languages.size()>0){
					journal.setLanguages(languages);
				}
				
				Set<String> text_langs = new HashSet<String>(Arrays.asList(line[13].split(";")));
				text_langs.remove("");
				if (text_langs.size()>0){
					journal.setTextLanguages(text_langs);
				}
				
				Set<String> abs_langs = new HashSet<String>(Arrays.asList(line[14].split(";")));
				abs_langs.remove("");
				if (abs_langs.size()>0){
					journal.setAbstractLanguages(abs_langs);
				}
				
				ligne  = mapper.writeValueAsString(journal);
				writer.append(ligne + "\n");
			}
			read_csv.close();
			in_csv.close();
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
