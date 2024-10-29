package com.accenture.sms.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    private final String apiKey = ""; // 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

    public void sendNotification(String to, String subject, String text) {
        Email from = new Email("bestsubmanager@outlook.com");
        Email toEmail = new Email(to);
        Content content = new Content("sub plan payment", text);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            System.out.println("Error sending email: " + ex.getMessage());
        }
    }
}
