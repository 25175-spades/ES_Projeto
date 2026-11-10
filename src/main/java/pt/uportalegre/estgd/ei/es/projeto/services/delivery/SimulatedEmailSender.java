package pt.uportalegre.estgd.ei.es.projeto.services.delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;

import pt.uportalegre.estgd.ei.es.projeto.domain.Notification;
import pt.uportalegre.estgd.ei.es.projeto.repositories.DeliveryAttemptRepository;

@Service
@Profile("simulate-mail")
public class SimulatedEmailSender implements NotificationSender {

    @Autowired
    private DeliveryAttemptRepository attemptRepository;

    @Value("${notifications.simulated-failures:0}")
    private int simulatedFailures;

    @Override
    public void send(Notification notification) {
        long attemptCount = attemptRepository.countByNotificationId(
                notification.getId()
        );

        if (attemptCount < simulatedFailures) {
            throw new MailSendException("Falha de envio simulada");
        }

        // Se o método terminar sem exceção, o processador regista sucesso.
    }
}