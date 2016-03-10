package com.onescience.journal.methods;

import java.util.*;

import com.google.common.collect.Sets;
import com.onescience.journal.schema_journal.*;

public class Journal_methods {
	
	public Journal_methods(){
	}
	
	// return all Issn values, as a Set<String>, from a journal
	public Set<String> getIssnValues(Journal journal){
		Set<String> issnValues = new HashSet<String>();
		Set<Issn> issns = journal.getIssns();
		for (Issn issn : issns){
			issnValues.add(issn.getContent());
		}
		return issnValues;
	}
	
	// return all Issn values of a certain type (type = "ISSN-print" for example), as a Set<String>, from a journal
	public Set<String> getIssnValues(Journal journal, String type){
		Set<String> issnValues = new HashSet<String>();
		Set<Issn> issns = journal.getIssns();
		for (Issn issn : issns){
			if (issn.getMedia().equals(type)){
				issnValues.add(issn.getContent());
			}
		}
		return issnValues;
	}
	
	// test if 2 Issn are same (true if same type and same content)
	public boolean sameIssn(Issn issn1, Issn issn2){
		boolean testContent = issn1.getContent().equals(issn2.getContent());
		boolean testMedia = issn1.getMedia().equals(issn2.getMedia());
		return (testContent && testMedia);
	}
	
	// test if an Issn exists in a Set<Issn> 
	public boolean foundIssn(Set<Issn> issns, Issn issn){
		for (Issn issn0 : issns){
			if (sameIssn(issn0, issn)){
				return true;
			}
		}
		return false;
	}
	
	// add an issn in a Set<Issn> if it does not exist
	public Set<Issn> addIssn(Set<Issn> issns, Issn issn){
		Set<Issn> result = new HashSet<Issn>();
		result.add(issn);
		for (Issn issn0 : issns){
			if (!foundIssn(issns, issn0)){
				result.add(issn0);
			}
		}
		return result;
	}
	
	// merge 2 Set<Issn>
	public Set<Issn> mergeIssn(Set<Issn> issns1, Set<Issn> issns2){
		Set<Issn> result = new HashSet<Issn>();
		for (Issn issn : issns1){
			result = addIssn(result, issn);
		}
		for (Issn issn : issns2){
			result = addIssn(result, issn);
		}
		return result;
	}
	
	// deduplicate a Set<Issn>
	public Set<Issn> dedupIssn(Set<Issn> issns){
		Set<Issn> result = new HashSet<Issn>();
		for (Issn issn : issns){
			result = addIssn(result, issn);
		}
		return result;
	}
	
	
	// return all Id values of a certain type (type = "1journal" for example), as a Set<String>, from a journal
	public Set<String> getIdValue(Journal journal, String type){
		Set<String> result = new HashSet<String>();
		Set<Id> ids = journal.getIds();
		for (Id id: ids){
			if (id.getIdType().equals(type)){
				result.add(id.getIdNumber());
			}
		}
		return result;
	}
	
	
	public boolean sameSPR(StatusPR spr1, StatusPR spr2){
		boolean testStatus = spr1.getStatus().equals(spr2.getStatus());
		boolean testNote = spr1.getNote().equals(spr2.getNote());
		return (testStatus && testNote);
	}
	
	public boolean foundSPR(Set<StatusPR> sprs, StatusPR spr){
		for (StatusPR spr0 : sprs){
			if (sameSPR(spr0, spr)){
				return true;
			}
		}
		return false;
	}
	
	public Set<StatusPR> addSPR(Set<StatusPR> sprs, StatusPR spr){
		Set<StatusPR> result = new HashSet<StatusPR>();
		result.add(spr);
		for (StatusPR spr0 : sprs){
			if (!foundSPR(sprs, spr0)){
				result.add(spr0);
			}
		}
		return result;
	}
	
	public Set<StatusPR> mergeSPR(Set<StatusPR> sprs1, Set<StatusPR> sprs2){
		Set<StatusPR> result = new HashSet<StatusPR>();
		for (StatusPR spr : sprs1){
			result = addSPR(result, spr);
		}
		for (StatusPR spr : sprs2){
			result = addSPR(result, spr);
		}
		return result;
	}
	
