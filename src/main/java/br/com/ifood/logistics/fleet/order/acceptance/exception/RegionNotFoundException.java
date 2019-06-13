package br.com.ccrs.logistics.fleet.order.acceptance.exception;

public class RegionNotFoundException extends OrderAcceptanceException {

    private static final long serialVersionUID = -1666030822366814367L;

    public RegionNotFoundException() {
        super();
    }

    public RegionNotFoundException(final String message) {
        super(message);
    }
}
