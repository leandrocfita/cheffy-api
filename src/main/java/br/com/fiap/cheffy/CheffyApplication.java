package br.com.fiap.cheffy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CheffyApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CheffyApplication.class, args);
    }

}
