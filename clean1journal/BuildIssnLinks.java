package com.onescience.journal.clean1journal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.onescience.utils.SetUtil;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class BuildIssnLinks {

	public static void main(String[] args) {
		
		BuildIssnLinks ie = new BuildIssnLinks();
		ie.buildlinks();
		
		
	}
	
	// build a list of issn_couple who appear together usually
	public void buildlinks(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		String sm1file = prefix + "SM1/sm1_issn_eissn_occ.csv";
		String medlinefile = prefix + "Pubmed/medline_issn_occ.csv";
		String linkfile = prefix + "Thesaurus/issn_link.csv";
		
		try{
			String[] line;
			
			// read issn occurrence in SM1
			HashMap<String, HashMap<String, Integer>> issn_occ = new HashMap<String, HashMap<String, Integer>>();
			InputStreamReader in_sm1 = new InputStreamReader(new FileInputStream(sm1file), "UTF-8");
			CSVReader read_sm1 = new CSVReader(in_sm1, '\t', CSVWriter.NO_QUOTE_CHARACTER, 1);
			while ((line = read_sm1.readNext()) != null) {
				String issn = line[0];
				int occ = Integer.parseInt(line[2]);
				if (!issn.equals("")){
					HashMap<String, Integer> occs = new HashMap<String, Integer>();
					if (!issn.equals("issn")){
						if (line[1].equals("")){
							occs.put(issn, occ);
						} else {
							occs.put(line[1], occ);
						}
					} else {
						if (!line[1].equals("")){
							issn = line[1];
							occs.put(issn, occ);
						}
					}
					
					if (issn_occ.containsKey(issn)){
						HashMap<String, Integer> occs0 = issn_occ.get(issn);
						issn_occ.remove(issn);
						issn_occ.put(issn, new SetUtil().mergeStringNb(occs0, occs));
					} else {
						issn_occ.put(issn, occs);
					}
				}
			}
			read_sm1.close();
			in_sm1.close();
			
			// read issn occurrence in Medline
			InputStreamReader in_med = new InputStreamReader(new FileInputStream(medlinefile), "UTF-8");
			CSVReader read_med = new CSVReader(in_med, ':', CSVWriter.NO_QUOTE_CHARACTER, 0);
			while ((line = read_med.readNext()) != null) {
				String[] issns = line[0].split("#", -1);
				String issn = issns[0];
				int occ = Integer.parseInt(line[1]);
				if (!issn.equals("")){
					HashMap<String, Integer> occs = new HashMap<String, Integer>();
					if (issns.length==1){
						occs.put(issn, occ);
					} else {
						occs.put(issns[1], occ);
					}
					if (issn_occ.containsKey(issn)){
						HashMap<String, Integer> occs0 = issn_occ.get(issn);
						issn_occ.remove(issn);
						issn_occ.put(issn, new SetUtil().mergeStringNb(occs0, occs));
					} else {
						issn_occ.put(issn, occs);
					}
				}
			}
			read_med.close();
			in_med.close();
			
			
			
			FileWriter writer = new FileWriter(linkfile, false);
			HashMultimap<String, String> couple = HashMultimap.create();
			for (String issn : issn_occ.keySet()){
				Set<String> links = issn_occ.get(issn).keySet();
				links.remove(issn);
				for (String issn2 : links){
					couple.put(issn, issn2);
					couple.put(issn2, issn);
				}
			}
			
			Set<String> issns = new HashSet<String>();
			for (String issn : couple.keySet()){
				if (!issns.contains(issn)){
					Set<String> links = couple.get(issn);
					if (links.size()==1){
						String issn2 = links.iterator().next();
						Set<String> links2 = couple.get(issn2);
						if (links2.size()==1){
							/*
							int occ11 = 0;
							int occ12 = 0;
							int occ21 = 0;
							int occ22 = 0;
							if(issn_occ.containsKey(issn)){
								HashMap<String, Integer> list1 = issn_occ.get(issn);
								if (list1.containsKey(issn)){
									occ11 = list1.get(issn);
								}
								if (list1.containsKey(issn2)){
									occ12 = list1.get(issn2);
								}
							}
							if(issn_occ.containsKey(issn2)){
								HashMap<String, Integer> list2 = issn_occ.get(issn2);
								if (list2.containsKey(issn)){
									occ21 = list2.get(issn);
								}
								if (list2.containsKey(issn2)){
									occ22 = list2.get(issn2);
								}
							}
							writer.append(issn + ";" + issn2 + ";" + (occ12+occ21)*1.0/(occ11+occ12+occ21) + ";" + (occ12+occ21)*1.0/(occ22+occ12+occ21) + "\n");
							writer.flush();
							*/
							writer.append(issn + ";" + issn2 + "\n");
							writer.flush();
							issns.add(issn);
							issns.add(issn2);
						}
					}
				}
			}
			writer.close();
			System.out.println("nb issns: " + issns.size());
			
			/*
			// display the issns which match with more than 1 other issn
			for (String issn : issn_occ.keySet()){
				issn_occ.get(issn).remove(issn);
			}
			
			for (String issn : issn_occ.keySet()){
				if (issn_occ.get(issn).size()>1){
					//if (issn_occ.get(issn).entrySet().iterator().next().getValue()>2000){
						System.out.println(issn + ": " + issn_occ.get(issn));
					//}
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
