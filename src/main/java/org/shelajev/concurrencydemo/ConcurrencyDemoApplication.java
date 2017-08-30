package org.shelajev.concurrencydemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.zeroturnaround.exec.ProcessExecutor;

@SpringBootApplication
public class ConcurrencyDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcurrencyDemoApplication.class, args);
	}

  @Bean
  public CommandLineRunner openWebApp() {
    return (args) -> {
      new ProcessExecutor().command("open", "http://localhost:8080").execute();
    };
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.build();
  }

}
