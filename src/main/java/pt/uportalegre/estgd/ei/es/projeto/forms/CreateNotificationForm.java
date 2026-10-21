package pt.uportalegre.estgd.ei.es.projeto.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateNotificationForm {
    @NotBlank(message = "Indica pelo menos um destinatário")
    private String recipients;

    @NotBlank(message = "O assunto é obrigatório")
    @Size(max = 255, message = "O assunto pode ter até 255 caracteres")
    private String subject;

    @NotBlank(message = "O conteúdo é obrigatório")
    private String content;

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
