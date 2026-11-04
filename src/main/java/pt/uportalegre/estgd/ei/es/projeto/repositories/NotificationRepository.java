package pt.uportalegre.estgd.ei.es.projeto.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.uportalegre.estgd.ei.es.projeto.domain.Notification;
import pt.uportalegre.estgd.ei.es.projeto.domain.NotificationStatus;

import java.util.Collection;
import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    @EntityGraph(attributePaths = "recipients")
    List<Notification> findByStatusIn(
            Collection<NotificationStatus> statuses
    );
}
