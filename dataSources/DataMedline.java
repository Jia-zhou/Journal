package com.onescience.journal.dataSources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Journal;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class DataMedline {
	
	public DataMedline(){
	}
	
	// extract nlmid from downloaded file
	public void get_nlmid(String filename, String idfile){
		try {
			Set<String> ids = new HashSet<String>();
			FileWriter write_ids = new FileWriter(idfile, false);
			String ligne;
			BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
			while ((ligne = reader.readLine()) != null) {
				if (ligne.startsWith("NlmId:")){
					String id = ligne.substring(7);
					if (!ids.contains(id)){
						write_ids.append(id + "\n");
						write_ids.flush();
						ids.add(id);
					}
				}
			}
			reader.close();
			write_ids.close();
			System.out.println("Nb journals: " + ids.size() + "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// modify some structures in scraped file
	public void modifyjsonfile(String jnlsfile, String modifiedfile){
		try {
			String ligne;
			FileWriter writer = new FileWriter(modifiedfile, false);
			BufferedReader reader = new BufferedReader(new FileReader(new File(jnlsfile)));
			while ((ligne = reader.readLine()) != null) {
				ligne = ligne.replace("Greek, Modern", "Greek").replace("\"languages\":", "\"textLanguages\":");
				writer.append(ligne.replace("\"source\":\"medjour\"", "\"source\":[\"medjour\"]") + "\n");
			}
			reader.close();
			writer.flush();
			writer.close();
			
			List<Journal> jnls = new Jsonfile().readfile(modifiedfile);
			for (int i=0; i<jnls.size(); i++){
				Set<String> languages = new HashSet<String>();
				Set<String> langs = jnls.get(i).getTextLanguages();
				for (String lang : langs){
					String[] lgs = lang.split(",", -1);
					for (int j=0; j<lgs.length; j++){
						if (!lgs[j].trim().equals("")){
							languages.add(lgs[j].trim());
						}
					}
				}
				jnls.get(i).setTextLanguages(languages);
			}
			new Jsonfile().writefile(modifiedfile, jnls);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// extract article informations (issn, titles, language) from medline data
	public void parserMedline(String filepath, String datapath, String recapfile, String anormalfile){
		try {
			int nb_reper = 0;
			int nb_file = 0;
			int nb_line0 = 0;
			FileWriter writer = new FileWriter(recapfile, false);
			FileWriter writer2 = new FileWriter(anormalfile, false);
			File f0 = new File(filepath);
	        File[] files0 = f0.listFiles();
	        for (File file0 : files0) {  
	        	nb_reper++;
	        	String file0name = file0.getAbsolutePath();
	        	String repername = file0name.substring(file0name.length()-2);
	        	
	        	int nb_line1 = 0;
	        	String csvname = datapath + repername + ".csv";
	        	OutputStreamWriter out_csv = new OutputStreamWriter(new FileOutputStream(csvname), "UTF-8");
	            CSVWriter write_csv = new CSVWriter(out_csv, ';', '"');
	        	
	            File f1 = new File(file0.getAbsolutePath());
	            File[] files1 = f1.listFiles();
	            for (File file1 : files1){
	            	nb_file++;
	            	String filename = file1.getAbsolutePath();
	            	
	            	String[] line;
	            	int nb_line2 = 0;
	            	int nb_line = 0;
	            	InputStreamReader in_tsv = new InputStreamReader(new FileInputStream(filename), "UTF-8");
	    			CSVReader read_tsv = new CSVReader(in_tsv, '\t', CSVWriter.NO_QUOTE_CHARACTER, 0);
	    			while ((line = read_tsv.readNext()) != null) {
	    				nb_line++;
	    				if (line.length != 36){
	    					writer2.append(repername + " : " + filename.substring(34) + " : " + nb_line + " : " + line.length + "\n");
	    					writer2.flush();
	    				} else {
	    					String[] newline = new String[6];
	    					newline[0] = line[7];
	    					newline[1] = line[19];
	    					newline[2] = line[6];
	    					newline[3] = line[10];
	    					newline[4] = line[20];
	    					newline[5] = line[11];
	    					write_csv.writeNext(newline);
	    	                write_csv.flush();
	    	                nb_line2++;
	    				}
	    			}
	    			read_tsv.close();
	    			in_tsv.close();
	            	nb_line1 = nb_line1 + nb_line2;
	            	writer.append(repername + " : " + filename.substring(34) + " : " + nb_line2 + "\n");
	            	writer.flush();
	            }
	            write_csv.close();
	            out_csv.close();
	            nb_line0 = nb_line0 + nb_line1;
	            writer.append(file0name.substring(file0name.length()-2) + " : " + nb_line1 + "\n");
	            writer.flush();
	        }
	        writer.append("nb repertories: " + nb_reper + "\n");
	        writer.append("nb files: " + nb_file + "\n");
	        writer.append("nb enregistrements: " + nb_line0 + "\n");
	        writer.flush();
	        writer.close();
	        writer2.close();
	        
	        System.out.println("nb repertories: " + nb_reper);
	        System.out.println("nb files: " + nb_file);
	        System.out.println("nb enregistrements: " + nb_line0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
