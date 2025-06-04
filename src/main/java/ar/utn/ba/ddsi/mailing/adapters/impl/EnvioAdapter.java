package ar.utn.ba.ddsi.mailing.adapters.impl;

import ar.utn.ba.ddsi.mailing.adapters.IEnvioAdapter;
import org.springframework.stereotype.Component;

@Component
public class EnvioAdapter implements IEnvioAdapter {

  @Override
  public boolean enviarEmail(String destinatario, String remitente, String asunto, String contenido) {
    if (destinatario == null || remitente == null || asunto == null || contenido == null) {
      System.out.println("No se puedo enviar el email");
      return false;
    }
    System.out.println("Para: " + destinatario);
    System.out.println("De:  " + remitente);
    System.out.println("Asunto: " + asunto);
    System.out.println("Contenido: " + contenido);
    System.out.println("Email enviado exitosamente");
    return true;
  }
}
