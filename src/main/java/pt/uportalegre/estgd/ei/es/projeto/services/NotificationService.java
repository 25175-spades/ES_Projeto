package pt.uportalegre.estgd.ei.es.projeto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.uportalegre.estgd.ei.es.projeto.domain.DeliveryAttempt;
import pt.uportalegre.estgd.ei.es.projeto.domain.Notification;
import pt.uportalegre.estgd.ei.es.projeto.domain.Recipient;
import pt.uportalegre.estgd.ei.es.projeto.repositories.DeliveryAttemptRepository;
import pt.uportalegre.estgd.ei.es.projeto.repositories.NotificationRepository;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repository;

    @Autowired
    private DeliveryAttemptRepository attemptRepository;

    public Notification create(String subject, String content, List<Recipient> recipients) {

        // Cria a notificação
        Notification notification = new Notification(subject, content, recipients);

        // Regista na db
        return repository.save(notification);
    }

    @Transactional
    public void recordAttempt(Notification notification, boolean successful, String errorMessage, boolean retry) {

        attemptRepository.save(
                new DeliveryAttempt(notification, successful, errorMessage)
        );

        if (successful) {
            // Guarda a data de envio
            notification.markAsSent();
        } else if (!retry) {
            notification.markAsFailed();
        }

        repository.save(notification);
    }

    // Devolve a lista de notificações existentes
    public List<Notification> findAll() {
        return repository.findAll();
    }

    // Devolve uma notificação correspondente a um id
    public Notification findById(Long id) {
        return repository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Notification not found: " + id)
        );
    }

    // Devolve o histórico de tentativas
    public List<DeliveryAttempt> findAttempts(Long notificationId) {
        return attemptRepository
                .findByNotificationIdOrderByAttemptedAtAscIdAsc(notificationId);
    }
}
