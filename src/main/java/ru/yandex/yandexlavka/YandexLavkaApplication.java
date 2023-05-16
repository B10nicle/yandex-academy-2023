package ru.yandex.yandexlavka;

import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.SpringApplication;

/**
 * @author Oleg Khilko
 */

@SpringBootApplication
@ComponentScan(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
public class YandexLavkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(YandexLavkaApplication.class, args);
    }
}
