package pt.uportalegre.estgd.ei.es.projeto.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pt.uportalegre.estgd.ei.es.projeto.domain.DeliveryAttempt;
import pt.uportalegre.estgd.ei.es.projeto.domain.Notification;
import pt.uportalegre.estgd.ei.es.projeto.domain.NotificationStatus;
import pt.uportalegre.estgd.ei.es.projeto.repositories.DeliveryAttemptRepository;
import pt.uportalegre.estgd.ei.es.projeto.repositories.NotificationRepository;
import pt.uportalegre.estgd.ei.es.projeto.services.delivery.NotificationSender;

import java.util.List;

@Service
public class NotificationProcessor {

    private static final Logger logger = LoggerFactory.getLogger(NotificationProcessor.class);

    @Autowired
    private NotificationService service;

    @Autowired
    private NotificationSender sender;

    @Value("${notifications.max-attempts:3}")
    private int maxAttempts;

    @Scheduled(
            fixedDelayString = "${notifications.processing-delay-ms:10000}"
    )
    public void processNotifications() {

        List<Notification> pendingNotifications = service.findPendingNotifications();

        for (Notification notification : pendingNotifications) {
            try {
                process(notification);
            } catch (RuntimeException e) {
                // Um erro numa notificação não interrompe as restantes
                logger.error(
                        "Erro ao processar a notificação {}", notification.getId(), e
                );
            }
        }
    }

    private void process(Notification notification) {

        long attemptCount = service.countAttempts(notification.getId());

        if (attemptCount >= maxAttempts) {
            return;
        }

        try {
            sender.send(notification);
        } catch (MailException e) {
            boolean retry = e instanceof MailSendException
                    && attemptCount + 1 < maxAttempts;

            service.recordAttempt(notification, false, e.getMessage(), retry);
            return;
        }

        service.recordAttempt(notification, true, null, false);
    }
}
