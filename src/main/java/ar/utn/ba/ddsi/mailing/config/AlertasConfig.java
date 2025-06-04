package ar.utn.ba.ddsi.mailing.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class AlertasConfig {
  private double temperaturaAlerta = 35.0;
  private int humedadAlerta = 60;
}
