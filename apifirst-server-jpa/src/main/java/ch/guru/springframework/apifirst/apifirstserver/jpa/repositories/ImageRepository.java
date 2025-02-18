package ch.guru.springframework.apifirst.apifirstserver.jpa.repositories;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
}
