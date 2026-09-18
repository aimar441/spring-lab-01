package kz.iitu.springlab.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class HelloController {

    @Value("${app.owner:unknown}")
    private String owner;

    @GetMapping("/hello")
    public Greeting hello(@RequestParam(defaultValue = "world") String name) {
        return new Greeting("Hello, " + name + "!", owner, LocalDateTime.now());
    }

    @GetMapping("/info")
    public Info info() {
        return new Info(owner,
                System.getProperty("java.version"),
                Runtime.getRuntime().availableProcessors());
    }

    public record Greeting(String message, String owner, LocalDateTime timestamp) { }
    @GetMapping("/stats")
    public Stats stats(@RequestParam String numbers) {
        String[] values = numbers.split(",");

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int sum = 0;

        for (String value : values) {
            int number = Integer.parseInt(value.trim());
            min = Math.min(min, number);
            max = Math.max(max, number);
            sum += number;
        }

        double average = (double) sum / values.length;

        return new Stats(min, max, average);
    }

    public record Stats(int min, int max, double average) { }
    public record Info(String owner, String javaVersion, int cpuCores) { }
}