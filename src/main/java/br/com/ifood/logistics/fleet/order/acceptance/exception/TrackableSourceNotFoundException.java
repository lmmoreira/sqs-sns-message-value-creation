package br.com.ccrs.logistics.fleet.order.acceptance.exception;

public class TrackableSourceNotFoundException extends OrderAcceptanceException {

    private static final long serialVersionUID = -1666030822366814367L;

    public TrackableSourceNotFoundException() {
        super();
    }

    public TrackableSourceNotFoundException(final String message) {
        super(message);
    }
}
