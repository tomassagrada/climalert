package ar.utn.ba.ddsi.mailing.models.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ubicacion {
  private String ciudad;
  private String region;
  private String pais;

  public Ubicacion(String ciudad, String region, String pais) {
    this.ciudad = ciudad;
    this.region = region;
    this.pais = pais;
  }
}
