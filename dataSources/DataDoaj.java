package com.onescience.journal.dataSources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.onescience.journal.methods.Issn_methods;
import com.onescience.journal.schema_journal.Issn;
import com.onescience.journal.schema_journal.Journal;
import com.onescience.journal.schema_journal.Link;
import com.onescience.journal.schema_journal.Publisher;
import com.onescience.journal.schema_journal.StatusOA;
import com.onescience.journal.schema_journal.StatusPR;
import com.opencsv.CSVReader;

public class DataDoaj {
	
	public DataDoaj(){
	}
	
	// translate journal csv file tp json file
	public void csvtojson(String csvfile, String jsonfile){
		try {
			String line[];
			String ligne;
			ObjectMapper mapper = new ObjectMapper();
			FileWriter writer = new FileWriter(jsonfile, false);
			InputStreamReader in_csv = new InputStreamReader(new FileInputStream(csvfile), "UTF-8");
			CSVReader read_csv = new CSVReader(in_csv, ',', '"', 1);
			while ((line = read_csv.readNext()) != null) {
				Journal journal = new Journal();
				Set<String> source = new HashSet<String>();
				source.add("DOAJ2016");
				journal.setSource(source);
				
				Set<Issn> issns = new HashSet<Issn>();
				if (!line[3].equals("")){
					issns.add(new Issn("ISSN-print", line[3]));
				}
				if (!line[4].equals("")){
					issns.add(new Issn("ISSN-electronic", line[4]));
				}
				if (issns.size()>0){
					journal.setIssns(issns);
				}
				
				Set<String> titles = new HashSet<String>();
				if (!line[0].equals("")){
					titles.add(line[0]);
				}
				if (!line[2].equals("")){
					titles.add(line[2]);
				}
				if (titles.size()>0){
					journal.setTitles(titles);
				}
				
				Set<Link> links = new HashSet<Link>();
				if (!line[1].equals("")){
					Link link = new Link();
					link.setHref(new URI(line[1].replace("|", "/").trim()));
					link.setRel(Link.Rel.SELF);
					links.add(link);
				}
				if (links.size()>0){
					journal.setLinks(links);;
				}
				
				String editor = line[6];
				String name = line[5];
				String place = line[8];
				if (!(editor.equals("") && name.equals("") && place.equals(""))){
					Publisher publisher = new Publisher(editor, name, place, null);
					journal.setPublisher(publisher);					
				}
				
				String[] subjts = line[31].replaceAll("\\s+", " ").trim().split(", ");
				Set<String> subjects = new HashSet<String>(Arrays.asList(subjts));
				subjects.remove("");
				if (subjects.size()>0){
					journal.setSubjects(subjects);
				}
				
				String[] subfds = line[56].replace("|", "#").replaceAll("\\s+", " ").trim().split(" # ");
				Set<String> subfields = new HashSet<String>(Arrays.asList(subfds));
				subfields.remove("");
				if (subfields.size()>0){
					journal.setSubfields(subfields);
				}
				
				String lang = line[32].trim();
				String[] langs = lang.replace("Greek, Modern (1453-)", "Greek").split(", ");
				Set<String> languages = new HashSet<String>(Arrays.asList(langs));
				languages.remove("");
				if (languages.size()>0){
					journal.setTextLanguages(languages);
				}
				
				Set<StatusOA> soa = new HashSet<StatusOA>();
				StatusOA oa = new StatusOA();
				oa.setStatus(StatusOA.Status.YES);
				if (!line[29].equals("")){
					oa.setYear(line[29]);
				}
				oa.setNote("DOAJ");
				soa.add(oa);
				journal.setStatusOA(soa);
				
				Set<StatusPR> spr = new HashSet<StatusPR>(Arrays.asList(new StatusPR(StatusPR.Status.YES, "DOAJ")));
				journal.setStatusPR(spr);
				
				ligne  = mapper.writeValueAsString(journal);
				writer.append(ligne + "\n");
			}
			read_csv.close();
			in_csv.close();
			writer.flush();
			writer.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// extract all issn in Doaj csv file and analyze issn format
	public void issnlist(String csvfile, String issnfile){
		try {
			String line[];
			Multiset<String> m_issn = HashMultiset.create();
			Set<String> incorrect = new HashSet<String>();
			Set<String> unvalide = new HashSet<String>();
			
			FileWriter writer = new FileWriter(issnfile, false);
			InputStreamReader in_journal = new InputStreamReader(new FileInputStream(csvfile), "UTF-8");
			CSVReader read_journal = new CSVReader(in_journal, ',', '"', 1);
			while ((line = read_journal.readNext()) != null) {
				String pissn = line[3];
				String eissn = line[4];
				if (!pissn.equals("")){
					if (!m_issn.contains(pissn)){
						writer.append(pissn + "\n");
					}
					m_issn.add(pissn);
					if (!(new Issn_methods().issnformat(pissn))){
						incorrect.add(pissn);
					}
					if (!(new Issn_methods().issnvalide(pissn))){
						unvalide.add(pissn);
					}
				}
				if (!eissn.equals("")){
					if (!m_issn.contains(eissn)){
						writer.append(eissn + "\n");
					}
					m_issn.add(eissn);
					if (!(new Issn_methods().issnformat(eissn))){
						incorrect.add(eissn);
					}
					if (!(new Issn_methods().issnvalide(eissn))){
						unvalide.add(eissn);
					}
				}
			}
			read_journal.close();
			in_journal.close();
			writer.flush();
			writer.close();
			
			System.out.println("Nb issns totals: " + m_issn.size());
			System.out.println("Nb issns differents: " + m_issn.elementSet().size());
			System.out.println("Nb issns incorrect: " + incorrect.size());
			System.out.println(incorrect);
			System.out.println("Nb issns unvalide: " + unvalide.size());
			System.out.println(unvalide);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

}
