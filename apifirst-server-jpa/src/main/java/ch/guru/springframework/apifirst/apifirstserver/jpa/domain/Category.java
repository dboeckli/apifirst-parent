package ch.guru.springframework.apifirst.apifirstserver.jpa.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "char(36)", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @Size(min = 3,max = 25)
    private String category;
    @NotNull 
    @Size(min = 3,max = 255)
    private String description;
    @NotNull 
    @Size(min = 3,max = 25)
    private String categoryCode;

    @ManyToMany(mappedBy = "categories")
    @ToString.Exclude
    private List<Product> products;

    private OffsetDateTime dateCreated;
    private OffsetDateTime dateUpdated;
}
