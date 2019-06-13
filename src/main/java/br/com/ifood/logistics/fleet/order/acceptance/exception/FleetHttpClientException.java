package br.com.ccrs.logistics.fleet.order.acceptance.exception;

public class FleetHttpClientException extends RuntimeException {

    private static final long serialVersionUID = -8393478855392773271L;

    public FleetHttpClientException(final String message) {
        super(message);
    }
    
}
