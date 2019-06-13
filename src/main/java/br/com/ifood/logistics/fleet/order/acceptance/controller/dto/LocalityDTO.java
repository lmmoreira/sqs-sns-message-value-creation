package br.com.ccrs.logistics.fleet.order.acceptance.controller.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public final class LocalityDTO {

    private String name;

    private String externalId;

    private String streetAddress;

    private String districtName;

    private String cityName;

    private String stateName;

    private String stateCode;

    private String countryName;

    private String countryCode;

    private Double latitude;

    private Double longitude;

    private ZonedDateTime externalCreatedDate;

    @JsonCreator
    public LocalityDTO(@JsonProperty(value = "name", required = false) final String name,
                    @JsonProperty(value = "externalId", required = true) final String externalId,
                    @JsonProperty(value = "streetAddress", required = true) final String streetAddress,
                    @JsonProperty(value = "districtName", required = true) final String districtName,
                    @JsonProperty(value = "cityName", required = true) final String cityName,
                    @JsonProperty(value = "stateName", required = true) final String stateName,
                    @JsonProperty(value = "stateCode", required = true) final String stateCode,
                    @JsonProperty(value = "countryName", required = true) final String countryName,
                       @JsonProperty(value = "countryCode", required = true) final String countryCode,
                       @JsonProperty(value = "latitude", required = false) final Double latitude,
                       @JsonProperty(value = "longitude", required = false) final Double longitude,
                       @JsonProperty(value = "externalCreatedDate", required = true) final ZonedDateTime externalCreatedDate) {
        this.name = name;
        this.externalId = externalId;
        this.streetAddress = streetAddress;
        this.districtName = districtName;
        this.cityName = cityName;
        this.stateName = stateName;
        this.stateCode = stateCode;
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.countryCode = countryCode;
        this.externalCreatedDate = externalCreatedDate;
    }

    public String getName() {
        return name;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getDistrictName() {
        return districtName;
    }

    public String getCityName() {
        return cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public ZonedDateTime getExternalCreatedDate() {
        return externalCreatedDate;
    }

}
