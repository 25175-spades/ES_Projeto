package pt.uportalegre.estgd.ei.es.projeto.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pt.uportalegre.estgd.ei.es.projeto.domain.Recipient;

import java.util.Arrays;
import java.util.List;

public class CreateNotificationForm {
    private static final String EMAIL_PATTERN =
            "[A-Za-z0-9]+(?:[._%+-][A-Za-z0-9]+)*"
            + "@[A-Za-z0-9]+(?:-[A-Za-z0-9]+)*"
            + "(?:\\.[A-Za-z0-9]+(?:-[A-Za-z0-9]+)*)+";

    @NotBlank(message = "Indica pelo menos um destinatário")
    @Pattern(regexp = "\\s*" + EMAIL_PATTERN + "\\s*(?:;\\s*" + EMAIL_PATTERN + "\\s*)*",
            message = "Indica emails válidos separados por ponto e vírgula")
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

    public List<Recipient> getRecipientList() {
        return Arrays.stream(recipients.split(";", -1))
                .map(String::trim)
                .map(Recipient::new)
                .toList();
    }
}
