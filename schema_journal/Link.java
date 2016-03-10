
package com.onescience.journal.schema_journal;

import java.net.URI;
import java.util.Date;
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


/**
 * Link Description Object (from RFC-4287: Atom Syndication Format)
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "href",
    "rel",
    "contentType",
    "contentLength",
    "checked",
    "cached"
})
public class Link {

    /**
     * An URI template, as defined by RFC 6570
     * (Required)
     * 
     */
    @JsonProperty("href")
    private URI href;
    /**
     * Relation to the target resource of the link
     * (Required)
     * 
     */
    @JsonProperty("rel")
    private Link.Rel rel;
    /**
     * Media type (as defined by RFC 2046) describing the link target
     * 
     */
    @JsonProperty("contentType")
    private String contentType;
    /**
     * Indicates an advisory length of the linked content in octets
     * 
     */
    @JsonProperty("contentLength")
    private String contentLength;
    /**
     * The date the link resource has been checked (return a 2xx code)
     * 
     */
    @JsonProperty("checked")
    private Date checked;
    /**
     * Indicates the resource has been cached
     * 
     */
    @JsonProperty("cached")
    private Boolean cached;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Link() {
    }

    /**
     * 
     * @param cached
     * @param contentType
     * @param rel
     * @param checked
     * @param contentLength
     * @param href
     */
    public Link(URI href, Link.Rel rel, String contentType, String contentLength, Date checked, Boolean cached) {
        this.href = href;
        this.rel = rel;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.checked = checked;
        this.cached = cached;
    }

    /**
     * An URI template, as defined by RFC 6570
     * (Required)
     * 
     * @return
     *     The href
     */
    @JsonProperty("href")
    public URI getHref() {
        return href;
    }

    /**
     * An URI template, as defined by RFC 6570
     * (Required)
     * 
     * @param href
     *     The href
     */
    @JsonProperty("href")
    public void setHref(URI href) {
        this.href = href;
    }

    public Link withHref(URI href) {
        this.href = href;
        return this;
    }

    /**
     * Relation to the target resource of the link
     * (Required)
     * 
     * @return
     *     The rel
     */
    @JsonProperty("rel")
    public Link.Rel getRel() {
        return rel;
    }

    /**
     * Relation to the target resource of the link
     * (Required)
     * 
     * @param rel
     *     The rel
     */
    @JsonProperty("rel")
    public void setRel(Link.Rel rel) {
        this.rel = rel;
    }

    public Link withRel(Link.Rel rel) {
        this.rel = rel;
        return this;
    }

    /**
     * Media type (as defined by RFC 2046) describing the link target
     * 
     * @return
     *     The contentType
     */
    @JsonProperty("contentType")
    public String getContentType() {
        return contentType;
    }

    /**
     * Media type (as defined by RFC 2046) describing the link target
     * 
     * @param contentType
     *     The contentType
     */
    @JsonProperty("contentType")
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Link withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Indicates an advisory length of the linked content in octets
     * 
     * @return
     *     The contentLength
     */
    @JsonProperty("contentLength")
    public String getContentLength() {
        return contentLength;
    }

    /**
     * Indicates an advisory length of the linked content in octets
     * 
     * @param contentLength
     *     The contentLength
     */
    @JsonProperty("contentLength")
    public void setContentLength(String contentLength) {
        this.contentLength = contentLength;
    }

    public Link withContentLength(String contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    /**
     * The date the link resource has been checked (return a 2xx code)
     * 
     * @return
     *     The checked
     */
    @JsonProperty("checked")
    public Date getChecked() {
        return checked;
    }

    /**
     * The date the link resource has been checked (return a 2xx code)
     * 
     * @param checked
     *     The checked
     */
    @JsonProperty("checked")
    public void setChecked(Date checked) {
        this.checked = checked;
    }

    public Link withChecked(Date checked) {
        this.checked = checked;
        return this;
    }

    /**
     * Indicates the resource has been cached
     * 
     * @return
     *     The cached
     */
    @JsonProperty("cached")
    public Boolean getCached() {
        return cached;
    }

    /**
     * Indicates the resource has been cached
     * 
     * @param cached
     *     The cached
     */
    @JsonProperty("cached")
    public void setCached(Boolean cached) {
        this.cached = cached;
    }

    public Link withCached(Boolean cached) {
        this.cached = cached;
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

    public Link withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Generated("org.jsonschema2pojo")
    public static enum Rel {

        SELF("self"),
        PDF("pdf"),
        CACHE("cache");
        private final String value;
        private final static Map<String, Link.Rel> CONSTANTS = new HashMap<String, Link.Rel>();

        static {
            for (Link.Rel c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Rel(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static Link.Rel fromValue(String value) {
            Link.Rel constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
