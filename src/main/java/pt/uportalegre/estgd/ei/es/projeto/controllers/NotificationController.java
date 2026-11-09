package pt.uportalegre.estgd.ei.es.projeto.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import pt.uportalegre.estgd.ei.es.projeto.domain.Notification;
import pt.uportalegre.estgd.ei.es.projeto.forms.CreateNotificationForm;
import pt.uportalegre.estgd.ei.es.projeto.services.NotificationService;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

    // Consulta as notificações e mostra em lista
    @GetMapping
    public String list(Model model) {
        model.addAttribute("notifications", service.findAll());
        return "notifications/list";
    }

    // Apresenta o formulário para criar uma notificação
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("form", new CreateNotificationForm());
        return "notifications/create";
    }

    // Recebe a submissão do formulário
    @PostMapping
    public String create(
            // Valida o formulário
            @Valid @ModelAttribute("form") CreateNotificationForm form,
            BindingResult result) {

        if (result.hasErrors()) {
            return "notifications/create";
        }

        // Cria e envia a notificação
        Notification notification
                = service.create(
                        form.getSubject(),
                        form.getContent(),
                        form.getRecipientList()
        );

        // Faz um pedido GET para o detalhe após a submissão
        return "redirect:/notifications/" + notification.getId();
    }

    // Apresenta uma notificação específica
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
        model.addAttribute("attempts", service.findAttempts(id));

        return "notifications/detail";
    }
}
