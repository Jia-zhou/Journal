package com.onescience.journal.dataSources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;

public class BuildIssn {

	public static void main(String[] args) {
		
		BuildIssn ie = new BuildIssn();
		//ie.build_issns();
		ie.issns_analyse();

	}
	
	

	public void build_issns(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		String list1 = prefix + "1journal/journal_issns.txt";
		String list2 = prefix + "worldcat/Journal_issns.txt";
		String issn_list = prefix + "issns.txt";
		
		Multiset<String> m_issn = HashMultiset.create();
		
		try{
			String ligne;
			FileWriter writer = new FileWriter(issn_list, false);
			for (String file : Arrays.asList(list1, list2)){
				BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
				while ((ligne = reader.readLine()) != null) {
					if (!m_issn.contains(ligne)){
						writer.append(ligne + "\n");
					}
					m_issn.add(ligne);
				}
				reader.close();
			}
			writer.flush();
			writer.close();
			System.out.println("Nb valided issns: " + m_issn.size());
			System.out.println("Nb different issns: " + m_issn.elementSet().size());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void issns_analyse() {
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		String issns1 = prefix + "1journal/journal_issns.txt";
		String issns2 = prefix + "DOAJ/doaj_issns.txt";
		
		try {
			String ligne;
			Set<String> s1 = new HashSet<String>();
			BufferedReader reader1 = new BufferedReader(new FileReader(new File(issns1)));
			while ((ligne = reader1.readLine()) != null) {
				s1.add(ligne);
			}
			reader1.close();
			
			Set<String> s2 = new HashSet<String>();
			BufferedReader reader2 = new BufferedReader(new FileReader(new File(issns2)));
			while ((ligne = reader2.readLine()) != null) {
				s2.add(ligne);
			}
			reader2.close();
			
			System.out.println("nb issns dans 1journal: " + s1.size());
			System.out.println("nb issns dans DOAJ: " + s2.size());
			System.out.println("nb issns commons: " + Sets.intersection(s1, s2).size());
			System.out.println("nb issns nouveaux dans DOAJ: " + Sets.difference(s2, s1).size());
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}

}
