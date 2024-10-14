package io.lynx.oebs.services.mail;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
public class SendGridEmailService implements EmailService {

    private final String apiKey;
    private final String defaultTemplateId;


    public SendGridEmailService(@Value("${sendgrid.api.key}") String apiKey,
                                @Value("${sendgrid.template.id}") String defaultTemplateId) {
        this.apiKey = apiKey;
        this.defaultTemplateId = defaultTemplateId;
    }


    @Override
    public void sendEmail(String[] to, String templateId, Map<String, String> dynamicData) {
        // Use the provided templateId or fall back to the default template ID
        String resolvedTemplateId = templateId != null ? templateId : defaultTemplateId;// Prepare the SendGrid mail request
        Mail mail = getMail(to, dynamicData, resolvedTemplateId);
        // Send the email using SendGrid API
        log.info("api-key: {}", apiKey);
        SendGrid sendGrid = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            log.info("email response code: {}", response.getStatusCode());
            log.info("email response header: {}", response.getHeaders());
            log.info("email response body: {}", response.getBody());
        } catch (IOException ex) {
            throw new RuntimeException("Failed to send email", ex);
        }
    }

    private Mail getMail(String[] to, Map<String, String> dynamicData, String resolvedTemplateId) {
        String subject = "Sending with Twilio SendGrid is Fun";
        Content content = new Content("text/html", String.format("and <em>%s</em> to do anywhere with <strong>Java</strong>", dynamicData.get("otp")));
        Email from = new Email("niomwungeri.dev@gmail.com"); // Replace with your SendGrid verified sender email
        Mail mail = null;
        for (String t : to) {
            mail = new Mail(from, subject, new Email(t), content);
        }
        return mail;
    }
}
