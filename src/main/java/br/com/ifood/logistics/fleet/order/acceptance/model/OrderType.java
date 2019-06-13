package br.com.ccrs.logistics.fleet.order.acceptance.model;

import com.google.common.collect.ImmutableSet;

import java.util.HashSet;
import java.util.Set;

public enum OrderType {
    OWN_DELIVERY(OrderProperty.OPTIMIZABLE,
            OrderProperty.CAN_GENERATE_COMPLEMENT,
            OrderProperty.CAN_GENERATE_OCCURRENCES,
            OrderProperty.FREIGHT_REQUIRED,
            OrderProperty.REQUIRES_PAYMENT,
            OrderProperty.SEND_CREATION_EVENTS),
    MARKET_PLACE(OrderProperty.TIMEOUT_TO_ALLOCATE,
            OrderProperty.TIMEOUT_TO_COMPLETE,
            OrderProperty.COMPLETE_MANUALLY,
            OrderProperty.HAS_DEFAULT_MARKET_PLACE_REGION,
            OrderProperty.CAN_ACCEPT_ORIGIN_WITH_DEFAULT_REGION),
    MARKET_PLACE_DELIVERY(OWN_DELIVERY),
    HYBRID(OrderProperty.OPTIMIZABLE,
            OrderProperty.CAN_GENERATE_COMPLEMENT,
            OrderProperty.CAN_GENERATE_OCCURRENCES,
            OrderProperty.FREIGHT_REQUIRED,
            OrderProperty.REQUIRES_PAYMENT,
            OrderProperty.NEEDS_DESTINATION_REGION_CHECK),
    ON_DEMAND(HYBRID, OrderProperty.NEEDS_PAYMENT_TYPE_CHECK),
    LOOP_DELIVERY(OWN_DELIVERY);


    private final Set<OrderProperty> orderProperties;

    OrderType(final OrderProperty... orderProperties) {
        this.orderProperties = ImmutableSet.copyOf(orderProperties);
    }

    OrderType(final OrderType orderType) {
        this.orderProperties = ImmutableSet.copyOf(orderType.orderProperties);
    }

    public boolean hasProperty(final OrderProperty orderProperty) {
        return orderProperties.contains(orderProperty);
    }

    OrderType(final OrderType orderType, OrderProperty... orderProperties) {
        final Set<OrderProperty> properties = new HashSet<>();
        properties.addAll(ImmutableSet.copyOf(orderType.orderProperties));
        properties.addAll(ImmutableSet.copyOf(orderProperties));
        this.orderProperties = ImmutableSet.copyOf(properties);
    }
}