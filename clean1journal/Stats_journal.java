package com.onescience.journal.clean1journal;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Sets;
import com.onescience.journal.methods.Journal_methods;
import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.*;

public class Stats_journal {

	public static void main(String[] args) {
		
		Stats_journal ie = new Stats_journal();
		ie.test2();
		/*
		String t1 = "AARN News  . Lett";
		String t2 = "AARN";
		String t3 = "AARN news, letter";
		String t4 = "AARN news&letter";
		System.out.println(new TitleAnalyse().getMots(t1));
		System.out.println(new TitleAnalyse().getMots(t2));
		System.out.println(new TitleAnalyse().getMots(t3));
		System.out.println(new TitleAnalyse().getMots(t4));
		
		Journal j = new Journal();
		j.setSource("1journal");
		System.out.println(j.getIssns());
		for (Issn issn : j.getIssns()){
			System.out.println("OK");
		}
		
		//List<String> s = Arrays.asList("a", "b", "c", "d");
		List<String> s = new ArrayList<String>();
		s.add("a");
		s.add("b");
		s.add("c");
		s.add("d");
		System.out.println(s);
		System.out.println(s.size());
		s.add("e");
		System.out.println(s.remove("c"));
		System.out.println(s);
		System.out.println(s.size());
		*/
		
		
	}
	
	public void test(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		String json1 = prefix + "1journal/1journal.json";
		String json2 = prefix + "Medjour/medjour.json";
		String result = prefix + "disambiguation_result.txt";
		
		HashMultimap<String, Journal> tousjnls = HashMultimap.create();
		
		try {
			List<Journal> jnls1 = new Jsonfile().readfile(json1);
			List<Journal> jnls2 = new Jsonfile().readfile(json2);
			for (int i=0; i<jnls1.size(); i++){
				Journal journal = jnls1.get(i);
				Set<String> issns = new Journal_methods().getIssnValues(journal);
				for (String issn : issns){
					tousjnls.put(issn, journal);
				}
			}
			for (int i=0; i<jnls2.size(); i++){
				Journal journal = jnls2.get(i);
				Set<String> issns = new Journal_methods().getIssnValues(journal);
				for (String issn : issns){
					tousjnls.put(issn, journal);
				}
			}
			System.out.println("nb lines in the 1st json file: " + jnls1.size());
			System.out.println("nb lines in the 2nd json file: " + jnls2.size());
			System.out.println("nb couple issn-journal: " + tousjnls.size());
			System.out.println("nb different issns: " + tousjnls.keySet().size());
			
			// affichier les groupes par issn
			int nb = 0;
			FileWriter writer = new FileWriter(result, false);
			for (String issn : tousjnls.keySet()){
				if (tousjnls.get(issn).size()>2){
					nb++;
					writer.append(issn + ": \n");
					for (Journal journal : tousjnls.get(issn)){
						writer.append(journal.getSource() + ": ");
						writer.append(journal.getTitles().toString() + "\n");
					}
				}
			}
			System.out.println("nb issns associated with au moins 2 journals: " + nb);
			writer.flush();
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void test2(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		List<String> jsonfiles = new ArrayList<String>();
		jsonfiles.add(prefix+"1journal/1journal.json");
		jsonfiles.add(prefix+"Medjour/medjour.json");
		String result = prefix + "disambiguation_result2.txt";
		
		HashMultimap<String, Journal> tousjnls = HashMultimap.create();
		
		try {
			List<Journal> jnls = new Jsonfile().readfile(jsonfiles);
			for (int i=0; i<jnls.size(); i++){
				Journal journal = jnls.get(i);
				Set<String> issns = new Journal_methods().getIssnValues(journal);
				for (String issn : issns){
					tousjnls.put(issn, journal);
				}
			}
			System.out.println("nb lines in 2 json files: " + jnls.size());
			System.out.println("nb couple issn-journal: " + tousjnls.size());
			System.out.println("nb different issns: " + tousjnls.keySet().size());
			
			// affichier les groupes par issn
			int nb = 0;
			FileWriter writer = new FileWriter(result, false);
			for (String issn : tousjnls.keySet()){
				if (tousjnls.get(issn).size()>2){
					nb++;
					writer.append(issn + ": \n");
					for (Journal journal : tousjnls.get(issn)){
						writer.append(journal.getSource() + ": ");
						writer.append(journal.getTitles().toString() + "\n");
					}
				}
			}
			System.out.println("nb issns associated with au moins 2 journals: " + nb);
			writer.flush();
			writer.close();
			
			
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
