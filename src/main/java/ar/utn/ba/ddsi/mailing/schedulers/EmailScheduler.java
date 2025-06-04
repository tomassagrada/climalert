package ar.utn.ba.ddsi.mailing.schedulers;

import ar.utn.ba.ddsi.mailing.config.SchedulingConfig;
import ar.utn.ba.ddsi.mailing.services.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailScheduler {
    private final IEmailService emailService;

    public EmailScheduler(IEmailService emailService) {
        this.emailService = emailService;
    }

    @Scheduled(cron = "${cron.expression}")
    public void procesarEmailsPendientes() {
        emailService.procesarPendientes();
    }

    @Scheduled(cron = "${cron.expression}")
    public void loguearEmailsPendientes() {
        emailService.loguearEmailsPendientes();
    }
} 