package br.com.ccrs.logistics.fleet.order.acceptance.exception;

public class SNSPublishingException extends RuntimeException {

    private static final long serialVersionUID = 6153096947226665378L;

    public SNSPublishingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
