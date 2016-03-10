package com.onescience.journal.clean1journal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.onescience.journal.methods.Journal_methods;
import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Journal;
import com.onescience.utils.SetUtil;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class Addsubfields {

	public static void main(String[] args) {
		
		Addsubfields ie = new Addsubfields();
		//ie.analyseSubfieldsSMs();
		//ie.analyseSubfields1journal();
		//ie.importSubfields();
		ie.statsArticle();
		//ie.jsontocsv();
	}
	
	public void analyseSubfieldsSMs(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String sfdfile = prefix + "issns_subfields.csv";
		try {
			String[] line;
			HashMultimap<String, String> issn_sfd = HashMultimap.create();
			int nb_line = 0;
			int nb_issn_sfd = 0;
			InputStreamReader in_sfd = new InputStreamReader(new FileInputStream(sfdfile), "UTF-8");
			CSVReader read_sfd = new CSVReader(in_sfd, ';', '"', 0);
			while ((line = read_sfd.readNext()) != null) {
				nb_line++;
				if ((!line[2].equals(""))&&(!line[2].equals("UNCLASSIFIED"))){
					if (!line[0].equals("")){
						issn_sfd.put(line[0], line[2]);
						nb_issn_sfd++;
					}
					if (!line[1].equals("")){
						issn_sfd.put(line[1], line[2]);
						nb_issn_sfd++;
					}
				}
			}
			read_sfd.close();
			in_sfd.close();
			
			System.out.println("nb lines: " + nb_line);
			System.out.println("nb issn_subfield: " + nb_issn_sfd);
			System.out.println("nb issn_subfield different: " + issn_sfd.size());
			System.out.println("nb issns different: " + issn_sfd.keySet().size());
			
			int nb_pb = 0;
			for (String issn : issn_sfd.keySet()){
				Set<String> sfds = issn_sfd.get(issn);
				if (sfds.size()>2){
					nb_pb++;
					System.out.println(issn + ":" + sfds);
				}
			}
			System.out.println("nb problems: " + nb_pb);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void analyseSubfields1journal(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		//String jnlfile = prefix + "newjournal_normlang_complete.json";
		String jnlfile = prefix + "newjournal_lang_subfield.json";
		
		try {
			List<Journal> jnls = new Jsonfile().readfile(jnlfile);
			Set<String> setvide = new HashSet<String>();
			int nb_sfds = 0;
			int nb_remove = 0;
			int nb_null = 0;
			for (int i=0; i<jnls.size(); i++){
				Set<String> sfd = jnls.get(i).getSubfields();
				if(sfd.size()>0){
					nb_sfds++;
				}
				if(sfd.contains("")||sfd.contains("UNCLASSIFIED")){
					nb_null++;
				}
				if(sfd.size()>1){
					nb_remove++;
					System.out.println(new Journal_methods().getIssnValues(jnls.get(i)) + ":" + jnls.get(i).getSubfields() + ":" + jnls.get(i).getTitles());
					jnls.get(i).setSubfields(setvide);
				}
			}
			System.out.println("nb journals with subfields: " + nb_sfds);
			System.out.println("nb journals with empty string in subfields: " + nb_null);
			System.out.println("nb journals with more than 2 subfields: " + nb_remove);
			
			//new Jsonfile().writefile(jnlfile, jnls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void importSubfields(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String sfdfile = prefix + "issns_subfields.csv";
		String reffile = prefix + "subfield_manuelle";
		String jnlfile = prefix + "newjournal_normlang_complete.json";
		String sfdedfile = prefix + "newjournal_lang_subfield.json";
		
		try{
			String[] line;
			HashMap<String, String> issn_sfd = new HashMap<String, String>();
			InputStreamReader in_sfd = new InputStreamReader(new FileInputStream(sfdfile), "UTF-8");
			CSVReader read_sfd = new CSVReader(in_sfd, ';', '"', 0);
			while ((line = read_sfd.readNext()) != null) {
				if ((!line[2].equals(""))&&(!line[2].equals("UNCLASSIFIED"))){
					if (!line[0].equals("")){
						issn_sfd.put(line[0], line[2]);
					}
					if (!line[1].equals("")){
						issn_sfd.put(line[1], line[2]);
					}
				}
			}
			read_sfd.close();
			in_sfd.close();
			
			
			InputStreamReader in_ref = new InputStreamReader(new FileInputStream(reffile), "UTF-8");
			CSVReader read_ref = new CSVReader(in_ref, ':', '"', 0);
			while ((line = read_ref.readNext()) != null) {
				issn_sfd.remove(line[0]);
				issn_sfd.put(line[0], line[1]);
			}
			read_ref.close();
			in_ref.close();
			
			
			
			List<Journal> jnls = new Jsonfile().readfile(jnlfile);
			HashMultimap<String, Integer> issn_nb = HashMultimap.create();
			for (int i=0; i<jnls.size(); i++){
				Set<String> issns = new Journal_methods().getIssnValues(jnls.get(i));
				for (String issn : issns){
					issn_nb.put(issn, i);
				}
			}
			
			int nb_getsfd = 0;
			int nb_samesfd = 0;
			int nb_comflit = 0;
			for (String issn : issn_sfd.keySet()){
				Set<String> sfds = new HashSet<String>();
				sfds.add(issn_sfd.get(issn));
				Set<Integer> nbs = issn_nb.get(issn);
				for (int nb : nbs){
					Set<String> sfds1 = jnls.get(nb).getSubfields();
					if (sfds1.size()==0){
						jnls.get(nb).setSubfields(sfds);
						nb_getsfd++;
					} else {
						if (sfds.equals(sfds1)){
							nb_samesfd++;
						} else {
							jnls.get(nb).setSubfields(sfds);
							nb_comflit++;
						}
					}
				}
			}
			
			System.out.println("nb jounrals get subfield: " + nb_getsfd);
			System.out.println("nb jounrals with same subfield: " + nb_samesfd);
			System.out.println("nb jounrals with different subfield: " + nb_comflit);
			
			
			
			/*
			int nb_getsfd2 = 0;
			int nb_samesfd2 = 0;
			int nb_comflit2 = 0;
			int nb_nocomflit1 = 0;
			int nb_nocomflit2 = 0;
			int nb_pbs = 0;
			for (String issn : issn_sfd.keySet()){
				Set<String> sfds = issn_sfd.get(issn);
				Set<Integer> nbs = issn_nb.get(issn);
				for (int nb : nbs){
					Set<String> sfds1 = jnls.get(nb).getSubfields();
					if (sfds1.size()==0){
						jnls.get(nb).setSubfields(sfds);
						nb_getsfd2++;
					} else {
						if (sfds.equals(sfds1)){
							nb_samesfd2++;
						} else {
							nb_comflit2++;
							if (sfds1.containsAll(sfds)){
								nb_nocomflit1++;
								//System.out.println(issn + " : " + sfds + " .vs. " + sfds1);
							} else {
								if (sfds.containsAll(sfds1)){
									nb_nocomflit2++;
									//System.out.println(issn + " : " + sfds + " .vs. " + sfds1);
								} else {
									nb_pbs++;
									System.out.println(issn + " : " + sfds + " .vs. " + sfds1);
								}	
							}
							
						}
					}
					
				}
				
				
				
				
			}
			
			System.out.println("nb jounrals get subfield: " + nb_getsfd2);
			System.out.println("nb jounrals with same subfield: " + nb_samesfd2);
			System.out.println("nb jounrals with different subfield: " + nb_comflit2);
			System.out.println("nb jounrals with more subfields in 1journal: " + nb_nocomflit1);
			System.out.println("nb jounrals with less subfields in 1journal: " + nb_nocomflit2);
			System.out.println("nb jounrals with subfields completely different: " + nb_pbs);
			*/
			
			
			new Jsonfile().writefile(sfdedfile, jnls);
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public void statsArticle(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String sfdedfile = prefix + "newjournal_lang_subfield.json";
		String occfile = prefix + "1db_issns_occ.csv";
		String sdfoccfile = prefix + "issns_occ_subfields.csv";
		String nosdffile = prefix + "issns_occ_nodsfds.txt";
		
		try {
			String[] line;
			int nb_articles = 0;
			HashMap<String, Double> issn_occ = new HashMap<String, Double>();
			InputStreamReader in_occ = new InputStreamReader(new FileInputStream(occfile), "UTF-8");
			CSVReader read_occ = new CSVReader(in_occ, ',', '"', 0);
			while ((line = read_occ.readNext()) != null) {
				int nb_atls = Integer.parseInt(line[1].trim());
				nb_articles = nb_articles + nb_atls;
				if (!line[0].equals("")){
					String[] issns = line[0].split("#", -1);
					double m = nb_atls*1.0/issns.length;
					for (int i=0; i<issns.length; i++){
						if (!issn_occ.containsKey(issns[i])){
							issn_occ.put(issns[i], m);
						} else {
							double valeur = issn_occ.get(issns[i]);
							issn_occ.remove(issns[i]);
							issn_occ.put(issns[i], valeur+m);
						}
					}
				}
			}
			read_occ.close();
			in_occ.close();
			issn_occ = new SetUtil().triAvecValeur(issn_occ);
			
			
			List<Journal> jnls = new Jsonfile().readfile(sfdedfile);
			HashMultimap<String, Integer> issn_nb = HashMultimap.create();
			for (int i=0; i<jnls.size(); i++){
				Set<String> issns = new Journal_methods().getIssnValues(jnls.get(i));
				for (String issn : issns){
					issn_nb.put(issn, i);
				}
			}
			
			double nb_articles1 = 0.0;
			for (String issn : issn_occ.keySet()){
				Set<Integer> nbs = issn_nb.get(issn);
				if (nbs.size()>0){
					nb_articles1 = nb_articles1 + issn_occ.get(issn);
				}
			}
			
			double cumul = 0.0;
			double cumul2 = 0;
			FileWriter writer = new FileWriter(sdfoccfile, false);
			FileWriter writer2 = new FileWriter(nosdffile, false);
			for (String issn : issn_occ.keySet()){
				Set<Integer> nbs = issn_nb.get(issn);
				double pourcentage = issn_occ.get(issn)*100/nb_articles;
				if (nbs.size()==0){
					writer.append(issn + ";" + issn_occ.get(issn) + ";;" + pourcentage + ";not in 1journal\n");
					writer.flush();
				} else {
					double pourcentage1 = issn_occ.get(issn)*100/nb_articles1;
					cumul = cumul + pourcentage1;
					Set<String> subfields = new HashSet<String>();
					for (int nb : nbs){
						subfields.addAll(jnls.get(nb).getSubfields());
					}
					subfields.remove("");
					if (subfields.size()==0){
						cumul2 = cumul2 + pourcentage1;
						writer2.append(issn + ";" + issn_occ.get(issn) + ";" + cumul2 + "\n");
						writer2.flush();
					}
					writer.append(issn + ";" + issn_occ.get(issn) + ";" + subfields + ";" + pourcentage + ";" + pourcentage1 + ";" + cumul + "\n");
					writer.flush();
				}
			}
			writer.close();
			writer2.close();
			
			
			System.out.println("nb articles: " + nb_articles);
			System.out.println("nb articles in 1journal: " + nb_articles1);
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public void jsontocsv(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String sfdedfile = prefix + "newjournal_lang_subfield.json";
		String csvfile = prefix + "newjournal_lang_subfield.csv";
		
		try {
			List<Journal> jnls = new Jsonfile().readfile(sfdedfile);
			OutputStreamWriter out_csv = new OutputStreamWriter(new FileOutputStream(csvfile), "UTF-8");
            CSVWriter write_csv = new CSVWriter(out_csv, '$', CSVWriter.NO_QUOTE_CHARACTER);
            for (int i=0; i<jnls.size(); i++){
            	Set<String> issns = new Journal_methods().getIssnValues(jnls.get(i));
            	Set<String> titles = jnls.get(i).getTitles();
            	titles.remove("");
            	for (String issn : issns){
            		for (String title : titles){
            			String[] newline = new String[5];
            			newline[0] = Integer.toString(i+1);
            			newline[1] = issn;
            			newline[2] = title;
            			newline[3] = String.join("#", jnls.get(i).getSubfields().toArray(new String[0]));
            			newline[4] = String.join("#", jnls.get(i).getTextLanguages().toArray(new String[0]));
            			write_csv.writeNext(newline);
    	                write_csv.flush();
            		}
            	}
            }
            write_csv.close();
			out_csv.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
}
