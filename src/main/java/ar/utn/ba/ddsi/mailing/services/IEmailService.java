package ar.utn.ba.ddsi.mailing.services;

import ar.utn.ba.ddsi.mailing.models.dtos.EmailInputDto;
import ar.utn.ba.ddsi.mailing.models.entities.Email;
import java.util.List;

public interface IEmailService {
    Email crearEmail(EmailInputDto email);
    List<Email> obtenerEmails(Boolean pendiente);
    void procesarPendientes();
    void loguearEmailsPendientes();
    boolean enviar(Email email);
} 