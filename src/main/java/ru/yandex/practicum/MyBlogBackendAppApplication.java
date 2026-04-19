package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.yandex.practicum")
public class MyBlogBackendAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBlogBackendAppApplication.class, args);
	}

}
