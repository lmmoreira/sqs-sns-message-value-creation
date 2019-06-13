package br.com.ccrs.logistics.fleet.order.acceptance.handler.model;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCreatedEvent {

    private String orderUuid;
    private ZonedDateTime createdDate;

    @JsonCreator
    public OrderCreatedEvent(@JsonProperty(value = "orderUuid", required = true) final String orderUuid,
            @JsonProperty(value = "createdDate", required = true) final ZonedDateTime createdDate) {
        this.orderUuid = orderUuid;
        this.createdDate = createdDate;
    }
}
