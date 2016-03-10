
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
    "oaType",
    "year",
    "note"
})
public class StatusOA {

    /**
     * open access status
     * 
     */
    @JsonProperty("status")
    private StatusOA.Status status;
    /**
     * open access status
     * 
     */
    @JsonProperty("oaType")
    private StatusOA.OaType oaType;
    /**
     * the beginning year of OA
     * 
     */
    @JsonProperty("year")
    private String year;
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
    public StatusOA() {
    }

    /**
     * 
     * @param oaType
     * @param status
     * @param year
     * @param note
     */
    public StatusOA(StatusOA.Status status, StatusOA.OaType oaType, String year, String note) {
        this.status = status;
        this.oaType = oaType;
        this.year = year;
        this.note = note;
    }

    /**
     * open access status
     * 
     * @return
     *     The status
     */
    @JsonProperty("status")
    public StatusOA.Status getStatus() {
        return status;
    }

    /**
     * open access status
     * 
     * @param status
     *     The status
     */
    @JsonProperty("status")
    public void setStatus(StatusOA.Status status) {
        this.status = status;
    }

    public StatusOA withStatus(StatusOA.Status status) {
        this.status = status;
        return this;
    }

    /**
     * open access status
     * 
     * @return
     *     The oaType
     */
    @JsonProperty("oaType")
    public StatusOA.OaType getOaType() {
        return oaType;
    }

    /**
     * open access status
     * 
     * @param oaType
     *     The oaType
     */
    @JsonProperty("oaType")
    public void setOaType(StatusOA.OaType oaType) {
        this.oaType = oaType;
    }

    public StatusOA withOaType(StatusOA.OaType oaType) {
        this.oaType = oaType;
        return this;
    }

    /**
     * the beginning year of OA
     * 
     * @return
     *     The year
     */
    @JsonProperty("year")
    public String getYear() {
        return year;
    }

    /**
     * the beginning year of OA
     * 
     * @param year
     *     The year
     */
    @JsonProperty("year")
    public void setYear(String year) {
        this.year = year;
    }

    public StatusOA withYear(String year) {
        this.year = year;
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

    public StatusOA withNote(String note) {
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

    public StatusOA withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Generated("org.jsonschema2pojo")
    public static enum OaType {

        GOLD("gold"),
        GREEN("green"),
        HYBRID("hybrid");
        private final String value;
        private final static Map<String, StatusOA.OaType> CONSTANTS = new HashMap<String, StatusOA.OaType>();

        static {
            for (StatusOA.OaType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private OaType(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static StatusOA.OaType fromValue(String value) {
            StatusOA.OaType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    @Generated("org.jsonschema2pojo")
    public static enum Status {

        YES("Yes"),
        NO("No"),
        UNKNOW("Unknow");
        private final String value;
        private final static Map<String, StatusOA.Status> CONSTANTS = new HashMap<String, StatusOA.Status>();

        static {
            for (StatusOA.Status c: values()) {
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
        public static StatusOA.Status fromValue(String value) {
            StatusOA.Status constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
