package ch.guru.springframework.apifirst.apifirstserver.jpa.domain;

import jakarta.persistence.*;
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
    
    private String category;
    private String description;
    private String categoryCode;

    @ManyToMany(mappedBy = "categories")
    @ToString.Exclude
    private List<Product> products;

    private OffsetDateTime dateCreated;
    private OffsetDateTime dateUpdated;
}
