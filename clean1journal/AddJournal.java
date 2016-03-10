package com.onescience.journal.clean1journal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.onescience.journal.methods.Journal_methods;
import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Journal;
import com.opencsv.CSVReader;

public class AddJournal {

	public static void main(String[] args) {
		
		AddJournal ie = new AddJournal();
		
		//ie.analyseIssn();
		ie.importJournal();
		//ie.analyselinks();

	}
	
	
	
	
	
	
	
	public void analyseIssn(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String journalfile1 = prefix + "1journal_issn.json";
		String journalfile2 = prefix + "other_journal/others.json";
		
		try {
			List<Journal> jnls1 = new Jsonfile().readfile(journalfile1);
			List<Journal> jnls2 = new Jsonfile().readfile(journalfile2);
			Set<String> issns1 = new HashSet<String>();
			Set<String> issns2 = new HashSet<String>();
			for (int i=0; i<jnls1.size(); i++){
				Set<String> issn = new Journal_methods().getIssnValues(jnls1.get(i));
				for (String is : issn){
					issns1.add(is);
				}
			}
			for (int i=0; i<jnls2.size(); i++){
				Set<String> issn = new Journal_methods().getIssnValues(jnls2.get(i));
				for (String is : issn){
					issns2.add(is);
				}
			}
			
			System.out.println("nb issns in 1st file: " + issns1.size());
			System.out.println("nb issns in 2nd file: " + issns2.size());
			
			Set<String> same_issn = Sets.intersection(issns1, issns2);
			System.out.println("nb same issns: " + same_issn.size());
			
			/*
			for (String issn: same_issn){
				System.out.println(issn);
			}
			
			Set<String> diff_issn = Sets.difference(issns2, issns1);
			for (String issn: diff_issn){
				System.out.println(issn);
			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void importJournal(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String journalfile1 = "1journal_issn.json";
		String journalfile2 = "other_journal/others_deduplicated.json";
		String journalfile3 = "samejournal_manuelle.json";
		String newjournalfile = prefix + "newjournal_issn.json";
		
		try {
			List<String> filelist = new ArrayList<String>();
			filelist.add(prefix+journalfile1);
			filelist.add(prefix+journalfile2);
			filelist.add(prefix+journalfile3);
			
			List<Journal> journals = new Jsonfile().readfile(filelist);
			new Jsonfile().writefile(newjournalfile, journals);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void analyselinks(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String linkfile = prefix + "links.csv";
		
		try {
			String[] line;
			Multiset<String> m_issns = HashMultiset.create();
			Set<String> issn1 = new HashSet<String>();
			Set<String> issn2 = new HashSet<String>(); 
			int nb_links = 0;
			InputStreamReader in_link = new InputStreamReader(new FileInputStream(linkfile), "UTF-8");
			CSVReader read_link = new CSVReader(in_link, ';', '"', 0);
			while ((line = read_link.readNext()) != null) {
				nb_links++;
				m_issns.add(line[0]);
				m_issns.add(line[1]);
				issn1.add(line[0]);
				issn2.add(line[1]);
			}
			read_link.close();
			in_link.close();
			System.out.println("nb links: " + nb_links);
			System.out.println("nb issns: " + m_issns.size());
			System.out.println("nb issns differents: " + m_issns.elementSet().size());
			
			System.out.println(issn1.size());
			System.out.println(issn2.size());
			Set<String> sames = Sets.intersection(issn1, issn2);
			System.out.println(sames.size());
			/*
			for (String issn : m_issns.elementSet()){
				if (m_issns.count(issn)>1){
					System.out.println(issn + " -- " + m_issns.count(issn) + " fois");
				}
			}
			*/
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	


}
