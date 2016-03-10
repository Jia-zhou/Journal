package com.onescience.journal.buildthesaurus;

import java.util.List;

import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Journal;

public class Buildissnlink {
	
	public Buildissnlink(){
	}
	
	public void locissnlink(String jnlsfile){
		try {
			List<Journal> jnls = new Jsonfile().readfile(jnlsfile);
			for (int i=0; i<jnls.size(); i++){
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
