package br.com.ccrs.logistics.fleet.order.acceptance.handler.model;

import java.time.ZonedDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegionChangeEvent {

    @JsonProperty("uuid")
    private String regionUuid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("attributes")
    private Map<String, String> attributes;

    @JsonProperty("lastModifiedDate")
    private ZonedDateTime lastModifiedDate;

}
