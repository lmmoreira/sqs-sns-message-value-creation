package br.com.ccrs.logistics.fleet.order.acceptance.exception;

public class EventHandlerMappingException extends OrderAcceptanceException {

    public EventHandlerMappingException(Exception e) {
        super(e);
    }

}
