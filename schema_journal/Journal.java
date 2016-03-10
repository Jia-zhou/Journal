
package com.onescience.journal.schema_journal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "source",
    "ids",
    "titles",
    "frequency",
    "subjects",
    "subfields",
    "languages",
    "textLanguages",
    "AbstractLanguages",
    "publisher",
    "issns",
    "relatedJournal",
    "links",
    "statusOA",
    "statusPR",
    "observation"
})
public class Journal {

    /**
     * Journal identifier
     * 
     */
    @JsonProperty("id")
    private String id;
    /**
     * Where the journal information has been found
     * (Required)
     * 
     */
    @JsonProperty("source")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<String> source = new LinkedHashSet<String>();
    /**
     * Different Id number
     * (Required)
     * 
     */
    @JsonProperty("ids")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Id> ids = new LinkedHashSet<Id>();
    /**
     * Journal title and their variants
     * 
     */
    @JsonProperty("titles")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<String> titles = new LinkedHashSet<String>();
    /**
     * Journal frequency
     * 
     */
    @JsonProperty("frequency")
    private String frequency;
    /**
     * Journal subjects
     * 
     */
    @JsonProperty("subjects")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<String> subjects = new LinkedHashSet<String>();
    /**
     * Journal subfields
     * 
     */
    @JsonProperty("subfields")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<String> subfields = new LinkedHashSet<String>();
    /**
     * The published languages of the journal
     * 
     */
    @JsonProperty("languages")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<String> languages = new LinkedHashSet<String>();
    /**
     * The languages of the journal's articles
     * 
     */
    @JsonProperty("textLanguages")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<String> textLanguages = new LinkedHashSet<String>();
    /**
     * The languages of the journal's articles' abstracts
     * 
     */
    @JsonProperty("AbstractLanguages")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<String> AbstractLanguages = new LinkedHashSet<String>();
    /**
     * Journal publisher
     * 
     */
    @JsonProperty("publisher")
    private Publisher publisher;
    /**
     * International Standard Serial Number
     * 
     */
    @JsonProperty("issns")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Issn> issns = new LinkedHashSet<Issn>();
    /**
     * The relation with other journals
     * 
     */
    @JsonProperty("relatedJournal")
    private List<RelatedJournal> relatedJournal = new ArrayList<RelatedJournal>();
    /**
     * Links
     * 
     */
    @JsonProperty("links")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Link> links = new LinkedHashSet<Link>();
    /**
     * Open access status
     * 
     */
    @JsonProperty("statusOA")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<StatusOA> statusOA = new LinkedHashSet<StatusOA>();
    /**
     * Peer-reviewed status
     * 
     */
    @JsonProperty("statusPR")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<StatusPR> statusPR = new LinkedHashSet<StatusPR>();
    /**
     * Additional notes
     * 
     */
    @JsonProperty("observation")
    private String observation;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Journal() {
    }

    /**
     * 
     * @param observation
     * @param statusOA
     * @param subfields
     * @param subjects
     * @param ids
     * @param textLanguages
     * @param links
     * @param frequency
     * @param publisher
     * @param id
     * @param languages
     * @param relatedJournal
     * @param source
     * @param statusPR
     * @param AbstractLanguages
     * @param titles
     * @param issns
     */
    public Journal(String id, Set<String> source, Set<Id> ids, Set<String> titles, String frequency, Set<String> subjects, Set<String> subfields, Set<String> languages, Set<String> textLanguages, Set<String> AbstractLanguages, Publisher publisher, Set<Issn> issns, List<RelatedJournal> relatedJournal, Set<Link> links, Set<StatusOA> statusOA, Set<StatusPR> statusPR, String observation) {
        this.id = id;
        this.source = source;
        this.ids = ids;
        this.titles = titles;
        this.frequency = frequency;
        this.subjects = subjects;
        this.subfields = subfields;
        this.languages = languages;
        this.textLanguages = textLanguages;
        this.AbstractLanguages = AbstractLanguages;
        this.publisher = publisher;
        this.issns = issns;
        this.relatedJournal = relatedJournal;
        this.links = links;
        this.statusOA = statusOA;
        this.statusPR = statusPR;
        this.observation = observation;
    }

