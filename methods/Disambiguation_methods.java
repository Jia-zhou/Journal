package com.onescience.journal.methods;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.onescience.journal.schema_journal.Journal;

public class Disambiguation_methods {
	
	public Disambiguation_methods(){
	}
	
	
	/**
	 * find duplications among a list of journals with common issn: group same journal by title
	 * return the results by a list of Set<Integer>, with Integer represent the index of journal in the journal list 
	 * same journals will be grouped in the same set
	 */
	public List<Set<Integer>> sameJournal(List<Journal> journals){
		List<int[]> cpls = new ArrayList<int[]>();
		int nbj = journals.size();
		for (int i=0; i<nbj-1; i++){
			for (int j=i+1; j<nbj; j++){
				if (similarTitle(journals.get(i).getTitles(), journals.get(j).getTitles(), 1)){
					int[] cpl = {i, j};
					cpls.add(cpl);
				}
			}
		}
		return groupNodes(nbj, cpls);
	}
	
	// group same journal of a journal list by title, where a journal is represented by a Set<String> of all their titles 
	public List<Set<Integer>> getSameJournal(List<Set<String>> journals){
		List<int[]> cpls = new ArrayList<int[]>();
		int nbj = journals.size();
		for (int i=0; i<nbj-1; i++){
			for (int j=i+1; j<nbj; j++){
				if (similarTitle(journals.get(i), journals.get(j), 1)){
					int[] cpl = {i, j};
					cpls.add(cpl);
				}
			}
		}
		return groupNodes(nbj, cpls);
	}
	
	// group the connected nodes, the connections are represented by couple-node in edges
	public List<Set<Integer>> groupNodes(int nb, List<int[]> edges){
		List<Set<Integer>> result = new ArrayList<Set<Integer>>();
		for (int i=0; i<nb; i++){
			Set<Integer> set = new HashSet<Integer>();
			set.add(i);
			result.add(set);
		}
		for (int l=0; l<edges.size(); l++){
			int a = edges.get(l)[0];
			int b = edges.get(l)[1];
			int inda = 0;
			int indb = 0;
			for (int m=0; m<result.size(); m++){
				if (result.get(m).contains(a)){
					inda = m;
				}
				if (result.get(m).contains(b)){
					indb = m;
				}
			}
			if (inda != indb){
				Set<Integer> set = Sets.union(result.get(inda), result.get(indb));
				result.remove(Math.max(inda, indb));
				result.remove(Math.min(inda, indb));
				result.add(set);
			}
		}
		return result;
	}
	
	// test if 2 sets of titles have a similar title, at level of type (evaluated by TitleCompartor().analyseTitle) 
	public boolean similarTitle(Set<String> ttls1, Set<String> ttls2, int type){
		boolean result = false;
		if ((ttls1.size()==0)||(ttls2.size()==0)){
			return true;
		}
		String[] titles1 = ttls1.toArray(new String[0]);
		String[] titles2 = ttls2.toArray(new String[0]);
		int i = 0;
		while ((!result)&&(i<titles1.length)){
			int j = 0;
			while((!result)&&(j<titles2.length)){
				result = new TitleComparator().analyseTitle(titles1[i], titles2[j], type);
				j++;
			}
			i++;
		}
		return result;
	}

}
