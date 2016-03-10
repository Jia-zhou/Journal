package com.onescience.journal.build1journal;

import java.util.List;

import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Journal;

public class Addnewjournal {
	
	public Addnewjournal(){
	}
	
	// add all the journals in the json files of filelist in a new json file
	public void importJournal(List<String> filelist, String addedfile){
		try {
			List<Journal> jnls = new Jsonfile().readfile(filelist);
			new Jsonfile().writefile(addedfile, jnls);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
