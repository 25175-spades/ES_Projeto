package pt.uportalegre.estgd.ei.es.projeto.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_attempt")
public class DeliveryAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @Column(name = "attempted_at", nullable = false, updatable = false)
    private LocalDateTime attemptedAt;

    @Column(nullable = false)
    private boolean successful;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    protected DeliveryAttempt() {

    }

    public DeliveryAttempt(Notification notification, Boolean successful, String errorMessage) {

        this.notification = notification;
        this.attemptedAt = LocalDateTime.now();
        this.successful = successful;
        this.errorMessage = successful ? null : errorMessage;
    }

    public long getId() {
        return id;
    }

    public Notification getNotification() {
        return notification;
    }

    public LocalDateTime getAttemptedAt() {
        return attemptedAt;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