    /**
     * Journal identifier
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Journal identifier
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public Journal withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Where the journal information has been found
     * (Required)
     * 
     * @return
     *     The source
     */
    @JsonProperty("source")
    public Set<String> getSource() {
        return source;
    }

    /**
     * Where the journal information has been found
     * (Required)
     * 
     * @param source
     *     The source
     */
    @JsonProperty("source")
    public void setSource(Set<String> source) {
        this.source = source;
    }

    public Journal withSource(Set<String> source) {
        this.source = source;
        return this;
    }

    /**
     * Different Id number
     * (Required)
     * 
     * @return
     *     The ids
     */
    @JsonProperty("ids")
    public Set<Id> getIds() {
        return ids;
    }

    /**
     * Different Id number
     * (Required)
     * 
     * @param ids
     *     The ids
     */
    @JsonProperty("ids")
    public void setIds(Set<Id> ids) {
        this.ids = ids;
    }

    public Journal withIds(Set<Id> ids) {
        this.ids = ids;
        return this;
    }

    /**
     * Journal title and their variants
     * 
     * @return
     *     The titles
     */
    @JsonProperty("titles")
    public Set<String> getTitles() {
        return titles;
    }

    /**
     * Journal title and their variants
     * 
     * @param titles
     *     The titles
     */
    @JsonProperty("titles")
    public void setTitles(Set<String> titles) {
        this.titles = titles;
    }

    public Journal withTitles(Set<String> titles) {
        this.titles = titles;
        return this;
    }

    /**
     * Journal frequency
     * 
     * @return
     *     The frequency
     */
    @JsonProperty("frequency")
    public String getFrequency() {
        return frequency;
    }

    /**
     * Journal frequency
     * 
     * @param frequency
     *     The frequency
     */
    @JsonProperty("frequency")
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Journal withFrequency(String frequency) {
        this.frequency = frequency;
        return this;
    }

    /**
     * Journal subjects
     * 
     * @return
     *     The subjects
     */
    @JsonProperty("subjects")
    public Set<String> getSubjects() {
        return subjects;
    }

    /**
     * Journal subjects
     * 
     * @param subjects
     *     The subjects
     */
    @JsonProperty("subjects")
    public void setSubjects(Set<String> subjects) {
        this.subjects = subjects;
    }

    public Journal withSubjects(Set<String> subjects) {
        this.subjects = subjects;
        return this;
    }

    /**
     * Journal subfields
     * 
     * @return
     *     The subfields
     */
    @JsonProperty("subfields")
    public Set<String> getSubfields() {
        return subfields;
    }

    /**
     * Journal subfields
     * 
     * @param subfields
     *     The subfields
     */
    @JsonProperty("subfields")
    public void setSubfields(Set<String> subfields) {
        this.subfields = subfields;
    }

    public Journal withSubfields(Set<String> subfields) {
        this.subfields = subfields;
        return this;
    }

    /**
     * The published languages of the journal
     * 
     * @return
     *     The languages
     */
    @JsonProperty("languages")
    public Set<String> getLanguages() {
        return languages;
    }

    /**
     * The published languages of the journal
     * 
     * @param languages
     *     The languages
     */
    @JsonProperty("languages")
    public void setLanguages(Set<String> languages) {
        this.languages = languages;
    }

    public Journal withLanguages(Set<String> languages) {
        this.languages = languages;
        return this;
    }

    /**
     * The languages of the journal's articles
     * 
     * @return
     *     The textLanguages
     */
    @JsonProperty("textLanguages")
    public Set<String> getTextLanguages() {
        return textLanguages;
    }

    /**
     * The languages of the journal's articles
     * 
     * @param textLanguages
     *     The textLanguages
     */
    @JsonProperty("textLanguages")
    public void setTextLanguages(Set<String> textLanguages) {
        this.textLanguages = textLanguages;
    }

    public Journal withTextLanguages(Set<String> textLanguages) {
        this.textLanguages = textLanguages;
        return this;
    }

