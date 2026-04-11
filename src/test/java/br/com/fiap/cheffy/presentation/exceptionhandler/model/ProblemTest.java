package br.com.fiap.cheffy.presentation.exceptionhandler.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProblemTest {

    @Test
    void createProblem() {
        Problem.Field field = Problem.Field.builder()
                .name("field")
                .userMessage("message")
                .build();

        Problem problem = Problem.builder()
                .status(400)
                .title("Bad Request")
                .detail("Detail")
                .userMessage("User message")
                .timestamp(LocalDateTime.now())
                .fields(List.of(field))
                .build();

        assertThat(problem.getStatus()).isEqualTo(400);
        assertThat(problem.getTitle()).isEqualTo("Bad Request");
        assertThat(problem.getDetail()).isEqualTo("Detail");
        assertThat(problem.getUserMessage()).isEqualTo("User message");
        assertThat(problem.getTimestamp()).isNotNull();
        assertThat(problem.getFields()).hasSize(1);
        assertThat(problem.getFields().get(0).getName()).isEqualTo("field");
        assertThat(problem.getFields().get(0).getUserMessage()).isEqualTo("message");
    }

    @Test
    void createProblemWithNullFields() {
        Problem problem = Problem.builder()
                .status(500)
                .title("Error")
                .build();

        assertThat(problem.getStatus()).isEqualTo(500);
        assertThat(problem.getTitle()).isEqualTo("Error");
        assertThat(problem.getDetail()).isNull();
        assertThat(problem.getUserMessage()).isNull();
        assertThat(problem.getTimestamp()).isNull();
        assertThat(problem.getFields()).isNull();
    }

    @Test
    void createFieldWithAllProperties() {
        Problem.Field field = Problem.Field.builder()
                .name("email")
                .userMessage("Invalid email")
                .build();

        assertThat(field.getName()).isEqualTo("email");
        assertThat(field.getUserMessage()).isEqualTo("Invalid email");
    }

    @Test
    void problemBuilderToStringDoesNotThrow() {
        String s = Problem.builder().status(400).title("T").toString();
        assertThat(s).isNotNull();
    }

    @Test
    void fieldBuilderToStringDoesNotThrow() {
        String s = Problem.Field.builder().name("f").toString();
        assertThat(s).isNotNull();
    }
}
