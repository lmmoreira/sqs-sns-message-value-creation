package br.com.ccrs.logistics.fleet.order.acceptance.exception;

public class OrderAcceptanceException extends RuntimeException {

    private static final long serialVersionUID = -3939451819963698635L;

    public OrderAcceptanceException(Throwable e) {
        super(e);
    }

    public OrderAcceptanceException() {
        super();
    }

    public OrderAcceptanceException(final String message) {
        super(message);
    }
}
