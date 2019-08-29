package en.ladislav.finderapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.validation.constraints.NotNull;

@SpringBootApplication
public class FinderAPI {

    @NotNull
    protected SpringApplicationBuilder configure(@NotNull SpringApplicationBuilder application) {
        return application.sources(FinderAPI.class);
    }

    public static void main(String... args) {
        SpringApplication.run(FinderAPI.class);
    }
}
