package com.onescience.journal.build1journal;

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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.onescience.journal.methods.Disambiguation_methods;
import com.onescience.journal.methods.Issn_methods;
import com.onescience.journal.methods.Journal_methods;
import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Id;
import com.onescience.journal.schema_journal.Issn;
import com.onescience.journal.schema_journal.Journal;
import com.onescience.journal.schema_journal.StatusPR;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class Cleanformer1journal {
	
	public Cleanformer1journal(){
	}
	
	// replace the coded characters in all titles of 1journal list, and separate certain titles par "#"
	public void clean_title(String jnlsfile, String cleanedfile, String caracfile){
		try {
			String ligne;
			List<String> caracs = new ArrayList<String>();
			List<String> replace = new ArrayList<String>();
			BufferedReader read_caracs = new BufferedReader(new FileReader(new File(caracfile)));
			while ((ligne = read_caracs.readLine()) != null) {
				String[] replaces = ligne.split("-", -1);
				caracs.add(replaces[0]);
				replace.add(replaces[1]);
			}
			read_caracs.close();
			
			FileWriter writer = new FileWriter(cleanedfile, true);
			BufferedReader reader = new BufferedReader(new FileReader(new File(jnlsfile)));
			while ((ligne = reader.readLine()) != null) {
				for (int i=0; i<caracs.size(); i++){
					ligne = ligne.replaceAll(caracs.get(i), replace.get(i));
				}
				if (containsHanScript(ligne)){
					ligne = ligne.replaceAll("/", "#");
				}
				writer.append(ligne + "\n");
			}
			reader.close();
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// list all Issn values which don't match Issn standard format
	public void build_wrongIssn(String jnlsfile, String writefile){
		try{
			String[] line;
			Multiset<String> m_issn = HashMultiset.create();
			Multiset<String> m_pissn = HashMultiset.create();
			Multiset<String> m_eissn = HashMultiset.create();
			
			FileWriter write_issn = new FileWriter(writefile, true);
			InputStreamReader in_jnls = new InputStreamReader(new FileInputStream(jnlsfile), "UTF-8");
			CSVReader read_jnls = new CSVReader(in_jnls, ';', '"', 1);
			while ((line = read_jnls.readNext()) != null) {
				String issn = line[3].trim();
				String pissn = line[4].trim();
				String eissn = line[5].trim();
				
				if ((!(new Issn_methods().issnformat(issn)))&&(!issn.equals("null"))&&(!issn.equals(""))){
					if(!m_issn.contains(issn)){
						//write_issn.append("  issn:  " + issn + "  ---  eissn: " + eissn + "  ---  pissn: " + pissn + "\n");
						write_issn.append("  issn:  " + issn + "\n");
					}
					m_issn.add(issn);
				}
				if ((!(new Issn_methods().issnformat(pissn)))&&(!pissn.equals("NULL"))&&(!pissn.equals(""))&&(!pissn.equals("E-only-title"))){
					if(!m_pissn.contains(pissn)){
						//write_issn.append("p_issn:  " + pissn + "  ---  issn: " + issn + "  ---  eissn: " + eissn + "\n");
						write_issn.append("p_issn:  " + pissn + "\n");
					}
					m_pissn.add(pissn);
				}
				if ((!(new Issn_methods().issnformat(eissn)))&&(!eissn.equals("NULL"))&&(!eissn.equals(""))){
					if(!m_eissn.contains(eissn)){
						//write_issn.append("e_issn:  " + eissn + "  ---  issn: " + issn + "  ---  pissn: " + pissn + "\n");
						write_issn.append("e_issn:  " + eissn + "\n");
					}
					m_eissn.add(eissn);
				}
			}
			read_jnls.close();
			in_jnls.close();
			write_issn.flush();
			write_issn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// separate certain Issn like: 1740-7141 (print)1743-1654 (online)
	public void clean_issn1(String jnlsfile, String cleanedfile){
		try {
			String[] line;
			OutputStreamWriter out_jnls = new OutputStreamWriter(new FileOutputStream(cleanedfile), "UTF-8");
            CSVWriter write_jnls = new CSVWriter(out_jnls, ';', '"');
			InputStreamReader in_jnls = new InputStreamReader(new FileInputStream(jnlsfile), "UTF-8");
			CSVReader read_jnls = new CSVReader(in_jnls, ';', '"', 0);
			line = read_jnls.readNext();
			write_jnls.writeNext(line);
            write_jnls.flush();
            
            while ((line = read_jnls.readNext()) != null) {
            	String issn = line[3].replaceAll(" ", "").trim();
            	String pissn = "";
            	String eissn = "";
            	int position_p = issn.indexOf("(print)");
            	int position_e = issn.indexOf("(online)");
            	if (position_p>0){
            		pissn = issn.substring(position_p-9, position_p);
            	}
            	if(position_e>0){
            		eissn = issn.substring(position_e-9, position_e);
            	}
            	if ((position_p>0)||(position_e>0)){
            		issn = "";
            	}
            	line[3] = issn;
            	if (!pissn.equals("")){
            		line[4] = pissn;
            	}
            	if (!eissn.equals("")){
            		line[5] = eissn;
            	}
            	write_jnls.writeNext(line);
                write_jnls.flush();
            }
            read_jnls.close();
			in_jnls.close();
			write_jnls.close();
			out_jnls.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// return certain Issn to standard format
	public void clean_issn2(String jnlsfile, String cleanedfile){
		try{
			String[] line;
			OutputStreamWriter out_jnls = new OutputStreamWriter(new FileOutputStream(cleanedfile), "UTF-8");
            CSVWriter write_jnls = new CSVWriter(out_jnls, ';', '"');
			InputStreamReader in_jnls = new InputStreamReader(new FileInputStream(jnlsfile), "UTF-8");
			CSVReader read_jnls = new CSVReader(in_jnls, ';', '"', 0);
			line = read_jnls.readNext();
			write_jnls.writeNext(line);
            write_jnls.flush();
            
			while ((line = read_jnls.readNext()) != null) {
				String issn = line[3].trim();
				String eissn = line[5].trim();
				if ((!(new Issn_methods().issnformat(issn)))&&(!issn.equals("null"))&&(!issn.equals(""))&&(!issn.equals("978-31230-9-2"))){
					Pattern pattern = Pattern.compile("\\d{7}(\\d|x|X)");
					String issn2 = issn.replaceAll("[^0-9xX]","");
					if (pattern.matcher(issn2).matches()){
						issn = issn2.substring(0,4) + "-" + issn2.substring(4,8);
					}
				}
				if ((!(new Issn_methods().issnformat(eissn)))&&(!eissn.equals("NULL"))&&(!eissn.equals(""))){
					Pattern pattern = Pattern.compile("\\d{7}(\\d|x|X)");
					String eissn2 = eissn.replaceAll("[^0-9xX]","");
					if (pattern.matcher(eissn2).matches()){
						eissn = eissn2.substring(0,4) + "-" + eissn2.substring(4,8);
					}
					if (eissn.equals("1816-210Х")){
						eissn = "1816-210X";
					}
				}
				line[3] = issn;
				line[5] = eissn;
				write_jnls.writeNext(line);
                write_jnls.flush();
			}
            read_jnls.close();
			in_jnls.close();
			write_jnls.close();
			out_jnls.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// reset the Issn value not matching standard format to empty  
	public void clean_issn3(String jnlsfile, String cleanedfile){
		try {
			String[] line;
			OutputStreamWriter out_jnls = new OutputStreamWriter(new FileOutputStream(cleanedfile), "UTF-8");
            CSVWriter write_jnls = new CSVWriter(out_jnls, ';', '"');
			InputStreamReader in_jnls = new InputStreamReader(new FileInputStream(jnlsfile), "UTF-8");
			CSVReader read_jnls = new CSVReader(in_jnls, ';', '"', 0);
			line = read_jnls.readNext();
			write_jnls.writeNext(line);
            write_jnls.flush();
            
			while ((line = read_jnls.readNext()) != null) {
				String issn = line[3].trim();
				String pissn = line[4].trim();
				String eissn = line[5].trim();
				if ((!(new Issn_methods().issnformat(issn)))&&(!issn.equals("978-31230-9-2"))){
					line[3] = "";
				}
				if ((!(new Issn_methods().issnformat(pissn))) && (!pissn.equals("E-only-title"))){
					line[4] = "";
				}
				if (!(new Issn_methods().issnformat(eissn))){
					line[5] = "";
				}
				write_jnls.writeNext(line);
                write_jnls.flush();
			}
            read_jnls.close();
			in_jnls.close();
			write_jnls.close();
			out_jnls.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Deduplicate journals (former 1journal) listed in a csv file (csvfile) and write the results in a new csv file (dudepfile)
	 * Merge condition: 2 journals have the same Issn Values and at least one similar title (level=1 in TitleCoparator().analyseTitle)
	 * For journal without Issn, it can be merged with another journal by an exact match of title
	 * 
	 * Columns in csvfile: source, idsource, title, issn, pissn, eissn, id1journal, subfield
	 * Columns in dedupfile: id1journal, issn, pissn, eissn, title, subfield
	 */
	public void csvdedup(String csvfile, String dedupfile){
		try{
			String line[];
			int line_noissn = 0;
            int line_nb = 0;
            TreeSet<Integer> lignevide = new TreeSet<Integer>();
            HashMultimap<String, String> issns_autres = HashMultimap.create();
			HashMultimap<String, String> issns_autres_id = HashMultimap.create();
			HashMultimap<String, String> noissns = HashMultimap.create();
			HashMultimap<String, String> noissns_id = HashMultimap.create();
			
			InputStreamReader in_journal = new InputStreamReader(new FileInputStream(csvfile), "UTF-8");
			CSVReader read_journal = new CSVReader(in_journal, ';', '"', 1);
			while ((line = read_journal.readNext()) != null) {
				line_nb++;
				String issn = line[3].trim();
				String pissn = line[4].trim();
				String eissn = line[5].trim();
				String title = line[2].trim();
				String subfield = line[7].trim();
				String id1journal = line[6].trim();
				boolean p_issn = (!issn.equals(""));
				boolean p_pissn = (!pissn.equals(""));
				boolean p_eissn = (!eissn.equals(""));
				if (p_issn || p_pissn || p_eissn){
					String issns = issn + "#" + pissn + "#" + eissn;
					issns_autres.put(issns, title + "#" + subfield);
					issns_autres_id.put(issns, title + "#" + subfield + "#" + id1journal);
				} else {
					line_noissn++;
					if (!title.equals("")){
						noissns.put(title, subfield);
						noissns_id.put(title, subfield + "#" + id1journal);
					} else {
						lignevide.add(line_nb);
					}
				}
			}
			read_journal.close();
			in_journal.close();
			System.out.println("	- nb de ligne dans 1journal liste: " + line_nb);
			System.out.println("	- nb de ligne (au moins 1 issn) differente dans 1journal liste: " + issns_autres.size());
			System.out.println("	- nb de ligne (au moins 1 issn) avec issns different: " + issns_autres.keySet().size());
			System.out.println("	- nb de ligne sans aucun issn: " + line_noissn);
			System.out.println("	- nb de ligne (sans issns) different: " + noissns.size());
			System.out.println("	- nb de title different sans aucun issn: " + noissns.keySet().size());
			System.out.println("nb lignes sans aucune information: " + lignevide.size());
			
			// merge the lines with same Issns (issn & pissn & eissn) and at least one similar title
			OutputStreamWriter out_journal = new OutputStreamWriter(new FileOutputStream(dedupfile), "UTF-8");
            CSVWriter write_journal = new CSVWriter(out_journal, ';', '"');
            String[] headline = {"id1journal", "issn", "p_issn", "e_issn", "titles", "subfields"};
            write_journal.writeNext(headline);
            write_journal.flush();
			HashMultimap<String, String> dedup = HashMultimap.create();
			for (String issns : issns_autres_id.keySet()){
				Set<String> autres = issns_autres_id.get(issns);
				List<Set<String>> ttls = new ArrayList<Set<String>>();
				List<String> sfds = new ArrayList<String>();
				List<String> ids = new ArrayList<String>();
				for (String autre : autres){
					String[] title_field = autre.split("#", -1);
					String[] title = title_field[0].split(";", -1);
					Set<String> ts = new HashSet<String>();
					for (int l=0; l<title.length; l++){
						String t = title[l].trim();
						if (t.length()>1){
							ts.add(t);
						}
					}
					ttls.add(ts);
					sfds.add(title_field[1]);
					ids.add(title_field[2]);
				}
				List<Set<Integer>> sames = new Disambiguation_methods().getSameJournal(ttls);
				for (int l=0; l<sames.size(); l++){
					Set<String> titles = new HashSet<String>();
					Set<String> subfields = new HashSet<String>();
					Set<String> id1journals = new HashSet<String>();
					for (int nb : sames.get(l)){
						titles.addAll(ttls.get(nb));
						subfields.add(sfds.get(nb));
						id1journals.add(ids.get(nb));
					}
					for (String title : titles){
						if (noissns_id.containsKey(title)){
							Set<String> sfds_ids = noissns_id.get(title);
							for (String sfd_id: sfds_ids){
								String[] sdfid = sfd_id.split("#", -1);
								subfields.add(sdfid[0]);
								id1journals.add(sdfid[1]);
							}
							noissns_id.removeAll(title);
						}
					}
					titles.remove("");
					subfields.remove("");
					String ts = String.join("#", titles.toArray(new String[0]));
					String ss = String.join("#", subfields.toArray(new String[0]));
					String id = String.join("#", id1journals.toArray(new String[0]));
					dedup.put(issns, ts + ";" + ss + ";" +id);
					
					String[] newline = new String[6];
					String[] is = issns.split("#", -1);
					newline[0] = id;
					newline[1] = is[0];
					newline[2] = is[1];
					newline[3] = is[2];
					newline[4] = ts;
					newline[5] = ss;
					write_journal.writeNext(newline);
	                write_journal.flush();
				}
			}
			
			// merge the lines without any Issn to the lines with Issn by exact match of title
			int nb_noissn = 0;
			for (String title : noissns_id.keySet()){
				Set<String> ss = noissns_id.get(title);
				Set<String> subfields = new HashSet<String>();
				Set<String> id1journals = new HashSet<String>();
				Iterator<String> s = ss.iterator();
				while(s.hasNext()){
					String[] subfield_id = s.next().split("#", -1);
					subfields.add(subfield_id[0]);
					id1journals.add(subfield_id[1]);
				}
				subfields.remove("");
				String sfd = String.join("#", subfields.toArray(new String[0]));
				String id = String.join("#", id1journals.toArray(new String[0]));
				nb_noissn++;
				String[] newline = new String[6];
				newline[0] = id;
				newline[1] = "";
				newline[2] = "";
				newline[3] = "";
				newline[4] = title;
				newline[5] = sfd;
				write_journal.writeNext(newline);
                write_journal.flush();
			}
			write_journal.close();
			out_journal.close();
			
			System.out.println("	- nb journals with issn arpes deduplication: " + dedup.size());
			System.out.println("	- nb de ligne different sans aucun issn apres deduplication: " + nb_noissn);
			System.out.println("	- nb de ligne total apres deduplication: " + (dedup.size() + nb_noissn));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// translate 1journal from csv file to jsonfile
	public void csvtojson(String csvfile, String jsonfile){
		try{
			String line[];
			String ligne;
			ObjectMapper mapper = new ObjectMapper();
			FileWriter writer = new FileWriter(jsonfile, false);
			InputStreamReader in_jnls = new InputStreamReader(new FileInputStream(csvfile), "UTF-8");
			CSVReader read_jnls = new CSVReader(in_jnls, ';', '"', 1);
			while ((line = read_jnls.readNext()) != null) {
				String[] id1journal = line[0].split("#", -1);
				String issn = line[1];
				String pissn = line[2];
				String eissn = line[3];
				String[] ttls = line[4].split("#", -1);
				String[] subfds = line[5].split("#", -1);
				Journal journal = new Journal();
				
				Set<String> source = new HashSet<String>();
				source.add("1journal");
				journal.setSource(source);
				
				Set<Id> ids = new HashSet<Id>();
				for(int i=0; i<id1journal.length; i++){
					ids.add(new Id("1journal", id1journal[i]));
				}
				journal.setIds(ids);
				
				Set<Issn> issns = new HashSet<Issn>();
				if (!issn.equals("")){
					issns.add(new Issn("ISSN", issn));
				}
				if (!pissn.equals("")){
					issns.add(new Issn("ISSN-print", pissn));
				}
				if (!eissn.equals("")){
					issns.add(new Issn("ISSN-electronic", eissn));
				}
				if (issns.size()>0){
					journal.setIssns(issns);
				}
				
				Set<String> titles = new HashSet<String>(Arrays.asList(ttls));
				titles.remove("");
				if (titles.size()>0){
					journal.setTitles(titles);
				}
				
				Set<String> subfields = new HashSet<String>(Arrays.asList(subfds));
				subfields.remove("");
				if (subfields.size()>0){
					journal.setSubfields(subfields);
				}
				
				Set<StatusPR> spr = new HashSet<StatusPR>(Arrays.asList(new StatusPR(StatusPR.Status.YES, "1journal")));
				journal.setStatusPR(spr);
				
				ligne  = mapper.writeValueAsString(journal);
				writer.append(ligne + "\n");
			}
			read_jnls.close();
			in_jnls.close();
			writer.flush();
			writer.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Deduplicate journals (former 1journal) listed in a json file (jsonfile) and write the results in a new json file (dudepfile)
	 * Merge condition: 2 journals have at least one common Issn Values and one similar title (level=1 in TitleCoparator().analyseTitle)
	 * Journal without Issn will not be treated
	 */
	public void jsondedup(String jsonfile, String dedupfile){
		try {
			List<Journal> jnls = new Jsonfile().readfile(jsonfile);
			int nb_iteration = 0;
			boolean samelist = false;
			while(!samelist){
				nb_iteration++;
				System.out.println("\n	- nb iteration: " + nb_iteration);
				
				HashMultimap<String, Journal> issn_journal = HashMultimap.create();
				HashMultimap<String, Integer> issn_nb = HashMultimap.create();
				HashMap<Integer, Journal> nb_journal = new HashMap<Integer, Journal>();
				for (int i=0; i<jnls.size(); i++){
					Journal journal = jnls.get(i);
					Set<String> issns = new Journal_methods().getIssnValues(journal);
					for (String issn : issns){
						issn_journal.put(issn, journal);
						issn_nb.put(issn, i);
						nb_journal.put(i, journal);
					}
				}
				System.out.println("	- nb journals: " + jnls.size());
				System.out.println("	- nb issn-journal: " + issn_nb.size());
				
				TreeSet<Integer> delectNb = new TreeSet<Integer>();
				for (String issn : issn_nb.keySet()){
					Set<Integer> nbs = issn_nb.get(issn);
					if (nbs.size()>=2){
						if (Sets.intersection(delectNb, nbs).size()==0){
							List<Journal> js = new ArrayList<Journal>();
							List<Integer> numbers = new ArrayList<Integer>();
							for (Integer nb : nbs){
								js.add(nb_journal.get(nb));
								numbers.add(nb);
							}
							List<Set<Integer>> sames = new Disambiguation_methods().sameJournal(js);
							for (int i=0; i<sames.size(); i++){
								if (sames.get(i).size()>1){
									List<Journal> samejournals = new ArrayList<Journal>();
									List<Integer> sameNbs = new ArrayList<Integer>();
									for (int j : sames.get(i)){
										samejournals.add(js.get(j));
										sameNbs.add(numbers.get(j));
									}
									delectNb.addAll(sameNbs);
									jnls.add(merge1journal(samejournals));
								}
							}
						}
					}
				}
				System.out.println("	- nb delected journals: " + delectNb.size());
				for (int nb : delectNb.descendingSet()){
					jnls.remove(nb);
				}
				System.out.println("	- nb journals apres deduplication: " + jnls.size());
				samelist = (delectNb.size()==0);		
			}
			new Jsonfile().writefile(dedupfile, jnls);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Deduplicate journals (former 1journal) listed in a json file (jsonfile) and write the results in a new json file (dudepfile)
	 * Merge condition: 2 journals have at least one common Issn Values and one similar title (level=1 in TitleCoparator().analyseTitle)
	 * For journal without Issn, it can be merged with another journal by title match (level=2 in TitleCoparator().analyseTitle)
	 * The merged journals between one journal without Issn and one journal with Issn will be listed by their titles in a txt file (deduptitle)
	 */
	public void jsondedupAll(String jsonfile, String dedupfile, String deduptitle){
		try {
			List<Journal> jnls = new Jsonfile().readfile(jsonfile);
			int nb_iteration = 0;
			boolean samelist = false;
			while(!samelist){
				nb_iteration++;
				System.out.println("\n	- nb iteration: " + nb_iteration);
				
				HashMultimap<String, Journal> issn_journal = HashMultimap.create();
				HashMultimap<String, Integer> issn_nb = HashMultimap.create();
				HashMap<Integer, Journal> nb_journal = new HashMap<Integer, Journal>();
				for (int i=0; i<jnls.size(); i++){
					Journal journal = jnls.get(i);
					Set<String> issns = new Journal_methods().getIssnValues(journal);
					for (String issn : issns){
						issn_journal.put(issn, journal);
						issn_nb.put(issn, i);
						nb_journal.put(i, journal);
					}
				}
				System.out.println("	- nb journals: " + jnls.size());
				System.out.println("	- nb issn-journal: " + issn_nb.size());
				
				TreeSet<Integer> delectNb = new TreeSet<Integer>();
				for (String issn : issn_nb.keySet()){
					Set<Integer> nbs = issn_nb.get(issn);
					if (nbs.size()>=2){
						if (Sets.intersection(delectNb, nbs).size()==0){
							List<Journal> js = new ArrayList<Journal>();
							List<Integer> numbers = new ArrayList<Integer>();
							for (Integer nb : nbs){
								js.add(nb_journal.get(nb));
								numbers.add(nb);
							}
							List<Set<Integer>> sames = new Disambiguation_methods().sameJournal(js);
							for (int i=0; i<sames.size(); i++){
								if (sames.get(i).size()>1){
									List<Journal> samejournals = new ArrayList<Journal>();
									List<Integer> sameNbs = new ArrayList<Integer>();
									for (int j : sames.get(i)){
										samejournals.add(js.get(j));
										sameNbs.add(numbers.get(j));
									}
									delectNb.addAll(sameNbs);
									jnls.add(merge1journal(samejournals));
								}
							}
						}
					}
				}
				System.out.println("	- nb delected journals: " + delectNb.size());
				for (int nb : delectNb.descendingSet()){
					jnls.remove(nb);
				}
				System.out.println("	- nb journals apres deduplication: " + jnls.size());
				samelist = (delectNb.size()==0);		
			}
			new Jsonfile().writefile(dedupfile, jnls);
			
			
			// merge the journals without any Issn to journals with Issn by title match
			List<Journal> jnls2 = new Jsonfile().readfile(dedupfile);
			FileWriter writer = new FileWriter(deduptitle, false);
			List<Journal> issn_jnls = new ArrayList<Journal>();
			List<Journal> noissn_jnls = new ArrayList<Journal>();
			TreeSet<Integer> delectNb = new TreeSet<Integer>();
			for (int i=0; i<jnls2.size(); i++){
				Journal journal = jnls2.get(i);
				if(new Journal_methods().getIssnValues(journal).size()>0){
					issn_jnls.add(journal);
				} else {
					noissn_jnls.add(journal);
				}
			}
			System.out.println("	- nb journals with issn: " + issn_jnls.size());
			System.out.println("	- nb journals without issn: " + noissn_jnls.size());
			
			for (int i=0; i<noissn_jnls.size(); i++){
				System.out.println(i);
				Set<String> ttls1 = noissn_jnls.get(i).getTitles();
				Set<String> id1journal1 = new Journal_methods().getIdValue(noissn_jnls.get(i), "1journal");
				boolean found = false;
				int j = 0;
				Set<String> ttls3 = new HashSet<String>();
				Set<String> issns = new HashSet<String>();
				
				while ((!found)&&(j<issn_jnls.size())){
					Set<String> ttls2 = issn_jnls.get(j).getTitles();
					ttls3.clear();
					for (String title : ttls2){
						ttls3.add(title);
					}
					issns = new Journal_methods().getIssnValues(issn_jnls.get(j));
					if ((ttls2.size()>0)&&(new Disambiguation_methods().similarTitle(ttls1, ttls2, 2))){
						ttls2.addAll(ttls1);
						issn_jnls.get(j).setTitles(ttls2);
						Set<String> sfds = issn_jnls.get(j).getSubfields();
						sfds.addAll(noissn_jnls.get(i).getSubfields());
						issn_jnls.get(j).setSubfields(sfds);
						
						Set<String> id1journal2 = new Journal_methods().getIdValue(issn_jnls.get(j), "1journal");
						id1journal2.addAll(id1journal1);
						Set<Id> ids = new HashSet<Id>(); 
						for (String id1journal : id1journal2){
							ids.add(new Id("1journal", id1journal));
						}
						issn_jnls.get(j).setIds(ids);
						delectNb.add(i);
						found = true;
					}	
					j++;
				}
				System.out.println(found);
				if (found){
					writer.append(issns + "\n");
					writer.append(ttls3 + "\n");
					writer.append(ttls1 + "\n");
					writer.append("\n");
					writer.flush();
					System.out.println(issn_jnls.get(j-1).getTitles());
					System.out.println(noissn_jnls.get(i).getTitles());
				}
			}
			
			System.out.println("nb delected: " + delectNb.size());
			System.out.println("nb noissn: " + noissn_jnls.size());
			System.out.println("nb jounral total: " + jnls2.size());
			for (int nb : delectNb.descendingSet()){
				noissn_jnls.remove(nb);
			}
			
			jnls2.clear();
			System.out.println("nb journals: " + jnls2.size());
			jnls2.addAll(issn_jnls);
			System.out.println("nb journals: " + jnls2.size());
			jnls2.addAll(noissn_jnls);
			System.out.println("nb journals: " + jnls2.size());
			
			System.out.println("	- nb journals without issn after: " + noissn_jnls.size());
			System.out.println("	- nb journals after: " + jnls2.size());
			writer.close();
			
			new Jsonfile().writefile(dedupfile, jnls2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//  separate the journals by Issn
	public void jsonduplicate(String jnslfile, String duplicatedfile){
		try {
			List<Journal> jnls = new Jsonfile().readfile(jnslfile);
			List<Journal> jnls2 = new ArrayList<Journal>();
			for (int i=0; i<jnls.size(); i++){
				Set<String> issns = new Journal_methods().getIssnValues(jnls.get(i));
				if (issns.size()>0){
					for (String issn : issns) {
						if (!issn.equals("E-only-title")){
							Journal journal = new Journal_methods().copyJournal(jnls.get(i));
							Set<Issn> iss = new HashSet<Issn>();
							iss.add(new Issn("ISSN", issn));
							journal.setIssns(iss);
							jnls2.add(journal);
						}	
					}
				} else {
					jnls2.add(jnls.get(i));
				}
			}
			System.out.println("nb journals in 1journal: " + jnls.size());
			System.out.println("nb issn_journal: " + jnls2.size());
			new Jsonfile().writefile(duplicatedfile, jnls2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// return a new journal which is the merged journal of 2 journals in 1journal list
	public Journal merge1journal(List<Journal> jnls){
		Journal journal = new Journal();
		
		Set<String> source = new HashSet<String>();
		source.add("1journal");
		journal.setSource(source);
		
		Set<StatusPR> spr = new HashSet<StatusPR>(Arrays.asList(new StatusPR(StatusPR.Status.YES, "1journal")));
		journal.setStatusPR(spr);
		
		Set<String> idvalues = new HashSet<String>();
		for (int i=0; i<jnls.size(); i++){
			idvalues.addAll(new Journal_methods().getIdValue(jnls.get(i), "1journal"));
		}
		Set<Id> ids = new HashSet<Id>();
		for (String idvalue : idvalues){
			ids.add(new Id("1journal", idvalue));
		}
		journal.setIds(ids);
		
		Set<Issn> issns = new HashSet<Issn>();
		for (int i=0; i<jnls.size(); i++){
			issns = new Journal_methods().mergeIssn(issns, jnls.get(i).getIssns());
		}
		journal.setIssns(issns);
		
		Set<String> titles = new HashSet<String>();
		for (int i=0; i<jnls.size(); i++){
			titles.addAll(jnls.get(i).getTitles());
		}
		journal.setTitles(titles);
		
		Set<String> subfields = new HashSet<String>();
		for (int i=0; i<jnls.size(); i++){
			subfields.addAll(jnls.get(i).getSubfields());
		}
		journal.setSubfields(subfields);
		return journal;
	}
	
	public boolean containsHanScript(String s) {
	    return s.codePoints().anyMatch(
	            codepoint ->
	            Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
	}
}
