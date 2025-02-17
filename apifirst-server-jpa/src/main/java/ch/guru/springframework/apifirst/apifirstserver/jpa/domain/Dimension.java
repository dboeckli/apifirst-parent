package ch.guru.springframework.apifirst.apifirstserver.jpa.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Dimension {
    @NotNull @Min(1L) @Max(999L)
    private Integer length;
    @NotNull @Min(1L) @Max(999L)
    private Integer width;
    @NotNull @Min(1L) @Max(999L)
    private Integer height;
}
