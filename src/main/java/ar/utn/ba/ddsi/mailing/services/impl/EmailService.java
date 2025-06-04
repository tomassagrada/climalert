package ar.utn.ba.ddsi.mailing.services.impl;

import ar.utn.ba.ddsi.mailing.adapters.IEnvioAdapter;
import ar.utn.ba.ddsi.mailing.models.dtos.EmailInputDto;
import ar.utn.ba.ddsi.mailing.models.entities.Email;
import ar.utn.ba.ddsi.mailing.models.repositories.IEmailRepository;
import ar.utn.ba.ddsi.mailing.services.IEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmailService implements IEmailService {
    @Autowired
    private IEnvioAdapter envioAdapter;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final IEmailRepository emailRepository;

    public EmailService(IEmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public Email crearEmail(EmailInputDto emailInput) {
        Email email = new Email(emailInput.getDestinatario(), emailInput.getRemitente(), emailInput.getAsunto(), emailInput.getContenido());
        return emailRepository.save(email);
    }

    @Override
    public List<Email> obtenerEmails(Boolean pendiente) {
        if (pendiente != null) {
            return emailRepository.findByEnviado(!pendiente);
        }
        return emailRepository.findAll();
    }

    @Override
    public void procesarPendientes() {
        List<Email> pendientes = emailRepository.findByEnviado(false);
        for (Email email : pendientes) {
            email.setEnviado(this.enviar(email));
            if(!email.isEnviado()){
                emailRepository.delete(email);
            }else{emailRepository.save(email);}
        }
    }

    @Override
    public void loguearEmailsPendientes() {
        List<Email> pendientes = obtenerEmails(true);
        logger.info("Emails pendientes de envío: {}", pendientes.size());
        pendientes.forEach(email -> 
            logger.info("Email pendiente - ID: {}, Destinatario: {}, Asunto: {}", 
                email.getId(),
                email.getDestinatario(), 
                email.getAsunto())
        );
    }

    @Override
    public boolean enviar(Email email) {
        return this.envioAdapter.enviarEmail(email.getDestinatario(), email.getRemitente(), email.getAsunto(), email.getContenido());
    }
} 