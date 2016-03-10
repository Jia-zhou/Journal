
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "idType",
    "idNumber"
})
public class Id {

    @JsonProperty("idType")
    private String idType;
    @JsonProperty("idNumber")
    private String idNumber;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Id() {
    }

    /**
     * 
     * @param idType
     * @param idNumber
     */
    public Id(String idType, String idNumber) {
        this.idType = idType;
        this.idNumber = idNumber;
    }

    /**
     * 
     * @return
     *     The idType
     */
    @JsonProperty("idType")
    public String getIdType() {
        return idType;
    }

    /**
     * 
     * @param idType
     *     The idType
     */
    @JsonProperty("idType")
    public void setIdType(String idType) {
        this.idType = idType;
    }

    public Id withIdType(String idType) {
        this.idType = idType;
        return this;
    }

    /**
     * 
     * @return
     *     The idNumber
     */
    @JsonProperty("idNumber")
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * 
     * @param idNumber
     *     The idNumber
     */
    @JsonProperty("idNumber")
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Id withIdNumber(String idNumber) {
        this.idNumber = idNumber;
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

    public Id withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
