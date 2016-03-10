
package com.onescience.journal.schema_journal;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "status",
    "note"
})
public class StatusPR {

    /**
     * open access status
     * 
     */
    @JsonProperty("status")
    private StatusPR.Status status;
    /**
     * note related to this status: source for example
     * 
     */
    @JsonProperty("note")
    private String note;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public StatusPR() {
    }

    /**
     * 
     * @param status
     * @param note
     */
    public StatusPR(StatusPR.Status status, String note) {
        this.status = status;
        this.note = note;
    }

    /**
     * open access status
     * 
     * @return
     *     The status
     */
    @JsonProperty("status")
    public StatusPR.Status getStatus() {
        return status;
    }

    /**
     * open access status
     * 
     * @param status
     *     The status
     */
    @JsonProperty("status")
    public void setStatus(StatusPR.Status status) {
        this.status = status;
    }

    public StatusPR withStatus(StatusPR.Status status) {
        this.status = status;
        return this;
    }

    /**
     * note related to this status: source for example
     * 
     * @return
     *     The note
     */
    @JsonProperty("note")
    public String getNote() {
        return note;
    }

    /**
     * note related to this status: source for example
     * 
     * @param note
     *     The note
     */
    @JsonProperty("note")
    public void setNote(String note) {
        this.note = note;
    }

    public StatusPR withNote(String note) {
        this.note = note;
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

    public StatusPR withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Status {

        YES("Yes"),
        NO("No"),
        UNKNOW("Unknow");
        private final String value;
        private final static Map<String, StatusPR.Status> CONSTANTS = new HashMap<String, StatusPR.Status>();

        static {
            for (StatusPR.Status c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Status(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static StatusPR.Status fromValue(String value) {
            StatusPR.Status constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
