package br.com.ccrs.logistics.fleet.order.acceptance.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import java.time.ZonedDateTime;
import java.util.Currency;

import br.com.ccrs.logistics.fleet.order.acceptance.model.PaymentType;
import lombok.Getter;

@Getter
public final class OrderDTO {

    private final String trackableSourceName;
    private final String externalId;
    private final ZonedDateTime createdDate;
    private final LocalityDTO origin;
    private final LocalityDTO destination;
    private final PaymentType paymentType;
    private final Long value;
    private final Long deliveryFee;
    private final Currency currency;
    private final String orderType;

    private String tenantIdentifier;

    @JsonCreator
    public OrderDTO(@JsonProperty(value = "trackableSourceName", required = true) final String trackableSourceName,
                    @JsonProperty(value = "externalId", required = true) final String externalId,
                    @JsonProperty(value = "createdDate", required = true) final ZonedDateTime createdDate,
                    @JsonProperty(value = "origin", required = true) final LocalityDTO origin,
                    @JsonProperty(value = "destination", required = true) final LocalityDTO destination,
                    @JsonProperty(value = "paymentType", required = true) final PaymentType paymentType,
                    @JsonProperty(value = "value", required = true) final Long value,
                    @JsonProperty(value = "deliveryFee", required = true) final Long deliveryFee,
                    @JsonProperty(value = "orderType") final String orderType,
                    @JsonProperty(value = "currency", required = true) final Currency currency) {
        this.trackableSourceName = trackableSourceName;
        this.externalId = externalId;
        this.createdDate = createdDate;
        this.origin = origin;
        this.destination = destination;
        this.paymentType = paymentType;
        this.value = value;
        this.deliveryFee = deliveryFee;
        this.currency = currency;
        this.orderType = orderType;
    }

    public void setTenantIdentifier(final String tenantIdentifier) {
        this.tenantIdentifier = tenantIdentifier;
    }
}
