package com.onescience.journal.dataSources;

import java.util.ArrayList;
import java.util.List;

import com.onescience.journal.build1journal.Deduplication;

public class AllOperations {

	public static void main(String[] args) {
		
		String prefix = "/home/jia/Documents/travail/journal/doc/origines/";
		/*
		// Medline
		String medline_downloadfile = prefix + "Medline/download/J_Entrez";
		String medline_idfile = prefix + "Medline/nlmid.txt";
		String medline_jnlsfile0 = prefix + "Medline/medjour.json";
		String medline_jnlsfile1 = prefix + "Medline/medjour_modified.json";
		String medline_filepath = "/media/jia/My Passport/medline";
		String medline_datapath = prefix + "Medline/data/";
		String medline_recapfile = prefix + "Medline/recap.txt";
		String medline_anormalfile = prefix + "Medline/line_anormal";
		
		// extract nlmid from downloaded file
		new DataMedline().get_nlmid(medline_downloadfile, medline_idfile);
		
		// reformat json file
		new DataMedline().modifyjsonfile(medline_jnlsfile0, medline_jnlsfile1);
		
		// extract article informations (issn, titles, language) from medline data
		new DataMedline().parserMedline(medline_filepath, medline_datapath, medline_recapfile, medline_anormalfile);
		
		
		
		// Doaj
		String doaj_csvfile = prefix + "DOAJ/doaj_20160111_1700_utf8.csv";
		String doaj_jsonfile = prefix + "DOAJ/doaj_20160111.json";
		String doaj_issnfile = prefix + "DOAJ/doaj_issns.txt";
		
		// translate Doaj journal list from csv file to json file
		new DataDoaj().csvtojson(doaj_csvfile, doaj_jsonfile);
		
		// extract issn list from Doaj csv jounral list
		new DataDoaj().issnlist(doaj_csvfile, doaj_issnfile);
		
		
		
		// Ulrich
		String ulrich_csvfile = prefix + "Ulrich/Ulrich_newfile_16July.csv";
		String ulrich_selectedfile = prefix + "Ulrich/Ulrich_selected.csv";
		String ulrich_dedupfile = prefix + "Ulrich/Ulrich_deduplicated.csv";
		String ulrich_modifiedfile = prefix + "Ulrich/Ulrich_modified.csv";
		String ulrich_jsonfile = prefix + "Ulrich/Ulrich_modified.json";
		
		// display all language strings in origin Ulrich csv file
		new DataUlrich().display_language(ulrich_csvfile);
		
		// select some fields (issn, title, publisher, languages,...) in a new csv file
		new DataUlrich().selectfield(ulrich_csvfile, ulrich_selectedfile);
		
		// display the duplication in the new csv file
		new DataUlrich().display_duplication(ulrich_selectedfile);
		
		// deduplicate the same journals in the new csv file
		new DataUlrich().csvdedup(ulrich_selectedfile, ulrich_dedupfile);
		
		// separate the languages in text and abstract languages
		new DataUlrich().separate_language(ulrich_dedupfile, ulrich_modifiedfile);
		
		// display all language strings in new Ulrich csv file
		new DataUlrich().language_analyse(ulrich_modifiedfile);
		
		// analyse les issn format in Ulrich journal list
		new DataUlrich().issn_analyse(ulrich_modifiedfile);
		
		// translate Ulrich journal list from csv file to json file
		new DataUlrich().csvtojson(ulrich_modifiedfile, ulrich_jsonfile);
		*/
		
		// Munual
		String manual_langcsvfile = prefix + "1journal/standard/manual_language.csv";
		String manual_langjsonfile = prefix + "1journal/standard/manual_language.json";
		
		String manual_scielofile = prefix + "1journal/other_journal/scielojournalslist.csv";
		String manual_scielodedupfile = prefix + "1journal/other_journal/scielojournalslist_modif.csv";
		List<String> manual_newjnlslist = new ArrayList<String>();
		manual_newjnlslist.add(prefix+"1journal/other_journal/prob_pr2.csv");
		manual_newjnlslist.add(prefix+"1journal/other_journal/1journal_manual_pr_modif.csv");
		manual_newjnlslist.add(prefix+"1journal/other_journal/scielojournalslist_modif.csv");
		String manual_newjnsljsonfile = prefix + "1journal/other_journal/others.json";
		String manual_newjnsldedupfile = prefix + "1journal/other_journal/others_deduplicated.json";
		
		String manual_samejnlscsvfile = prefix + "1journal/standard/manual_samejournal";
		String manual_samejnlsjsonfile = prefix + "1journal/standard/manual_samejournal.json";
		String manual_fauxtitle = prefix + "1journal/standard/samejournal_non";
		
		
		// translate the manual language reference from csv file to json file
		new DataManual().reflangtojson(manual_langcsvfile, manual_langjsonfile);
		
		// deduplicate scielo journal csv list
		new DataManual().dedupscielocsv(manual_scielofile, manual_scielodedupfile);
		
		// translate the manual new PR journal list from csv file to json file
		new DataManual().newjourtojson(manual_newjnlslist, manual_newjnsljsonfile);
		
		// deduplicate the new journal list
		new Deduplication().jsondedup(manual_newjnsljsonfile, manual_newjnsldedupfile);
		
		// translate the manual same-journal list from csv file to json file
		new DataManual().samejourtojson(manual_samejnlscsvfile, manual_samejnlsjsonfile, manual_fauxtitle);
		
		
		
		/*
		// Adat
		String adat_csvfile = prefix + "Adat/ADAT-List_2.csv";
		String adat_jsonfile = prefix + "Adat/ADAT-List_2.json";
		
		// import the journals with language in Adat csv list into a json file
		new DataAdat().csvtojson(adat_csvfile, adat_jsonfile);
		
		
		
		// Caj
		String caj_csvfile = prefix + "CAJ/CAJ.csv";
		String caj_jsonfile = prefix + "CAJ/CAJ.json";
		
		// import the journals in Caj csv list into a json file
		new DataCaj().csvtojson(caj_csvfile, caj_jsonfile);
		
		
		
		// Copernicus
		String copernicus_csvfile = prefix + "Copernicus/copernicus_journals.csv";
		String copernicus_jsonfile = prefix + "Copernicus/copernicus.json";
		
		// import the journals in Copernicus csv list into a json file
		new DataCopernicus().csvtojson(copernicus_csvfile, copernicus_jsonfile);
		
		
		
		// Jstage
		String jstage_csvfile = prefix + "jstage/journals_list_en.csv";
		String jstage_jsonfile = prefix + "jstage/jstage.json";
		
		// import the journals with issn in Jstage csv list into a json file
		new DataJstage().csvtojson(jstage_csvfile, jstage_jsonfile);
		
		
		
		// Karger
		String karger_csvfile = prefix + "Karger/karger_journals_2013USD.csv";
		String karger_jsonfile = prefix + "Karger/karger.json";
		
		// import the journals in Karger csv list into a json file
		new DataKarger().csvtojson(karger_csvfile, karger_jsonfile);
		
		
		
		// Srp
		String srp_csvfile = prefix + "SRP/SRP_JOURNALS_PRICE_LIST.csv";
		String srp_jsonfile = prefix + "SRP/SRP.json";
		
		// import the journals in Srp csv list into a json file
		new DataSrp().csvtojson(srp_csvfile, srp_jsonfile);
		
		
		
		// Epm
		String epm_csvfile = prefix + "EPM/dump_catalogue_EPM.csv";
		String epm_jsonfile = prefix + "EPM/EPM.json";
		
		// import the journals in Epm csv list into a json file
		new DataEpm().csvtojson(epm_csvfile, epm_jsonfile);
		
		
		
		// Road
		String road_csvfile = prefix + "Road/road_parsed2016-02-05.csv";
		String road_jsonfile = prefix + "Road/road.json";
		
		// import the journals in Road csv list into a json file
		new DataRoad().csvtojson(road_csvfile, road_jsonfile);
		
		
		
		// British_Library
		String britlib_csvfile = prefix + "British_Library/British_Library.csv";
		String britlib_jsonfile = prefix + "British_Library/British_Library.json";
		String britlib_modifiedfile = prefix + "British_Library/British_Library_modified.json";
		
		// import the journals in British Library csv list into a json file
		new DataBritlib().csvtojson(britlib_csvfile, britlib_jsonfile);
		
		// separate the language string into set<String> in json file
		new DataBritlib().separatelangs(britlib_jsonfile, britlib_modifiedfile);
		
		
		
		// Loc
		List<String> loc_filelist = new ArrayList<String>();
		loc_filelist.add(prefix + "scrapy/2016-01-13T21-26-05.json");
		loc_filelist.add(prefix + "scrapy/2016-01-15T12-45-44.json");
		loc_filelist.add(prefix + "scrapy/2016-01-18T15-43-51.json");
		loc_filelist.add(prefix + "scrapy/2016-01-18T15-53-34.json");
		String loc_jsonfile = prefix + "loc.json";
		String loc_modifiedfile = prefix + "loc_modified.json";
		
		// merge several json files in a new single json file
		new DataLoc().mergejsonfiles(loc_filelist, loc_jsonfile);
		
		// separate the language string into set<String> in json file
		new DataLoc().separatelangs(loc_jsonfile, loc_modifiedfile);
		
		
		
		// worldcat
		String worldcat_csvpart1 = prefix + "worldcat/Journal_Titles_A-L.csv";
		String worldcat_csvpart2 = prefix + "worldcat/Journal_Titles_M-Z.csv";
		String worldcat_csvfile = prefix + "worldcat/Journal_Titles.csv";
		String worldcat_issn_anormal = prefix + "worldcat/Journal_issn_anormal.txt";
		String worldcat_correctedfile = prefix + "worldcat/Journal_Titles_corrected.csv";
		String worldcat_issn_correction = prefix + "worldcat/Journal_issn_co.txt";
		String worldcat_issnlist = prefix + "worldcat/Journal_issns.txt";
		
		// merge 2 csv files into a single csv file
		new DataWorldcat().mergecsvfile(worldcat_csvpart1, worldcat_csvpart2, worldcat_csvfile);
		
		// diaplay the issn not correct from worldcat csv
		new DataWorldcat().issn_anormal(worldcat_csvfile, worldcat_issn_anormal);
		
		// correct some issn to standard format in worldcat list
		new DataWorldcat().correctlist(worldcat_csvfile, worldcat_issn_correction, worldcat_correctedfile);
		
		// build the issn list from worldcat list
		new DataWorldcat().issnlist(worldcat_correctedfile, worldcat_issnlist);
		*/

	}
	
	
	
	

}
