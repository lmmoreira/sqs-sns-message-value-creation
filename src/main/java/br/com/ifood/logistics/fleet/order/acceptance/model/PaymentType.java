package br.com.ccrs.logistics.fleet.order.acceptance.model;

import java.util.Comparator;

public enum PaymentType {

    CARD(true),
    CHECK(false),
    MONEY(false),
    ONLINE(false),
    TICKET(true),
    OTHER(false),
    COMPLEMENT(false);

    private final boolean paymentAtDelivery;

    PaymentType(final boolean paymentAtDelivery) {
        this.paymentAtDelivery = paymentAtDelivery;
    }

    public boolean isPaymentAtDelivery() {
        return paymentAtDelivery;
    }


    public static final Comparator<PaymentType> paymentAtDeliveryFirstComparator = (p1,p2) -> {
        if (p1.isPaymentAtDelivery() == p2.isPaymentAtDelivery()) {
            return 0;
        }
        return p1.isPaymentAtDelivery() ? -1 : 1;
    };

}
