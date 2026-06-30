package com.codewithsakkol.wizard.store.payments;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
@AllArgsConstructor
@Data
public class WebhookRequest {
    private Map<String, String> header;
    private String payload;

}
