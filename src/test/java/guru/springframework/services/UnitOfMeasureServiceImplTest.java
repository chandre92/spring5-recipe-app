package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureConverter;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitOfMeasureServiceImplTest {
    @Mock
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Spy
    private UnitOfMeasureConverter unitOfMeasureConverter;

    @InjectMocks
    private UnitOfMeasureServiceImpl unitOfMeasureService;

    @Test
    void listAll() {
        // Arrange
        UnitOfMeasure firstUnitOfMeasure = new UnitOfMeasure();
        firstUnitOfMeasure.setId(1L);
        UnitOfMeasure secondUnitOfMeasure = new UnitOfMeasure();
        secondUnitOfMeasure.setId(2L);
        when(unitOfMeasureRepository.findAll()).thenReturn(Set.of(firstUnitOfMeasure, secondUnitOfMeasure));

        // Act
        Set<UnitOfMeasureCommand> unitOfMeasureCommands = unitOfMeasureService.listAll();

        // Assert
        assertThat(unitOfMeasureCommands).hasSize(2);
        verify(unitOfMeasureRepository).findAll();
        verify(unitOfMeasureConverter, times(2)).convertToCommand(any());
        verifyNoMoreInteractions(unitOfMeasureRepository, unitOfMeasureConverter);
    }
}