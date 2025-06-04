package ar.utn.ba.ddsi.mailing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class SchedulingConfig implements SchedulingConfigurer {
    @Value("${scheduling.hilos}")
    int cantidadHilos;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    private Executor taskExecutor() {
        return Executors.newScheduledThreadPool(cantidadHilos); // Pool de 3 hilos para nuestras tareas
    }
} 