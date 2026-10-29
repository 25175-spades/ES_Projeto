package pt.uportalegre.estgd.ei.es.projeto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import pt.uportalegre.estgd.ei.es.projeto.domain.Notification;
import pt.uportalegre.estgd.ei.es.projeto.domain.Recipient;
import pt.uportalegre.estgd.ei.es.projeto.repositories.NotificationRepository;
import pt.uportalegre.estgd.ei.es.projeto.services.delivery.NotificationSender;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repository;

    @Autowired
    private NotificationSender sender;

    public Notification createAndSend(String subject, String content, List<Recipient> recipients) {

        // Cria a notificação
        Notification notification = new Notification(subject, content, recipients);

        // Regista na db
        notification = repository.save(notification);


        try {
            // Chama o envio
            sender.send(notification);
        }
        catch (MailException e) {
            notification.markAsFailed();

            throw e;
        }

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