	public Set<StatusPR> dedupSPR(Set<StatusPR> sprs){
		Set<StatusPR> result = new HashSet<StatusPR>();
		for (StatusPR spr : sprs){
			result = addSPR(result, spr);
		}
		return result;
	}
	
	
	public boolean sameId(Id id1, Id id2){
		boolean testType = id1.getIdType().equals(id2.getIdType());
		boolean testNumber = id1.getIdNumber().equals(id2.getIdNumber());
		return (testType && testNumber);
	}
	
	public boolean foundId(Set<Id> ids, Id id){
		for (Id id0 : ids){
			if (sameId(id0, id)){
				return true;
			}
		}
		return false;
	}
	
	public Set<Id> addId(Set<Id> ids, Id id){
		Set<Id> result = new HashSet<Id>();
		result.add(id);
		for (Id id0 : ids){
			if (!foundId(ids, id0)){
				result.add(id0);
			}
		}
		return result;
	}
	
	public Set<Id> mergeId(Set<Id> ids1, Set<Id> ids2){
		Set<Id> result = new HashSet<Id>();
		for (Id id : ids1){
			result = addId(result, id);
		}
		for (Id id : ids2){
			result = addId(result, id);
		}
		return result;
	}
	
	public Set<Id> dedupId(Set<Id> ids){
		Set<Id> result = new HashSet<Id>();
		for (Id id : ids){
			result = addId(result, id);
		}
		return result;
	}
	
	
	
	
	// copy all the fields of a journal in a new journal
	public Journal copyJournal(Journal journal){
		Journal j = new Journal();
		j.setSource(journal.getSource());
		j.setIds(journal.getIds());
		j.setTitles(journal.getTitles());
		j.setFrequency(journal.getFrequency());
		j.setSubjects(journal.getSubjects());
		j.setSubfields(journal.getSubfields());
		j.setLanguages(journal.getLanguages());
		j.setTextLanguages(journal.getTextLanguages());
		j.setAbstractLanguages(journal.getAbstractLanguages());
		j.setPublisher(journal.getPublisher());
		j.setIssns(journal.getIssns());
		j.setRelatedJournal(journal.getRelatedJournal());
		j.setLinks(journal.getLinks());
		j.setStatusOA(journal.getStatusOA());
		j.setStatusPR(journal.getStatusPR());
		j.setObservation(journal.getObservation());
		return j;
	}
	
	
	/**return the relation of 2 sets of language
	 * type = 0: same sets
	 * type = 1: no sense (2 sets are "multi-languages", or "undetermined-language")
	 * type = 2: no conflict (1 of 2 sets is "multi-languages", or "undetermined-language")
	 * type = 3: almost same (2 sets are same, not empty, after removing "multi-languages" and "undetermined-language")
	 * type = 4: partially same (one set contain all the languages of the other one)
	 * type = 5: common element (2 sets contain at least one same language)
	 * type = 6: conflict (2 sets contain completely different languages)
	 */
	public int sameSetlanguages(Set<String> s1, Set<String> s2){
		if (s1.equals(s2)){
			return 0;
		}
		
		Set<String> lang1 = new HashSet<String>();
		for (String lang : s1){
			lang1.add(lang);
		}
		Set<String> lang2 = new HashSet<String>();
		for (String lang : s2){
			lang2.add(lang);
		}
		lang1.removeAll(Arrays.asList("und", "mul"));
		lang2.removeAll(Arrays.asList("und", "mul"));
		
		if ((lang1.size()==0) && (lang2.size()==0)){
			return 1; 
		}
		
		if ((lang1.size()==0) || (lang2.size()==0)){
			return 2;
		}
		
		if (lang1.equals(lang2)){
			return 3;
		}
		
		if (lang1.containsAll(lang2) || (lang2.containsAll(lang1))){
			return 4;
		}
		
		if (Sets.intersection(lang1, lang2).size()>0){
			return 5;
		}
		
		return 6;
	}

}
