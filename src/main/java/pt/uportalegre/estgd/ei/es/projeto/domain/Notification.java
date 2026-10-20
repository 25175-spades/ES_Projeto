package pt.uportalegre.estgd.ei.es.projeto.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column (nullable = false)
    private String subject;

    @Column (nullable = false, columnDefinition = "TEXT")
    private String content;

    @ElementCollection
    @CollectionTable(
            name = "notification_recipient",
            joinColumns = @JoinColumn(name = "notification_id")
    )
    private List<Recipient> recipients = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    protected Notification() {

    }

    public Notification(String subject, String content, List<Recipient> recipients) {
        this.subject = subject;
        this.content = content;
        this.createdAt = LocalDateTime.now();

        this.recipients = new ArrayList<>(recipients);
    }

    public void markAsSent() {
        this.sentAt = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public List<Recipient> getRecipients() {
        return List.copyOf(recipients);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }
}
