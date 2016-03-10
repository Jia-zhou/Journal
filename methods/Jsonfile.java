package com.onescience.journal.methods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onescience.journal.schema_journal.Journal;

public class Jsonfile {
	
	public Jsonfile(){
	}
	
	// read a json file of journal, return a list of journal
	public List<Journal> readfile(String filename){
		List<Journal> journals = new ArrayList<Journal>();
		try{
			String ligne;
			ObjectMapper mapper = new ObjectMapper();
			BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
			while ((ligne = reader.readLine()) != null) {
				Journal journal = mapper.readValue(ligne, Journal.class);
				journals.add(journal);
			}
			reader.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return journals;
	}
	
	// read a list of json file of journal, return a list of journal
	public List<Journal> readfile(List<String> filelist){
		List<Journal> journals = new ArrayList<Journal>();
		try{
			String ligne;
			ObjectMapper mapper = new ObjectMapper();
			for (int i = 0; i<filelist.size(); i++) {
				String filename = filelist.get(i);
				BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
				while ((ligne = reader.readLine()) != null) {
					Journal journal = mapper.readValue(ligne, Journal.class);
					journals.add(journal);
				}
				reader.close();
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return journals;
	}
	
	// writer a list of journal in a json file
	public void writefile(String filename, List<Journal> journals){
		try {
			String ligne;
			ObjectMapper mapper = new ObjectMapper();
			FileWriter writer = new FileWriter(filename, false);
			for (int i=0; i<journals.size(); i++){
				ligne = mapper.writeValueAsString(journals.get(i));
				writer.append(ligne + "\n");
			}
			writer.flush();
			writer.close();
		}  catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
