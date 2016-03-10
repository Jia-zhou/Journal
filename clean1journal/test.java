package com.onescience.journal.clean1journal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.opencsv.CSVReader;

public class test {

	public static void main(String[] args) {
		
		test ie = new test();
		ie.testissn();

	}
	
	public void testissn(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String file1 = prefix + "compare_lang_prob.csv";
		String file2 = prefix + "standard/manual_language.csv";
		
		try {
			String[] line;
			int nb_line = 0;
			HashMap<String, Integer> issn1 = new HashMap<String, Integer>();
			InputStreamReader in_file1 = new InputStreamReader(new FileInputStream(file1), "UTF-8");
			CSVReader read_file1 = new CSVReader(in_file1, ':', '"', 0);
			while ((line = read_file1.readNext()) != null) {
				nb_line++;
				issn1.put(line[0], nb_line);
			}
			read_file1.close();
			in_file1.close();
			
			Set<String> issn2 = new HashSet<String>();
			InputStreamReader in_file2 = new InputStreamReader(new FileInputStream(file2), "UTF-8");
			CSVReader read_file2 = new CSVReader(in_file2, ';', '"', 0);
			while ((line = read_file2.readNext()) != null) {
				issn2.add(line[0]);
			}
			read_file2.close();
			in_file2.close();
			
			
			Set<String> sameissn = Sets.intersection(issn1.keySet(), issn2);
			System.out.println("nb issn in file1: " + issn1.size());
			System.out.println("nb issn in file2: " + issn2.size());
			System.out.println("nb issn common: " + sameissn.size());
			for (String issn : sameissn){
				System.out.println(issn + " - " + issn1.get(issn));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
