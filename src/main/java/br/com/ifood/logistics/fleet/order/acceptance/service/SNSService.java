package br.com.ccrs.logistics.fleet.order.acceptance.service;

import java.util.Map;

public interface SNSService {
    void publish(String topic, String message, Map<String, String> attributes);
}
