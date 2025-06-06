package com.tinomaster.virtualdream.virtualdream.services;

import com.tinomaster.virtualdream.virtualdream.utils.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SchedulerService implements CommandLineRunner {

    private final CleanupService cleanupService;

    /**
     * Programa la tarea para ejecutar todos los días a medianoche.
     */
    @Scheduled(cron = "0 0 0 * * ?") // A la medianoche
    public void programarEliminacionRegistros() {
        int registrosEliminados = cleanupService.deleteOldServiceSales();
        Log.info("Registros eliminados programados: " + registrosEliminados);
    }

    /**
     * Ejecuta la limpieza al iniciar la aplicación.
     */
    @Override
    public void run(String... args) {
        int registrosEliminados = cleanupService.deleteOldServiceSales();
        Log.info("Registros eliminados al iniciar: " + registrosEliminados);
    }
}
