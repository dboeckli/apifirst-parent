package ch.guru.springframework.apifirst.apifirstserver.jpa.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Name {
    @Size(min = 0,max = 25)
    private String prefix;

    @NotNull
    @Size(min = 1,max = 100)
    private String firstName;

    @NotNull @Size(min = 1,max = 100)
    private String lastName;

    @Size(min = 0,max = 25)
    private String suffix;
}
