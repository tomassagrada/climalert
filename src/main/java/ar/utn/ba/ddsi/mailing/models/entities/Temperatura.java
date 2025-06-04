package ar.utn.ba.ddsi.mailing.models.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Temperatura {
  private Double temperaturaCelsius;

  public Temperatura(Double temperaturaCelsius) {
    this.temperaturaCelsius = temperaturaCelsius;
  }

  public Double temperaturaFahrenheit(){
    return (temperaturaCelsius * 1.8) + 32;
  }
}
