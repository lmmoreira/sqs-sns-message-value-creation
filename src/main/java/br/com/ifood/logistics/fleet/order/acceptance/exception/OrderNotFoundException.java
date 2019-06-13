package br.com.ccrs.logistics.fleet.order.acceptance.exception;

public class OrderNotFoundException extends OrderAcceptanceException {

    private static final long serialVersionUID = 6112177134204692295L;

    public OrderNotFoundException(final String message) {
        super(message);
    }

    public OrderNotFoundException() {
        super();
    }
}
