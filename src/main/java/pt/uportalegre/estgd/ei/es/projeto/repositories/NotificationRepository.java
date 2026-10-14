package pt.uportalegre.estgd.ei.es.projeto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.uportalegre.estgd.ei.es.projeto.domain.Notification;

public interface NotificationRepository
    extends JpaRepository<Notification, Long> {

}
