package guru.springframework.repositories;

import guru.springframework.domain.UnitOfMeasure;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

// Needs to be called as *IT because with *IntegrationTest ending will
// interpreted by maven's unit test plugin as a unit test.
// see maven-failsafe-plugin config in the pom.xml
@DataJpaTest // Includes @ExtendWith({SpringExtension.class})
class UnitOfMeasureRepositoryIT {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Test
    @DirtiesContext
    void findByDescription() {
        // Act
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription("Teaspoon");

        // Assert
        assertTrue(unitOfMeasureOptional.isPresent());
    }

    @Test
    void findByDescriptionCup() {
        // Act
        Optional<UnitOfMeasure> cupOptional = unitOfMeasureRepository.findByDescription("Cup");

        // Assert
        assertTrue(cupOptional.isPresent());
    }
}