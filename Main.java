package com.onescience.journal;

import java.util.ArrayList;
import java.util.List;

import com.onescience.journal.build1journal.Addlanguage;
import com.onescience.journal.build1journal.Addnewjournal;
import com.onescience.journal.build1journal.Addsubfield;
import com.onescience.journal.build1journal.Cleanformer1journal;
import com.onescience.journal.build1journal.Deduplication;
import com.onescience.journal.build1journal.JsontoCsv;

public class Main {

	public static void main(String[] args) {
		
		String prefix0 = "/home/jia/Documents/travail/journal/doc/origines/";
		String prefix00 = "/home/jia/Documents/travail/journal/doc/origines/1journal/";
		String prefix01 = "/home/jia/Documents/travail/journal/doc/origines/1journal/steps/";
		String prefix02 = "/home/jia/Documents/travail/journal/doc/origines/1journal/other_journal/";
		String prefix03 = "/home/jia/Documents/travail/journal/doc/origines/1journal/standard/";
		
		String jnlsfile00 = prefix00 + "journal";
		
		String jnlsfile01 = prefix00 + "journal_cleaned.csv";
		String caracReplace = prefix01 + "replacement_caractere.txt";
		String symbolReplace = prefix01 + "replacement_symbol.txt";
		
		String anormalissn0 = prefix01 + "journal_issn_anormal0.txt";
		String jnlsfile02a = prefix01 + "journal_corrected1.csv";
		String anormalissn1 = prefix01 + "journal_issn_anormal1.txt";
		String jnlsfile02b = prefix01 + "journal_corrected2.csv";
		String anormalissn2 = prefix01 + "journal_issn_anormal2.txt";
		String jnlsfile02c = prefix01 + "journal_corrected3.csv";
		String anormalissn3 = prefix01 + "journal_issn_anormal3.txt";
		
		String jnlsfile02 = prefix00 + "journal_corrected.csv";
		String jnlsfile03 = prefix00 + "journal_deduplicated.csv";
		String jnlsfile10 = prefix00 + "1journal.json";
		String jnlsfile11 = prefix00 + "1journal_deduplicated.json";
		String jnlsfile12 = prefix00 + "1journal_deduplicated2.json";
		String deduptitle = prefix00 + "ambiguetitle/dedup_noissn.txt";
		String jnlsfile13 = prefix00 + "1journal_issn.json";
		
		String jnlsfile20 = prefix00 + "newjournal_issn.json";
		String newjnls1 = prefix02 + "others_deduplicated.json";
		String newjnls2 = prefix03 + "manual_samejournal.json";
		List<String> newjnlslist = new ArrayList<String>();
		newjnlslist.add(jnlsfile13);
		newjnlslist.add(newjnls1);
		newjnlslist.add(newjnls2);
		String jnlsfile21 = prefix00 + "newjournal_deduplicated.json";
		
		String jnlsfile22 = prefix00 + "newjournal_lang.json";
		List<String> langsources = new ArrayList<String>();
		langsources.add(prefix00+"manual_language.json");
		langsources.add(prefix0+"Ulrich/Ulrich_modified.json");
		langsources.add(prefix0+"DOAJ/doaj_20160111.json");
		langsources.add(prefix0+"Medjour/medjour_modified.json");
		langsources.add(prefix0+"jstage/jstage.json");
		langsources.add(prefix0+"Adat/ADAT-List_2.json");
		langsources.add(prefix0+"CAJ/CAJ.json");
		langsources.add(prefix0+"Copernicus/copernicus.json");
		langsources.add(prefix0+"Karger/karger.json");
		langsources.add(prefix0+"SRP/SRP.json");
		langsources.add(prefix0+"British_Library/British_Library_modified.json");
		langsources.add(prefix0+"Road/road.json");
		langsources.add(prefix0+"Loc/loc_modified.json");
		
		String jnlsfile23 = prefix00 + "newjournal_normlang.json";
		String langnormfile = prefix03 + "languages.csv";
		String langexception = prefix03 + "lang_exception.txt";
		String jnlsfile24 = prefix00 + "newjournal_normlang_complete.json";
		String issnlinks = prefix03 + "links";
		
		String jnlsfile25 = prefix00 + "newjournal_lang_subfield.json";
		String sfdSM1SM2 = prefix00 + "issns_subfields.csv";
		String sfdmanual = prefix03 + "manual_subfield";
		String jnlsfile25csv = prefix00 + "newjournal_lang_subfield.csv";
		
		
		
		
		
		/**
		 * Former 1journal csv file: source, idsource, title, issn, pissn, eissn, id1journal, subfield
		 * Deduplicated 1journal csv file: id1journal, issn, pissn, eissn, title, subfield
		 * 
		 * clean the former 1journal list: remove coded letters, clean Issn format, merge same lines, ...
		 */
		// remove coded letters in all titles of 1journal
		new Cleanformer1journal().clean_title(jnlsfile00, jnlsfile01, caracReplace);
		
		// list Issn values which don't match Issn standard format and clean Issn by step
		new Cleanformer1journal().build_wrongIssn(jnlsfile01, anormalissn0);
		new Cleanformer1journal().clean_issn1(jnlsfile01, jnlsfile02a);
		new Cleanformer1journal().build_wrongIssn(jnlsfile02a, anormalissn1);
		new Cleanformer1journal().clean_issn2(jnlsfile02a, jnlsfile02b);
		new Cleanformer1journal().build_wrongIssn(jnlsfile02b, anormalissn2);
		new Cleanformer1journal().clean_issn3(jnlsfile02b, jnlsfile02c);
		new Cleanformer1journal().build_wrongIssn(jnlsfile02c, anormalissn3);
		
		// deduplicate former 1journal list in csv format
		new Cleanformer1journal().csvdedup(jnlsfile02, jnlsfile03);
		
		// translate 1journal from csv file to jsonfile
		new Cleanformer1journal().csvtojson(jnlsfile03, jnlsfile10);
		
		// deduplicate former 1journal list in json format: only by same Issn
		new Cleanformer1journal().jsondedup(jnlsfile10, jnlsfile11);
		
		// deduplicate former 1journal list in json format: all journals with or without journals
		new Cleanformer1journal().jsondedupAll(jnlsfile10, jnlsfile12, deduptitle);
		
		// separate the journals by Issn
		new Cleanformer1journal().jsonduplicate(jnlsfile12, jnlsfile13);
		
		
		/**
		 * import new journals to 1journal list, import languages and subfields to 1journal list
		 */
		// add new journals (new PR list + manual disambiguation) in 1journal
		new Addnewjournal().importJournal(newjnlslist, jnlsfile20);
		
		// deduplicate new 1journal list
		new Deduplication().jsondedup(jnlsfile20, jnlsfile21);
		
		// import the language from different sources
		new Addlanguage().importLanguage(jnlsfile21, langsources, jnlsfile22);
		
		// normalize the language text to language Iso2B
		new Addlanguage().normalizeLanguage(jnlsfile22, langnormfile, langexception, jnlsfile23);
		
		// complete journals languages by Issn links
		new Addlanguage().completeLanguage(jnlsfile23, issnlinks, jnlsfile24);
		
		// import the subfields from SM1 and SM2 to 1journal
		new Addsubfield().importSubfield(jnlsfile24, sfdSM1SM2, sfdmanual, jnlsfile25);
		
		// extract some journal fields (Issn, title, subfield, language) to a csv file
		new JsontoCsv().csvProduction(jnlsfile25, jnlsfile25csv);	
		
		
		/**
		 * build the new journal thesaurus
		 */
		
		
		/**
		 * analysis and statistics
		 */
		
	}

}
