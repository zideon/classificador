package br.uff.classificador;

import br.uff.classificador.service.LeitorNoticiaJson;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableCaching
public class ClassificadorApplication {

	public static void main(String[] args) {
            ConfigurableApplicationContext run = SpringApplication.run(ClassificadorApplication.class, args);

	}
}
