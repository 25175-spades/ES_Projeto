package pt.uportalegre.estgd.ei.es.projeto.services;

import org.springframework.stereotype.Service;
import pt.uportalegre.estgd.ei.es.projeto.domain.Notification;
import pt.uportalegre.estgd.ei.es.projeto.domain.Recipient;
import pt.uportalegre.estgd.ei.es.projeto.repositories.NotificationRepository;
import pt.uportalegre.estgd.ei.es.projeto.services.delivery.NotificationSender;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repository;
    private final NotificationSender sender;

    public NotificationService(NotificationRepository repository, NotificationSender sender) {
        this.repository = repository;
        this.sender = sender;
    }

    public Notification createAndSend(String subject, String content, List<Recipient> recipients) {

        // Cria a notificação
        Notification notification = new Notification(subject, content, recipients);

        // Regista na db
        notification = repository.save(notification);

        // Chama o envio
        sender.send(notification);

        // Guarda a data de envio
        notification.markAsSent();
        return repository.save(notification);
    }

    // Devolve a lista completa de notificações existentes
    public List<Notification> findAll() {
        return repository.findAll();
    }

    // Devolve uma notificação correspondente ao id
    public Notification findById(Long id) {
        return repository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Notification not found: " + id)
        );
    }
}
