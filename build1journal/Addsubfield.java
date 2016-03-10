package com.onescience.journal.build1journal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.onescience.journal.methods.Journal_methods;
import com.onescience.journal.methods.Jsonfile;
import com.onescience.journal.schema_journal.Journal;
import com.opencsv.CSVReader;

public class Addsubfield {
	
	public Addsubfield(){
	}
	
	/**
	 * 
	 * add subfields of SM1 and SM2 to 1journal list, one subfield by Issn is accepted
	 * a manual reference (sfdmanualfile) is made for the journals with more than one subfield
	 */
	public void importSubfield(String jnlsfile, String sfdfile, String sfdmanualfile, String subfieddedfile){
		try{
			String[] line;
			HashMap<String, String> issn_sfd = new HashMap<String, String>();
			InputStreamReader in_sfd = new InputStreamReader(new FileInputStream(sfdfile), "UTF-8");
			CSVReader read_sfd = new CSVReader(in_sfd, ';', '"', 0);
			while ((line = read_sfd.readNext()) != null) {
				if ((!line[2].equals(""))&&(!line[2].equals("UNCLASSIFIED"))){
					if (!line[0].equals("")){
						issn_sfd.put(line[0], line[2]);
					}
					if (!line[1].equals("")){
						issn_sfd.put(line[1], line[2]);
					}
				}
			}
			read_sfd.close();
			in_sfd.close();
			
			InputStreamReader in_ref = new InputStreamReader(new FileInputStream(sfdmanualfile), "UTF-8");
			CSVReader read_ref = new CSVReader(in_ref, ':', '"', 0);
			while ((line = read_ref.readNext()) != null) {
				issn_sfd.remove(line[0]);
				issn_sfd.put(line[0], line[1]);
			}
			read_ref.close();
			in_ref.close();
			
			List<Journal> jnls = new Jsonfile().readfile(jnlsfile);
			HashMultimap<String, Integer> issn_nb = HashMultimap.create();
			for (int i=0; i<jnls.size(); i++){
				Set<String> issns = new Journal_methods().getIssnValues(jnls.get(i));
				for (String issn : issns){
					issn_nb.put(issn, i);
				}
			}
			
			int nb_getsfd = 0;
			int nb_samesfd = 0;
			int nb_comflit = 0;
			for (String issn : issn_sfd.keySet()){
				Set<String> sfds = new HashSet<String>();
				sfds.add(issn_sfd.get(issn));
				Set<Integer> nbs = issn_nb.get(issn);
				for (int nb : nbs){
					Set<String> sfds1 = jnls.get(nb).getSubfields();
					if (sfds1.size()==0){
						jnls.get(nb).setSubfields(sfds);
						nb_getsfd++;
					} else {
						if (sfds.equals(sfds1)){
							nb_samesfd++;
						} else {
							jnls.get(nb).setSubfields(sfds);
							nb_comflit++;
						}
					}
				}
			}
			System.out.println("nb jounrals get subfield: " + nb_getsfd);
			System.out.println("nb jounrals with same subfield: " + nb_samesfd);
			System.out.println("nb jounrals with different subfield: " + nb_comflit);
			new Jsonfile().writefile(sfdmanualfile, jnls);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
