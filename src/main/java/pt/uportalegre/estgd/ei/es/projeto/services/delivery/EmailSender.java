package pt.uportalegre.estgd.ei.es.projeto.services.delivery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pt.uportalegre.estgd.ei.es.projeto.domain.Notification;
import pt.uportalegre.estgd.ei.es.projeto.domain.Recipient;

@Service
public class EmailSender implements NotificationSender {

    private final JavaMailSender mailSender;
    private final String from;

    public EmailSender(
            JavaMailSender mailSender,
            @Value("${spring.mail.username}") String from) {

        this.mailSender = mailSender;
        this.from = from;
    }

    @Override
    public void send(Notification notification) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(
                // Recebe a lista de destinatários e converte-a num array
                notification.getRecipients().stream().map(Recipient::getAddress).toArray(String[]::new)
        );
        message.setSubject(notification.getSubject());
        message.setText(notification.getContent());

        mailSender.send(message);
    }
}
