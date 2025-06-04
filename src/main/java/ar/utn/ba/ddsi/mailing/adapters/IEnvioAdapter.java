package ar.utn.ba.ddsi.mailing.adapters;

public interface IEnvioAdapter {
  boolean enviarEmail(String destinatario, String remitente, String asunto, String contenido);
}
