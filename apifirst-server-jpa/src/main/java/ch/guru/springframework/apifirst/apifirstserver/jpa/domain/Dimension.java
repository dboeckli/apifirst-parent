package ch.guru.springframework.apifirst.apifirstserver.jpa.domain;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Dimension {
    private Integer length;
    private Integer width;
    private Integer height;
}
