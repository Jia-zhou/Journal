package com.onescience.journal.methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.onescience.utils.StringUtil;
import com.onescience.utils.pinyin.PinyinFormat;
import com.onescience.utils.pinyin.PinyinHelper;

public class TitleComparator {
	
	// list of words without meaning
	private static Set<String> nomeaning = new HashSet<String>(Arrays.asList("in", "of", "and", "the", "on", "to", "online", "print"));
	
	public TitleComparator(){
	}
	
	
	/**
	 * return if 2 titles are similar, at different level
	 * type = 1: less strict (for titles with same Issn value for example)
	 * type = 2: more strict
	 */
	public boolean analyseTitle(String t1, String t2, int type){
		boolean result = false;
		String t1a = t1.toLowerCase().replaceAll("\\s+", " ").trim();
		String t2a = t2.toLowerCase().replaceAll("\\s+", " ").trim();
		if ((!t1a.equals("")) && (!t2a.equals(""))){
			result = t1a.equals(t2a);
			if (result) {
				return true;
			}
		}
		
		String t1b = StringUtil.toEnglishAlphabet(t1a).replaceAll("\\s+", " ").trim();
		String t2b = StringUtil.toEnglishAlphabet(t2a).replaceAll("\\s+", " ").trim();
		if (!result){
			if ((!t1b.equals("")) && (!t2b.equals(""))){
				result = t1b.equals(t2b);
				if (result) {
					return true;
				}
			}
		}
		
		String t1c = t1b.replaceAll("['’´`¨˜!¸.,:-]", " ").replace("/", " ").replaceAll("\\s+", " ").trim();
		String t2c = t2b.replaceAll("['’´`¨˜!¸.,:-]", " ").replace("/", " ").replaceAll("\\s+", " ").trim();
		if ((!t1c.equals("")) && (!t2c.equals(""))){
			result = t1c.equals(t2c);
			if (result) {
				return true;
			}
		}
		
		// test if one title is completely included in the other title
		if (type == 1){
			int position1 = t1c.indexOf(t2c);
			int position2 = t2c.indexOf(t1c);
			if ((!t1c.equals("")) && (!t2c.equals(""))){
				if ((position1>=0)||(position2>=0)){
					return true;
				}
			}
		}
		
		String t1d = removeParenthesis(t1c);
		String t2d = removeParenthesis(t2c);
		if  ((!t1d.equals("")) && (!t2d.equals(""))){
			result = t1d.equals(t2d);
			if (result) {
				return true;
			}
		}
		
		String t1e = t1d.replaceAll("&", " and ").replaceAll("\\s+", " ").trim();
		String t2e = t2d.replaceAll("&", " and ").replaceAll("\\s+", " ").trim();
		if ((!t1e.equals("")) && (!t2e.equals(""))){
			result = t1e.equals(t2e);
			if (result) {
				return true;
			}
		}
		
		if (type == 1){
			if ((t1e.startsWith(t2e))||(t2e.startsWith(t1e))){
				result = true;
				if (result) {
					return true;
				}
			}
			if ((t1e.endsWith(t2e))||(t2e.endsWith(t1e))){
				result = true;
				if (result) {
					return true;
				}
			}
		}
		
		if (type ==1){
			if (sameWords(t1e, t2e)){
				result = true;
				if (result) {
					return true;
				}
			}
		}
		
		
		String t1f = PinyinHelper.convertToPinyinString(t1e, "", PinyinFormat.WITHOUT_TONE).replace(" ", "");
		String t2f = PinyinHelper.convertToPinyinString(t2e, "", PinyinFormat.WITHOUT_TONE).replace(" ", "");
		if ((!t1f.equals("")) && (!t2f.equals(""))){
			result = t1f.equals(t2f);
			if (result) {
				return true;
			}
		}
		
		if (type == 1){
			Set<String> mots1 = getMots(t1e);
			Set<String> mots2 = getMots(t2e);
			Set<String> diff1 = Sets.difference(Sets.difference(mots1, mots2), nomeaning);
			Set<String> diff2 = Sets.difference(Sets.difference(mots2, mots1), nomeaning);
			if ((diff1.size()==0)&&(diff2.size()==0)){
				result = true;
				if (result) {
					return true;
				}
			}
		}
		
		if (type == 1){
			return sameInitials(t1e, t2e);
		}
		
		return result;
	}
	
	// test if 2 titles have same initials of all the words with meaning
	public boolean sameInitials(String t1, String t2){
		String t3 = t1;
		String t4 = t2;
		if (t1.length()<t2.length()){
			t3 = t2;
			t4 = t1;
		}
		String[] ms = t3.split(" ", -1);
		List<String> mots = new ArrayList<String>();  
		for (int i=0; i<ms.length; i++){
			mots.add(ms[i]);
		}
		mots.removeAll(nomeaning);
		
		String t5 = "";
		for (int i=0; i<mots.size(); i++){
			t5 = t5 + mots.get(i).substring(0, 1);
		}
		return t4.equals(t5);
	}
	
	// test if 2 titles have the same words after removing all no-meaning words
	public boolean sameWords(String t1, String t2){
		String[] m1 = t1.split(" ", -1);
		String[] m2 = t2.split(" ", -1);
		
		List<String> ms1 = new ArrayList<String>();
		List<String> ms2 = new ArrayList<String>();
		for (int i=0; i<m1.length; i++){
			ms1.add(m1[i]);
		}
		for (int i=0; i<m2.length; i++){
			ms2.add(m2[i]);
		}
		ms1.removeAll(nomeaning);
		ms2.removeAll(nomeaning);
		
		if ((ms1.size()==ms2.size())&&(ms1.size()>1)){
			boolean case1 = true;
			int i = 0;
			while (case1 && (i< ms1.size())){
				if (!ms1.get(i).startsWith(ms2.get(i))){
					case1 = false;
				}
				i++;
			}
			if (case1){
				return true;
			}
			
			boolean case2 = true;
			int j = 0;
			while (case2 && (j<ms1.size())){
				if (!ms2.get(j).startsWith(ms1.get(j))){
					case2 = false;
				}
				j++;
			}
			if (case2){
				return true;
			}
		}
		return false;
	}
	
	// return the string after removing the part between parenthesis
	public String removeParenthesis(String c){
		String result = c;
		if (!c.equals("")){
			int posit1 = c.indexOf("(");
			if (posit1>=0){
				int posit2 = c.indexOf(")");
				if (posit2>posit1){
					result = (c.substring(0, posit1) + c.substring(posit2+1, c.length())).replaceAll("\\s+", " ").trim();
				}
			}
			if (result.equals("")){
				result = c.substring(posit1+1, c.length()-1);
			}
		}
		return result;
	}
	
	// return all the common words, as a Set<String>, in 2 string
	public Set<String> getCommonMots(String s1, String s2){
		Set<String> mots1 = getMots(s1);
		Set<String> mots2 = getMots(s2);
		return Sets.intersection(mots1, mots2);
	}
	
	// return all the words, as a Set<String>, of a string
	public Set<String> getMots(String s){
		String s2 = s.replaceAll("[.,&:]", " ").replaceAll("\\s+", " ").trim();
		String[] mots = s2.split(" ", -1);
		Set<String> result = new HashSet<String>();
		for (int i=0; i<mots.length; i++){
			result.add(mots[i]);
		}
		return result;
	}
}
