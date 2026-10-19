package pt.uportalegre.estgd.ei.es.projeto.services.delivery;

import pt.uportalegre.estgd.ei.es.projeto.domain.Notification;

public interface NotificationSender {
    void send(Notification notification);
}
