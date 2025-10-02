package org.example.tpoprogramacioniii;

import org.example.tpoprogramacioniii.View.ViewConsole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TpoProgramacionIiiApplication implements CommandLineRunner {

    private final ViewConsole viewConsole;

    public TpoProgramacionIiiApplication(ViewConsole viewConsole) {
        this.viewConsole = viewConsole;
    }

    public static void main(String[] args) {
        SpringApplication.run(TpoProgramacionIiiApplication.class, args);
        System.out.println("arrancandovich");
    }

    @Override
    public void run(String... args) {
        // delega toda la UI de consola a la clase ViewConsole
        viewConsole.run();
    }
}
