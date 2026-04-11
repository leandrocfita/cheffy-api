package br.com.fiap.cheffy;

import org.junit.jupiter.api.Test;

class CheffyApplicationTest {

    @Test
    void mainMethodExists() {
        // Apenas verifica se a classe principal existe e tem o método main
        try {
            CheffyApplication.class.getDeclaredMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            throw new AssertionError("Main method not found");
        }
    }
}
