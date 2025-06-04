package ar.utn.ba.ddsi.mailing.models.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@Data
public class EmailInputDto {
  @NotNull
  private String destinatario;
  @NotNull
  private String remitente;
  private String asunto;
  private String contenido;

  public EmailInputDto(String destinatario, String remitente, String asunto, String mensaje) {
    setDestinatario(destinatario);
    setRemitente(remitente);
    setAsunto(asunto);
    setContenido(mensaje);
  }
}
