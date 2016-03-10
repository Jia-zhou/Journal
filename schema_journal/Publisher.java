
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
 * Journal publisher
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "editor",
    "name",
    "place",
    "date"
})
public class Publisher {

    @JsonProperty("editor")
    private String editor;
    @JsonProperty("name")
    private String name;
    @JsonProperty("place")
    private String place;
    @JsonProperty("date")
    private String date;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Publisher() {
    }

    /**
     * 
     * @param editor
     * @param name
     * @param date
     * @param place
     */
    public Publisher(String editor, String name, String place, String date) {
        this.editor = editor;
        this.name = name;
        this.place = place;
        this.date = date;
    }

    /**
     * 
     * @return
     *     The editor
     */
    @JsonProperty("editor")
    public String getEditor() {
        return editor;
    }

    /**
     * 
     * @param editor
     *     The editor
     */
    @JsonProperty("editor")
    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Publisher withEditor(String editor) {
        this.editor = editor;
        return this;
    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Publisher withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 
     * @return
     *     The place
     */
    @JsonProperty("place")
    public String getPlace() {
        return place;
    }

    /**
     * 
     * @param place
     *     The place
     */
    @JsonProperty("place")
    public void setPlace(String place) {
        this.place = place;
    }

    public Publisher withPlace(String place) {
        this.place = place;
        return this;
    }

    /**
     * 
     * @return
     *     The date
     */
    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    /**
     * 
     * @param date
     *     The date
     */
    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    public Publisher withDate(String date) {
        this.date = date;
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

    public Publisher withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
