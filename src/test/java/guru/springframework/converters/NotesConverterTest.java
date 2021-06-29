package guru.springframework.converters;

import guru.springframework.commands.NotesCommand;
import guru.springframework.domain.Notes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NotesConverterTest {
    private static final Long ID_VALUE = 1L;
    private static final String RECIPE_NOTES = "Notes";

    @InjectMocks
    private NotesConverter notesConverter;

    @Test
    void convertToCommand_withNull() {
        // Act && Assert
        assertNull(notesConverter.convertToCommand(null));
    }

    @Test
    void convertToDomain_withNull() {
        // Act && Assert
        assertNull(notesConverter.convertToDomain(null));
    }

    @Test
    void convertToCommand_withEmpty() {
        // Act && Assert
        assertNotNull(notesConverter.convertToCommand(new Notes()));
    }

    @Test
    void convertToDomain_withEmpty() {
        // Act && Assert
        assertNotNull(notesConverter.convertToDomain(new NotesCommand()));
    }

    @Test
    void convertToCommand() {
        // Arrange
        Notes notes = new Notes();
        notes.setId(ID_VALUE);
        notes.setRecipeNotes(RECIPE_NOTES);

        // Act
        NotesCommand command = notesConverter.convertToCommand(notes);

        // Assert
        assertThat(command.getId()).isEqualTo(notes.getId());
        assertThat(command.getRecipeNotes()).isEqualTo(notes.getRecipeNotes());
    }

    @Test
    void convertToDomain() {
        // Arrange
        NotesCommand notesCommand = new NotesCommand();
        notesCommand.setId(ID_VALUE);
        notesCommand.setRecipeNotes(RECIPE_NOTES);

        // Act
        Notes notes = notesConverter.convertToDomain(notesCommand);

        // Assert
        assertThat(notes.getId()).isEqualTo(notesCommand.getId());
        assertThat(notes.getRecipeNotes()).isEqualTo(notesCommand.getRecipeNotes());
    }
}