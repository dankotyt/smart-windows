package ru.pin36bik;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.pin36bik.entity.Preset;
import ru.pin36bik.repository.PresetRepository;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        // Загружаем переменные из .env файла
        Dotenv dotenv = Dotenv.configure().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        // Запуск Spring-приложения
        ApplicationContext context = SpringApplication.run(Main.class, args);

        // Получаем репозиторий из контекста
        PresetRepository presetRepository = context.getBean(PresetRepository.class);

        // Создаем и сохраняем пресет
        Preset preset = new Preset();
        preset.setPresetName("Home Mode");
        preset.setVentilationFlag(true);
        preset.setVentilationTimer(30);
        preset.setDarknessValue(75);
        preset.setFrameColor(16753920);
        presetRepository.save(preset);

        // Чтение пресетов из базы данных
        Iterable<Preset> presets = presetRepository.findAll();
        for (Preset p : presets) {
            System.out.println("Preset ID: " + p.getId());
            System.out.println("Preset Name: " + p.getPresetName());
            System.out.println("Ventilation Flag: " + p.getVentilationFlag());
            System.out.println("Ventilation Timer: " + p.getVentilationTimer());
            System.out.println("Darkness Value: " + p.getDarknessValue());
            System.out.println("Frame Color: " + p.getFrameColor());
            System.out.println("-----------------------------");
        }
    }
}