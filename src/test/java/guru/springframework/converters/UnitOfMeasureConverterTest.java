package guru.springframework.converters;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UnitOfMeasureConverterTest {
    private static final String DESCRIPTION = "description";
    private static final Long LONG_VALUE = 1L;

    @InjectMocks
    private UnitOfMeasureConverter converter;

    @Test
    void convertToCommand_withNull() {
        // Act && Assert
        assertNull(converter.convertToCommand(null));
    }

    @Test
    void convertToDomain_withNull() {
        // Act && Assert
        assertNull(converter.convertToDomain(null));
    }

    @Test
    void convertToCommand_withEmpty() {
        // Act && Assert
        assertNotNull(converter.convertToCommand(new UnitOfMeasure()));
    }

    @Test
    void convertToDomain_withEmpty() {
        // Act && Assert
        assertNotNull(converter.convertToDomain(new UnitOfMeasureCommand()));
    }

    @Test
    void convertToCommand() {
        // Arrange
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(LONG_VALUE);
        unitOfMeasure.setDescription(DESCRIPTION);

        // Act
        UnitOfMeasureCommand unitOfMeasureCommand = converter.convertToCommand(unitOfMeasure);

        // Assert
        assertThat(unitOfMeasureCommand.getId()).isEqualTo(unitOfMeasure.getId());
        assertThat(unitOfMeasureCommand.getDescription()).isEqualTo(unitOfMeasure.getDescription());
    }

    @Test
    void convertToDomain() {
        // Arrange
        UnitOfMeasureCommand command = new UnitOfMeasureCommand();
        command.setId(LONG_VALUE);
        command.setDescription(DESCRIPTION);

        // Act
        UnitOfMeasure uom = converter.convertToDomain(command);

        // Assert
        assertThat(uom.getId()).isEqualTo(command.getId());
        assertThat(uom.getDescription()).isEqualTo(command.getDescription());
    }
}