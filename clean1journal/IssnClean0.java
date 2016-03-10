package com.onescience.journal.clean1journal;

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
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.onescience.journal.methods.Issn_methods;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class IssnClean0 {

	public static void main(String[] args) {
		
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		
		IssnClean0 ie = new IssnClean0();
		
		// correct the character special
		//ie.clean_1journal();
		
		// correct the issn errors dans 1journal list and build a issn list
		//ie.build_1journal();
		
		// test the consistency between issn, eissn, pissn in 1journal list
		//ie.issn_test();
		

	}
	
	
	public void clean_1journal(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/steps/";
		String journal = prefix + "journal";
		String journal_cleaned = prefix + "journal_cleaned";
		String caracteres = prefix + "replacement_caractere.txt";
		
		try {
			String ligne;
			List<String> caracs = new ArrayList<String>();
			List<String> replace = new ArrayList<String>();
			BufferedReader read_caracs = new BufferedReader(new FileReader(new File(caracteres)));
			while ((ligne = read_caracs.readLine()) != null) {
				String[] replaces = ligne.split("-", -1);
				caracs.add(replaces[0]);
				replace.add(replaces[1]);
			}
			read_caracs.close();
			
			FileWriter writer = new FileWriter(journal_cleaned, true);
			BufferedReader reader = new BufferedReader(new FileReader(new File(journal)));
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
	
	
	
	public void build_1journal(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/steps/";
		
		String issn_anormal0 = prefix + "journal_issn_anormal0.txt";
		String journal_corrected1 = prefix + "journal_corrected1.csv";
		String issn_anormal1 = prefix + "journal_issn_anormal1.txt";
		String journal_corrected2 = prefix + "journal_corrected2.csv";
		String issn_anormal2 = prefix + "journal_issn_anormal2.txt";
		String journal_corrected3 = prefix + "journal_corrected3.csv";
		String issn_anormal3 = prefix + "journal_issn_anormal3.txt";
		
		String prefix2 = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String journal = prefix2 + "journal_cleaned";
		String issn_list = prefix2 + "journal_issns.txt";
		
		try {
			String ligne;
			String line[];
			
			
			/*
			// build the wrong issn list
			Multiset<String> m_issn = HashMultiset.create();
			Multiset<String> m_pissn = HashMultiset.create();
			Multiset<String> m_eissn = HashMultiset.create();
			//FileWriter write_issn = new FileWriter(issn_anormal0, true);
			//InputStreamReader in_journal = new InputStreamReader(new FileInputStream(journal), "UTF-8");
			//FileWriter write_issn = new FileWriter(issn_anormal1, true);
			//InputStreamReader in_journal = new InputStreamReader(new FileInputStream(journal_corrected1), "UTF-8");
			//FileWriter write_issn = new FileWriter(issn_anormal2, true);
			//InputStreamReader in_journal = new InputStreamReader(new FileInputStream(journal_corrected2), "UTF-8");
			FileWriter write_issn = new FileWriter(issn_anormal3, true);
			InputStreamReader in_journal = new InputStreamReader(new FileInputStream(journal_corrected3), "UTF-8");
			CSVReader read_journal = new CSVReader(in_journal, ';', '"', 1);
			while ((line = read_journal.readNext()) != null) {
				String issn = line[3].trim();
				String pissn = line[4].trim();
				String eissn = line[5].trim();
				
				if ((!(new Issn_analyse().issnformat(issn)))&&(!issn.equals("null"))&&(!issn.equals(""))){
					if(!m_issn.contains(issn)){
						//write_issn.append("  issn:  " + issn + "  ---  eissn: " + eissn + "  ---  pissn: " + pissn + "\n");
						write_issn.append("  issn:  " + issn + "\n");
					}
					m_issn.add(issn);
				}
				if ((!(new Issn_analyse().issnformat(pissn)))&&(!pissn.equals("NULL"))&&(!pissn.equals(""))&&(!pissn.equals("E-only-title"))){
					if(!m_pissn.contains(pissn)){
						//write_issn.append("p_issn:  " + pissn + "  ---  issn: " + issn + "  ---  eissn: " + eissn + "\n");
						write_issn.append("p_issn:  " + pissn + "\n");
					}
					m_pissn.add(pissn);
				}
				if ((!(new Issn_analyse().issnformat(eissn)))&&(!eissn.equals("NULL"))&&(!eissn.equals(""))){
					if(!m_eissn.contains(eissn)){
						//write_issn.append("e_issn:  " + eissn + "  ---  issn: " + issn + "  ---  pissn: " + pissn + "\n");
						write_issn.append("e_issn:  " + eissn + "\n");
					}
					m_eissn.add(eissn);
				}
				
			}
			read_journal.close();
			in_journal.close();
			write_issn.flush();
			write_issn.close();
			*/
			
			
			// correct the issn in 1journal list
			OutputStreamWriter out_journal1 = new OutputStreamWriter(new FileOutputStream(journal_corrected1), "UTF-8");
            CSVWriter write_journal1 = new CSVWriter(out_journal1, ';', '"');
			InputStreamReader in_journal0 = new InputStreamReader(new FileInputStream(journal), "UTF-8");
			CSVReader read_journal0 = new CSVReader(in_journal0, ';', '"', 0);
			line = read_journal0.readNext();
			write_journal1.writeNext(line);
            write_journal1.flush();
            
            // copier issn print dans p_issn et issn online dans e_issn
            while ((line = read_journal0.readNext()) != null) {
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
            	write_journal1.writeNext(line);
                write_journal1.flush();
            }
            read_journal0.close();
			in_journal0.close();
			write_journal1.close();
			out_journal1.close();
			
			
            
            // reformater issn au format standard
            OutputStreamWriter out_journal2 = new OutputStreamWriter(new FileOutputStream(journal_corrected2), "UTF-8");
            CSVWriter write_journal2 = new CSVWriter(out_journal2, ';', '"');
			InputStreamReader in_journal1 = new InputStreamReader(new FileInputStream(journal_corrected1), "UTF-8");
			CSVReader read_journal1 = new CSVReader(in_journal1, ';', '"', 0);
			line = read_journal1.readNext();
			write_journal2.writeNext(line);
            write_journal2.flush();
            
			while ((line = read_journal1.readNext()) != null) {
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
				write_journal2.writeNext(line);
                write_journal2.flush();
			}
            read_journal1.close();
			in_journal1.close();
			write_journal2.close();
			out_journal2.close();
			
			
			
			// remise les issns non formates a vide
			HashMultimap<String, String> hm = HashMultimap.create();
			OutputStreamWriter out_journal3 = new OutputStreamWriter(new FileOutputStream(journal_corrected3), "UTF-8");
            CSVWriter write_journal3 = new CSVWriter(out_journal3, ';', '"');
			InputStreamReader in_journal2 = new InputStreamReader(new FileInputStream(journal_corrected2), "UTF-8");
			CSVReader read_journal2 = new CSVReader(in_journal2, ';', '"', 0);
			line = read_journal2.readNext();
			write_journal3.writeNext(line);
            write_journal3.flush();
            
			while ((line = read_journal2.readNext()) != null) {
				String issn = line[3].trim();
				String pissn = line[4].trim();
				String eissn = line[5].trim();
				
				if ((!(new Issn_methods().issnformat(issn)))&&(!issn.equals("978-31230-9-2"))){
					line[3] = "";
					hm.put("issn", issn);
				}
				if ((!(new Issn_methods().issnformat(pissn))) && (!pissn.equals("E-only-title"))){
					line[4] = "";
					hm.put("pissn", pissn);
				}
				if (!(new Issn_methods().issnformat(eissn))){
					line[5] = "";
					hm.put("eissn", eissn);
				}
				write_journal3.writeNext(line);
                write_journal3.flush();
			}
            read_journal2.close();
			in_journal2.close();
			write_journal3.close();
			out_journal3.close();
			
			System.out.println("issns: " + hm.get("issn").size());
			System.out.println("pissns: " + hm.get("pissn").size());
			System.out.println("eissns: " + hm.get("eissn").size());
			System.out.println("issn: " + hm.get("issn"));
			System.out.println("pissn: " + hm.get("pissn"));
			System.out.println("eissn: " + hm.get("eissn"));
			
			
			
			/*
			// build a issn list
			Multiset<String> m_issn = HashMultiset.create();
			FileWriter write_issns = new FileWriter(issn_list, false);
			InputStreamReader in_issn = new InputStreamReader(new FileInputStream(journal_corrected3), "UTF-8");
			CSVReader read_issn = new CSVReader(in_issn, ';', '"', 1);
			while ((line = read_issn.readNext()) != null) {
				String issn = line[3];
				String pissn = line[4];
				String eissn = line[5];
				if (!issn.equals("")){
					if (!m_issn.contains(issn)){
						write_issns.append(issn + "\n");
					}
					m_issn.add(issn);
				}
				
				if ((!pissn.equals(""))&&(!pissn.equals("E-only-title"))){
					if (!m_issn.contains(pissn)){
						write_issns.append(pissn + "\n");
					}
					m_issn.add(pissn);
				}
				if (!eissn.equals("")){
					if (!m_issn.contains(eissn)){
						write_issns.append(eissn + "\n");
					}
					m_issn.add(eissn);
				}
			}
			read_issn.close();
			in_issn.close();
			write_issns.flush();
			write_issns.close();
			System.out.println("Nb valided issns: " + m_issn.size());
			System.out.println("Nb different issns: " + m_issn.elementSet().size());	
			*/
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void issn_test(){
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String journal = prefix + "journal_corrected.csv";
		try{
			String line[];
			int nb_issn_eissn = 0;
			int pb_issn_eissn = 0;
			int nb_issn_pissn = 0;
			int pb_issn_pissn = 0;
			int nb_noissn = 0;
			InputStreamReader in_journal = new InputStreamReader(new FileInputStream(journal), "UTF-8");
			CSVReader read_journal = new CSVReader(in_journal, ';', '"', 1);
			while ((line = read_journal.readNext()) != null) {
				String issn = line[3].trim();
				String pissn = line[4].trim();
				String eissn = line[5].trim();
				String issns = "issn: " + issn + "; pissn: " + pissn + "; eissn: " + eissn; 
				boolean p_issn = (!issn.equals(""))&&(!issn.equals("null"))&&(!issn.equals("-"));
				boolean p_pissn = (!pissn.equals(""))&&(!pissn.equals("NULL"));//&&(!pissn.equals("E-only-title"));
				boolean p_eissn = (!eissn.equals(""))&&(!eissn.equals("NULL"));
				
				if (pissn.equals("E-only-title")){
					nb_issn_eissn++;
					boolean same = issn.equals(eissn);
					if (!same){
						pb_issn_eissn++;
						System.out.println(pb_issn_eissn + "--" + issns + " ----- " + same);
					}	
				}
				if (p_issn && p_pissn){
					nb_issn_pissn++;
					if ((!issn.equals(pissn))){
						pb_issn_pissn++;
						System.out.println(pb_issn_pissn + "--" + issns);
					}
				}
				if ((!p_issn) && (!p_pissn) && (!p_eissn)){
					nb_noissn++;
				}
			}
			System.out.println("nb journals with pissn = E-only-title: " + nb_issn_eissn + " -- pb: issn different de eissn: " + pb_issn_eissn);
			System.out.println("nb journals with issn and pissn: " + nb_issn_pissn + " -- pb: issn different de pissn: " + pb_issn_pissn);
			System.out.println("nb journals without issn: " + nb_noissn);
			read_journal.close();
			in_journal.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static boolean containsHanScript(String s) {
	    return s.codePoints().anyMatch(
	            codepoint ->
	            Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
	}

}
