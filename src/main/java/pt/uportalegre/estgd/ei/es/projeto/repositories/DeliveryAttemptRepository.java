package pt.uportalegre.estgd.ei.es.projeto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.uportalegre.estgd.ei.es.projeto.domain.DeliveryAttempt;

import java.util.List;

public interface DeliveryAttemptRepository
        extends JpaRepository<DeliveryAttempt, Long> {

    long countByNotificationId(Long notificationId);

    List<DeliveryAttempt> findByNotificationIdOrderByAttemptedAtAscIdAsc(
            Long notificationId
    );
}
