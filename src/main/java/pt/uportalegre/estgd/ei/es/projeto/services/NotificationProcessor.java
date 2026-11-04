package pt.uportalegre.estgd.ei.es.projeto.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import pt.uportalegre.estgd.ei.es.projeto.domain.DeliveryAttempt;
import pt.uportalegre.estgd.ei.es.projeto.domain.Notification;
import pt.uportalegre.estgd.ei.es.projeto.domain.NotificationStatus;
import pt.uportalegre.estgd.ei.es.projeto.repositories.DeliveryAttemptRepository;
import pt.uportalegre.estgd.ei.es.projeto.repositories.NotificationRepository;
import pt.uportalegre.estgd.ei.es.projeto.services.delivery.NotificationSender;

import java.util.List;

public class NotificationProcessor {

    private static final Logger logger = LoggerFactory.getLogger(NotificationProcessor.class);

    @Autowired
    private NotificationRepository repository;

    @Autowired
    private DeliveryAttemptRepository attemptRepository;

    @Autowired
    private NotificationSender sender;

    @Value("${notifications.max-attempts:3}")
    private int maxAttempts;

    @Scheduled(
            fixedDelayString = "${notifications.processing-delay-ms:10000}"
    )
    public void processNotifications() {

        List<Notification> notifications =
                repository.findByStatusIn(
                        List.of(NotificationStatus.PENDING,
                                NotificationStatus.FAILED)
                );

        for (Notification notification : notifications) {
            try {
                process(notification);
            } catch (RuntimeException e) {
                // Um erro numa notificação não interrompe as restantes.
                logger.error(
                        "Erro ao processar a notificação {}",
                        notification.getId(),
                        e
                );
            }
        }
    }

    private void process(Notification notification) {

        long attemptCount =
                attemptRepository.countByNotificationId(
                        notification.getId()
                );

        if (attemptCount >= maxAttempts) {
            return;
        }

        try {
            sender.send(notification);
        }
        catch (MailException e) {
            attemptRepository.save(
                    new DeliveryAttempt(
                            notification,
                            false,
                            e.getMessage()
                    )
            );

            notification.markAsFailed();
            repository.save(notification);

            // Termina esta tentativa
            return;
        }

        attemptRepository.save(
                new DeliveryAttempt(notification, true, null)
        );

        notification.markAsSent();
        repository.save(notification);
    }
}
