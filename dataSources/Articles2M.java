package com.onescience.journal.dataSources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.onescience.journal.methods.Issn_methods;
import com.onescience.journal.methods.Journal_methods;
import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Journal;
import com.opencsv.CSVReader;

public class Articles2M {

	public static void main(String[] args) {
		
		Articles2M ie = new Articles2M();
		// issn valide test for 2M articles
		//ie.issn_articles();
		ie.checkIssn();
	}
	
	
	// afficher les unvalides issns
	public void issn_articles(){
		String articlefile = "/home/jia/Documents/travail/journal/doc/JournalsYorrick.csv";
		try{
			String line[];
			Multiset<String> m_issn = HashMultiset.create();
			InputStreamReader in_article = new InputStreamReader(new FileInputStream(articlefile), "UTF-8");
			CSVReader read_article = new CSVReader(in_article, ';', '"', 1);
			while ((line = read_article.readNext()) != null) {
				String issn1 = line[1].replaceAll("[^-xX0-9]", "");
				String issn2 = line[2].replaceAll("[^-xX0-9]", "");
				Pattern pattern = Pattern.compile("\\d{4}(-+)\\d{3}(\\d|x|X)");
				if (!issn1.equals("")){
					if (!(new Issn_methods().issnvalide(new Issn_methods().issnreformat(issn1)))) {
						if (pattern.matcher(issn1).matches()){
							m_issn.add(issn1);
						}
					}
				}
				if (!issn2.equals("")){
					if (!(new Issn_methods().issnvalide(new Issn_methods().issnreformat(issn2)))) {
						if (pattern.matcher(issn2).matches()){
							m_issn.add(issn2);
						}
					}
				}
			}
			read_article.close();
			in_article.close();
			
			System.out.println(m_issn.size());
			System.out.println(m_issn.elementSet().size());
			
			for (String issn : m_issn.elementSet()){
				System.out.println(issn);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void checkIssn(){
		String prefix = "/home/jia/Documents/travail/journal/doc/";
		String articlefile = "JournalsYorrick.csv";
		String journalfile1 = "origines/1journal/1journal.json";
		String journalfile2 = "origines/DOAJ/doaj_20160111.json";
		List<String> jsons = new ArrayList<String>();
		jsons.add(prefix+journalfile1);
		jsons.add(prefix+journalfile2);
		
		try {
			
			// importer les infos de 1journal
			HashMultimap<String, Journal> issn_journal = HashMultimap.create();
			HashMultimap<String, Journal> title_journal = HashMultimap.create();
			List<Journal> jnls1 = new Jsonfile().readfile(jsons);
			for (int i=0; i<jnls1.size(); i++){
				Journal journal = jnls1.get(i);
				Set<String> issns = new Journal_methods().getIssnValues(journal);
				for (String issn : issns){
					issn_journal.put(issn, journal);
				}
				Set<String> titles = journal.getTitles();
				for (String title : titles){
					title_journal.put(title, journal);
				}
			}
			
			// les infos pour les 2M articles
			String line[];
			HashMultimap<String, Integer> issn_article = HashMultimap.create();
			HashMultimap<String, Integer> issn2_article = HashMultimap.create();
			HashMultimap<String, Integer> title_article= HashMultimap.create();
			HashMap<Integer, String> Nline_issn= new HashMap<Integer, String>();
			HashMap<Integer, String> Nline_issn2= new HashMap<Integer, String>();
			HashMap<Integer, String> Nline_title= new HashMap<Integer, String>();
			HashMap<Integer, Integer> nb_article = new HashMap<Integer, Integer>();
			int nb_ligne = 0;
			InputStreamReader in_article = new InputStreamReader(new FileInputStream(prefix+articlefile), "UTF-8");
			CSVReader read_article = new CSVReader(in_article, ';', '"', 1);
			while ((line = read_article.readNext()) != null) {
				nb_ligne++;
				if (!line[1].equals("")){
					issn_article.put(line[1], nb_ligne);
					String issn2 = new Issn_methods().issnreformat(line[1]);
					if (!issn2.equals("")){
						issn2_article.put(issn2, nb_ligne);
					}
				}
				if (!line[2].equals("")){
					issn_article.put(line[2], nb_ligne);
					String issn2 = new Issn_methods().issnreformat(line[2]);
					if (!issn2.equals("")){
						issn2_article.put(issn2, nb_ligne);
					}
				}
				if (!line[0].equals("")){
					title_article.put(line[0], nb_ligne);
				}
				nb_article.put(nb_ligne, Integer.parseInt(line[3]));
				Nline_title.put(nb_ligne, line[0]);
				Nline_issn.put(nb_ligne, line[1]);
				Nline_issn2.put(nb_ligne, line[2]);
				
			}
			read_article.close();
			in_article.close();
			
			Set<String> issn1 = issn_journal.keySet();
			Set<String> issn2 = issn_article.keySet();
			Set<String> same_issn = Sets.intersection(issn1, issn2);
			System.out.println("nb issn dans 1journal: " + same_issn.size());
			//System.out.println(same_issn);
			Set<Integer> lines_issn = new HashSet<Integer>();
			for (String issn : same_issn){
				Set<Integer> nbs = issn_article.get(issn);
				lines_issn.addAll(nbs);
				for (int nb : nbs){
					//System.out.println(issn + ": line: " + issn_article.get(issn) + "; nb articles: " + nb_article.get(nb));
				}			
			}
			int nb_issn = 0;
			for (int nb : lines_issn){
				nb_issn = nb_issn + nb_article.get(nb);
			}
			System.out.println("total nb articles by issn: " + nb_issn);
			
			Set<String> title1 = title_journal.keySet();
			Set<String> title2 = title_article.keySet();
			Set<String> same_title = Sets.intersection(title1, title2);
			System.out.println("nb titles dans 1journal: " + same_title.size());
			//System.out.println(same_title);
			Set<Integer> lines_title = new HashSet<Integer>();
			for (String title : same_title){
				Set<Integer> nbs = title_article.get(title);
				lines_title.addAll(nbs);
				for (int nb : nbs){
					System.out.println(title + ": line: " + title_article.get(title) + "; nb articles: " + nb_article.get(nb));
				}			
			}
			int nb_title = 0;
			for (int nb : lines_title){
				nb_title = nb_title + nb_article.get(nb);
			}
			System.out.println("total nb articles by issn: " + nb_title);
			
			Set<String> issn22 = issn2_article.keySet();
			Set<String> same_issn2 = Sets.intersection(issn1, issn22);
			System.out.println("nb issn dans 1journal: " + same_issn2.size());
			//System.out.println(same_issn2);
			Set<Integer> lines_issn2 = new HashSet<Integer>();
			for (String issn : same_issn2){
				Set<Integer> nbs = issn2_article.get(issn);
				lines_issn2.addAll(nbs);
				for (int nb : nbs){
					//System.out.println(issn + ": line: " + issn2_article.get(issn) + "; nb articles: " + nb_article.get(nb));
				}			
			}
			int nb_issn2 = 0;
			for (int nb : lines_issn2){
				nb_issn2 = nb_issn2 + nb_article.get(nb);
			}
			System.out.println("total nb articles by issn: " + nb_issn2);
			
			Set<Integer> lines_total = Sets.union(lines_title, lines_issn2);
			int nb_total = 0;
			for (int nb : lines_total){
				nb_total = nb_total + nb_article.get(nb);
				System.out.println("N° journal: " + nb + ": " + Nline_title.get(nb) + "; " + Nline_issn.get(nb) + "; " + Nline_issn2.get(nb) + "; " + nb_article.get(nb));
			}
			System.out.println("nb total lines: " + lines_total.size());
			System.out.println("total nb articles: " + nb_total);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	

}
