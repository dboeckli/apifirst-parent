package ch.guru.springframework.apifirst.apifirstserver.domain;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Name {
    private String prefix;
    private String firstName;
    private String lastName;
    private String suffix;
}
