package com.onescience.journal.dataSources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.onescience.journal.methods.Issn_methods;
import com.opencsv.CSVReader;

public class DataWorldcat {
	
	public DataWorldcat(){
	}
	
	// merge 2 csv files into a single csv file
	public void mergecsvfile(String csvfile1, String csvfile2, String worldcatfile){
		try {
			String ligne;
			FileWriter write_file = new FileWriter(worldcatfile, false);
			for (String wfile : Arrays.asList(csvfile1, csvfile2)){
				BufferedReader txtreader = new BufferedReader(new FileReader(new File(wfile)));
				while ((ligne = txtreader.readLine()) != null) {
					write_file.append(ligne.trim() + "\n");
					write_file.flush();
				}
				txtreader.close();
			}
			write_file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// diaplay the issn not correct from worldcat csv
	public void issn_anormal(String worldcatfile, String issn_anormal){
		try {
			String ligne;
			FileWriter write_anormal = new FileWriter(issn_anormal, false);
			BufferedReader txtreader = new BufferedReader(new FileReader(new File(worldcatfile)));
			while ((ligne = txtreader.readLine()) != null) {
				String issn = ligne.split("#", -1)[1];
				if (!(new Issn_methods().issnformat(issn))){
					Pattern pattern = Pattern.compile("\\d{7}(\\d|x|X)");
					if (!pattern.matcher(issn).matches()){
						write_anormal.append(issn + "\n");
						write_anormal.flush();
					}
				}
			}
			txtreader.close();
			write_anormal.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// correct some issn to standard format in worldcat list
	public void correctlist(String worldcatfile, String issn_correction, String correctedfile){
		try {
			String ligne;
			HashMap<String, String> correction = new HashMap<String, String>();
			BufferedReader correction_reader = new BufferedReader(new FileReader(new File(issn_correction)));
			while ((ligne = correction_reader.readLine()) != null) {
				String issn = ligne.split(";", -1)[1];
				if (new Issn_methods().issnformat(issn)){
					correction.put(ligne.split(";", -1)[0], issn);
				}
			}
			correction_reader.close();
			
			FileWriter write_correct = new FileWriter(correctedfile, false);
			BufferedReader file_reader = new BufferedReader(new FileReader(new File(worldcatfile)));
			while ((ligne = file_reader.readLine()) != null) {
				String issn = ligne.split("#", -1)[1];
				if (!(new Issn_methods().issnformat(issn))){
					Pattern pattern = Pattern.compile("\\d{7}(\\d|x|X)");
					if (pattern.matcher(issn).matches()){
						issn = issn.substring(0, 4) + "-" + issn.substring(4, 8);
					} else{
						if (correction.get(issn)!=null){
							issn = correction.get(issn);
						}
					}
				}
				write_correct.append(ligne.split("#", -1)[0] + "#" + issn + "\n");
			}
			file_reader.close();
			write_correct.flush();
			write_correct.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// build the issn list from worldcat list
	public void issnlist(String worldcatfile, String issn_list){
		try {
			String[] line;
			Multiset<String> m_issn = HashMultiset.create();
			FileWriter write_issns = new FileWriter(issn_list, false);
			InputStreamReader in_journal = new InputStreamReader(new FileInputStream(worldcatfile), "UTF-8");
			CSVReader read_journal = new CSVReader(in_journal, '#', '"', 0);
			while ((line = read_journal.readNext()) != null) {
				String issn = line[1];
				if (new Issn_methods().issnformat(issn)){
					if (!m_issn.contains(issn)){
						write_issns.append(issn + "\n");
					}
					m_issn.add(issn);
				}
			}
			read_journal.close();
			in_journal.close();
			write_issns.flush();
			write_issns.close();
			System.out.println("Nb correct issns: " + m_issn.size());
			System.out.println("Nb different issns: " + m_issn.elementSet().size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
