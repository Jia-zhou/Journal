package com.onescience.journal.build1journal;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Sets;
import com.onescience.journal.methods.Disambiguation_methods;
import com.onescience.journal.methods.Journal_methods;
import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Id;
import com.onescience.journal.schema_journal.Issn;
import com.onescience.journal.schema_journal.Journal;
import com.onescience.journal.schema_journal.StatusPR;

public class Deduplication {
	
	public Deduplication(){
	}
	
	/**
	 * Deduplicate journals listed in a json file (jsonfile) and write the results in a new json file (dudepfile)
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
									jnls.add(mergejnls(samejournals));
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
	 * Deduplicate journals listed in a json file (jsonfile) and write the results in a new json file (dudepfile)
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
									jnls.add(mergejnls(samejournals));
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
	
	
	// return a new journal which is the merged journal of all journals in the journal list
	public Journal mergejnls(List<Journal> jnls){
		Set<String> sources = new HashSet<String>();
		Set<String> titles = new HashSet<String>();
		Set<String> subfields = new HashSet<String>();
		Set<Id> ids = new HashSet<Id>();
		Set<Issn> issns = new HashSet<Issn>();
		Set<StatusPR> statusprs = new HashSet<StatusPR>();
		for (int i=0; i<jnls.size(); i++){
			sources.addAll(jnls.get(i).getSource());
			titles.addAll(jnls.get(i).getTitles());
			subfields.addAll(jnls.get(i).getSubfields());
			ids.addAll(jnls.get(i).getIds());
			issns.addAll(jnls.get(i).getIssns());
			statusprs.addAll(jnls.get(i).getStatusPR());
		}
		Journal journal = new Journal();
		journal.setSource(sources);
		journal.setTitles(titles);
		journal.setSubfields(subfields);
		journal.setIds(new Journal_methods().dedupId(ids));
		journal.setIssns(new Journal_methods().dedupIssn(issns));
		journal.setStatusPR(new Journal_methods().dedupSPR(statusprs));
		return journal;
	}
}
