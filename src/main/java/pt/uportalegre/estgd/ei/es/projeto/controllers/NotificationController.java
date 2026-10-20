package pt.uportalegre.estgd.ei.es.projeto.controllers;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import pt.uportalegre.estgd.ei.es.projeto.domain.Notification;
import pt.uportalegre.estgd.ei.es.projeto.domain.Recipient;
import pt.uportalegre.estgd.ei.es.projeto.forms.CreateNotificationForm;
import pt.uportalegre.estgd.ei.es.projeto.services.NotificationService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("notifications", service.findAll());
        return "notifications/list";
    }

    @GetMapping("/new")
    private String create(
            @Valid @ModelAttribute("form") CreateNotificationForm form,
            BindingResult result) {

        if (result.hasErrors()) {
            return "notifications/create";
        }

        List<Recipient> recipients = new ArrayList<>();

        for (String value : form.getRecipients().split(";", -1)) {

            String address = value.trim();

            try {
                InternetAddress email = new InternetAddress(address, true);
                email.validate();

                if (address.isBlank() || !address.contains("@") || !address.equals(email.getAddress())) {
                    throw new AddressException("Invalid email address");
                }

                recipients.add(new Recipient(address));
            } catch (AddressException e) {
                result.rejectValue("recipients", "invalid",
                        "Indica emails válidos separados por ponto e vírgula");

                return "notifications/create";
            }
        }

        Notification notification;

        try {
            notification = service.createAndSend(
                    form.getSubject(), form.getContent(), recipients
            );
        } catch (MailException e) {
            result.reject(
                    "sendFailed",
                    "A notificação foi registada, mas o envio falhou. " +
                            "Consulta a listagem antes de submeter novamente.");

            return "notifications/create";
        }

        return "redirect:/notifications/" + notification.getId();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Notification notification;

        try {
            notification = service.findById(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Notification not found",
                    e
            );
        }

        model.addAttribute("notification", notification);
        return "notification/detail";
    }
}
