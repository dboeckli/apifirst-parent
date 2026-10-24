package ch.guru.springframework.apifirst.apifirstserver.jpa.service.error;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotFoundExceptionTest {

    @Test
    void createsExceptionWithMessageAndCause() {
        RuntimeException cause = new RuntimeException("cause");
        NotFoundException exception = new NotFoundException("message", cause);

        assertThat(exception).hasMessage("message").hasCause(cause);
        assertThat(new NotFoundException("message")).hasMessage("message");
        assertThat(new NotFoundException()).hasNoCause().hasMessage(null);
    }

}
