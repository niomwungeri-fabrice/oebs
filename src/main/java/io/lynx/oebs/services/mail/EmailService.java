package io.lynx.oebs.services.mail;

import java.util.Map;

public interface EmailService {
    void sendEmail(String[] to, String templateId, Map<String, String> dynamicData);
}
