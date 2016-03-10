package com.onescience.journal.clean1journal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.regex.Pattern;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onescience.journal.methods.Disambiguation_methods;
import com.onescience.journal.methods.Journal_methods;
import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.methods.TitleComparator;
import com.onescience.journal.schema_journal.*;

public class Deduplication0 {

	public static void main(String[] args) {
		String prefix = "/home/jia/Documents/travail/journal/";
		
		
		Deduplication0 ie = new Deduplication0();
		
		// deduplicate journals in 1journal list selon issn, or exact title match for no issn lines
		//ie.deduplicate();
		
		// transformer 1journal en format json
		//ie.tojson();
		
		// desambiguer journals in 1journal list dans ficher json
		ie.jsondeduplication();
		
		// display journal group with same issn
		//ie.seeresult();
		
		// compte id nombre
		//ie.compteNbId();
		
		
		// duplicate journals avec plusieur issns
		//ie.jsonduplicate();
		
		
		
		
		
	}
	
	


	public void deduplicate(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String journalfile = prefix + "journal_corrected.csv";
		String journal_deduplicated = prefix + "journal_deduplicated2.csv";
		
		try{
			HashMultimap<String, String> issns_autres = HashMultimap.create();
			HashMultimap<String, String> issns_autres_id = HashMultimap.create();
			HashMultimap<String, String> noissns = HashMultimap.create();
			HashMultimap<String, String> noissns_id = HashMultimap.create();
			
			String line[];
			int i = 0;
            int j = 0;
            TreeSet<Integer> lignevide = new TreeSet<Integer>();
			InputStreamReader in_journal = new InputStreamReader(new FileInputStream(journalfile), "UTF-8");
			CSVReader read_journal = new CSVReader(in_journal, ';', '"', 1);
			while ((line = read_journal.readNext()) != null) {
				j++;
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
					i++;
					if (!title.equals("")){
						noissns.put(title, subfield);
						noissns_id.put(title, subfield + "#" + id1journal);
					} else {
						lignevide.add(j);
					}
				}
			}
			read_journal.close();
			in_journal.close();
			
			System.out.println("	- nb de ligne dans 1journal liste: " + j);
			System.out.println("	- nb de ligne (au moins 1 issn) differente dans 1journal liste: " + issns_autres.size());
			System.out.println("	- nb de ligne (au moins 1 issn) avec issns different: " + issns_autres.keySet().size());
			System.out.println("	- nb de ligne sans aucun issn: " + i);
			System.out.println("	- nb de ligne (sans issns) different: " + noissns.size());
			System.out.println("	- nb de title different sans aucun issn: " + noissns.keySet().size());
			
			System.out.println("nb lignes sans aucune information: " + lignevide.size());
			
			
			// merger the lines with same issns: issn & pissn & eissn + title
			OutputStreamWriter out_journal = new OutputStreamWriter(new FileOutputStream(journal_deduplicated), "UTF-8");
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
			
			// merge the lines without any issn to the lines with issns according to the exact match of title
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
	
	
	
	public void tojson(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String csvfile = prefix + "journal_deduplicated.csv";
		String jsonfile = prefix + "1journal.json";
		
		try{
			String line[];
			String ligne;
			ObjectMapper mapper = new ObjectMapper();
			FileWriter writer = new FileWriter(jsonfile, false);
			InputStreamReader in_journal = new InputStreamReader(new FileInputStream(csvfile), "UTF-8");
			CSVReader read_journal = new CSVReader(in_journal, ';', '"', 1);
			while ((line = read_journal.readNext()) != null) {
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
			read_journal.close();
			in_journal.close();
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
	
	
	
	public void jsondeduplication(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		//String jsonfile = prefix + "1journal.json";
		//String dedupfile = prefix + "1journal_deduplicated.json";
		//String dedupfile2 = prefix + "1journal_deduplicated2.json";
		
		//String jsonfile = prefix + "other_journal/others.json";
		//String dedupfile = prefix + "other_journal/others_deduplicated.json";
		
		String jsonfile = prefix + "newjournal_issn.json";
		String dedupfile = prefix + "newjournal_deduplicated.json";
		
		try {
			
			boolean samelist = false;
			int nb_iteration = 0;
			List<Journal> jnls = new Jsonfile().readfile(jsonfile);
			
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
			
			/*
			List<Journal> jnls2 = new Jsonfile().readfile(dedupfile);
			FileWriter writer = new FileWriter((prefix + "dedup_noissn.txt"), false);
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
			new Jsonfile().writefile(dedupfile2, jnls2);
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void jsonduplicate(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String jsonfile = prefix + "1journal_deduplicated2.json";
		String duplicatedfile = prefix + "1journal_issn.json";
		
		try {
			List<Journal> jnls = new Jsonfile().readfile(jsonfile);
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
			System.out.println("nb journal in 1journal: " + jnls.size());
			System.out.println("nb issn_journal: " + jnls2.size());
			
			new Jsonfile().writefile(duplicatedfile, jnls2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public void seeresult(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		//String jsonfile = prefix + "1journal_deduplicated2.json";
		String jsonfile = prefix + "newjournal_normlang.json";
		String ambiguetitles = prefix + "ambiguetitles.txt";
		
		
		try {
			HashMultimap<String, Journal> issn_journal = HashMultimap.create();
			List<Journal> jnls = new Jsonfile().readfile(jsonfile);
			
			int nb_issn_journal = 0;
			int nb_noissn_journal = 0;
			for (int i=0; i<jnls.size(); i++){
				Journal journal = jnls.get(i);
				Set<String> issns = new Journal_methods().getIssnValues(journal);
				if (issns.size()>0){
					nb_issn_journal++;
				} else {
					nb_noissn_journal++;
				}
			}
			System.out.println("nb journals avec issn: " + nb_issn_journal);
			System.out.println("nb journals sans issns: " + nb_noissn_journal);
			System.out.println("nb journals: " + (nb_issn_journal+nb_noissn_journal));
			
			
			
			for (int i=0; i<jnls.size(); i++){
				Journal journal = jnls.get(i);
				Set<String> issns = new Journal_methods().getIssnValues(journal);
				for (String issn : issns){
					issn_journal.put(issn, journal);
				}
			}
			System.out.println("nb journals: " + jnls.size());
			System.out.println("nb issn-journal: " + issn_journal.size());
			
			FileWriter writer = new FileWriter(ambiguetitles, false);
			int nb = 0;
			for (String issn : issn_journal.keySet()){
				if (issn_journal.get(issn).size()>=2){
					nb++;
					//System.out.println("issn: " + issn);
					writer.append("issn: " + issn + "\n");
					for (Journal journal : issn_journal.get(issn)){
						//System.out.println(journal.getTitles());
						//writer.append(journal.getTitles() + "\n");
						writer.append(String.join("#", journal.getTitles().toArray(new String[0])) + "\n");
					}
					writer.append("\n");
				}
			}
			System.out.println("Group same issn: " + nb);
			writer.append("Group same issn: " + nb + "\n");
			writer.flush();
			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static Journal merge1journal(List<Journal> jnls){
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
	
	
	
	public void compteNbId(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String csvfile = prefix + "journal";
		String csvfile2 = prefix + "journal_corrected.csv";
		String csvfile3 = prefix + "journal_language.csv";
		String dedupcsv = prefix + "journal_deduplicated.csv";
		String jsonfile = prefix + "";
		
		try {
			String line[];
			Multiset<String> ids = HashMultiset.create();
			
			
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(csvfile3), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, ';', '"', 1);
			while ((line = read_csv.readNext()) != null) {
				ids.add(line[1].trim());
			}
			read_csv.close();
			in_csv.close();
			
			/*
			InputStreamReader in_csv2 = new InputStreamReader(new FileInputStream(dedupcsv), "UTF-8");
			CSVReader read_csv2 = new CSVReader(in_csv2, ';', '"', 1);
			while ((line = read_csv2.readNext()) != null) {
				String[] id = line[0].split("#", -1);
				for (int i=0; i<id.length; i++){
					ids.add(id[i]);
				}
			}
			read_csv2.close();
			in_csv2.close();
			*/
			
			ids.remove("");
			System.out.println("nb lignes: " + ids.size());
			System.out.println("nb ids: " + ids.elementSet().size());
			
			
			
			
			
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

}
