
package com.onescience.journal.schema_journal;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * The related journal
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "relation",
    "title",
    "issn"
})
public class RelatedJournal {

    @JsonProperty("relation")
    private String relation;
    @JsonProperty("title")
    private String title;
    @JsonProperty("issn")
    private String issn;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public RelatedJournal() {
    }

    /**
     * 
     * @param title
     * @param issn
     * @param relation
     */
    public RelatedJournal(String relation, String title, String issn) {
        this.relation = relation;
        this.title = title;
        this.issn = issn;
    }

    /**
     * 
     * @return
     *     The relation
     */
    @JsonProperty("relation")
    public String getRelation() {
        return relation;
    }

    /**
     * 
     * @param relation
     *     The relation
     */
    @JsonProperty("relation")
    public void setRelation(String relation) {
        this.relation = relation;
    }

    public RelatedJournal withRelation(String relation) {
        this.relation = relation;
        return this;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public RelatedJournal withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 
     * @return
     *     The issn
     */
    @JsonProperty("issn")
    public String getIssn() {
        return issn;
    }

    /**
     * 
     * @param issn
     *     The issn
     */
    @JsonProperty("issn")
    public void setIssn(String issn) {
        this.issn = issn;
    }

    public RelatedJournal withIssn(String issn) {
        this.issn = issn;
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

    public RelatedJournal withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