    /**
     * The languages of the journal's articles' abstracts
     * 
     * @return
     *     The AbstractLanguages
     */
    @JsonProperty("AbstractLanguages")
    public Set<String> getAbstractLanguages() {
        return AbstractLanguages;
    }

    /**
     * The languages of the journal's articles' abstracts
     * 
     * @param AbstractLanguages
     *     The AbstractLanguages
     */
    @JsonProperty("AbstractLanguages")
    public void setAbstractLanguages(Set<String> AbstractLanguages) {
        this.AbstractLanguages = AbstractLanguages;
    }

    public Journal withAbstractLanguages(Set<String> AbstractLanguages) {
        this.AbstractLanguages = AbstractLanguages;
        return this;
    }

    /**
     * Journal publisher
     * 
     * @return
     *     The publisher
     */
    @JsonProperty("publisher")
    public Publisher getPublisher() {
        return publisher;
    }

    /**
     * Journal publisher
     * 
     * @param publisher
     *     The publisher
     */
    @JsonProperty("publisher")
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Journal withPublisher(Publisher publisher) {
        this.publisher = publisher;
        return this;
    }

    /**
     * International Standard Serial Number
     * 
     * @return
     *     The issns
     */
    @JsonProperty("issns")
    public Set<Issn> getIssns() {
        return issns;
    }

    /**
     * International Standard Serial Number
     * 
     * @param issns
     *     The issns
     */
    @JsonProperty("issns")
    public void setIssns(Set<Issn> issns) {
        this.issns = issns;
    }

    public Journal withIssns(Set<Issn> issns) {
        this.issns = issns;
        return this;
    }

    /**
     * The relation with other journals
     * 
     * @return
     *     The relatedJournal
     */
    @JsonProperty("relatedJournal")
    public List<RelatedJournal> getRelatedJournal() {
        return relatedJournal;
    }

    /**
     * The relation with other journals
     * 
     * @param relatedJournal
     *     The relatedJournal
     */
    @JsonProperty("relatedJournal")
    public void setRelatedJournal(List<RelatedJournal> relatedJournal) {
        this.relatedJournal = relatedJournal;
    }

    public Journal withRelatedJournal(List<RelatedJournal> relatedJournal) {
        this.relatedJournal = relatedJournal;
        return this;
    }

    /**
     * Links
     * 
     * @return
     *     The links
     */
    @JsonProperty("links")
    public Set<Link> getLinks() {
        return links;
    }

    /**
     * Links
     * 
     * @param links
     *     The links
     */
    @JsonProperty("links")
    public void setLinks(Set<Link> links) {
        this.links = links;
    }

    public Journal withLinks(Set<Link> links) {
        this.links = links;
        return this;
    }

    /**
     * Open access status
     * 
     * @return
     *     The statusOA
     */
    @JsonProperty("statusOA")
    public Set<StatusOA> getStatusOA() {
        return statusOA;
    }

    /**
     * Open access status
     * 
     * @param statusOA
     *     The statusOA
     */
    @JsonProperty("statusOA")
    public void setStatusOA(Set<StatusOA> statusOA) {
        this.statusOA = statusOA;
    }

    public Journal withStatusOA(Set<StatusOA> statusOA) {
        this.statusOA = statusOA;
        return this;
    }

    /**
     * Peer-reviewed status
     * 
     * @return
     *     The statusPR
     */
    @JsonProperty("statusPR")
    public Set<StatusPR> getStatusPR() {
        return statusPR;
    }

    /**
     * Peer-reviewed status
     * 
     * @param statusPR
     *     The statusPR
     */
    @JsonProperty("statusPR")
    public void setStatusPR(Set<StatusPR> statusPR) {
        this.statusPR = statusPR;
    }

    public Journal withStatusPR(Set<StatusPR> statusPR) {
        this.statusPR = statusPR;
        return this;
    }

    /**
     * Additional notes
     * 
     * @return
     *     The observation
     */
    @JsonProperty("observation")
    public String getObservation() {
        return observation;
    }

    /**
     * Additional notes
     * 
     * @param observation
     *     The observation
     */
    @JsonProperty("observation")
    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Journal withObservation(String observation) {
        this.observation = observation;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Journal withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
