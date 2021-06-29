package guru.springframework.converters;

import guru.springframework.commands.NotesCommand;
import guru.springframework.domain.Notes;
import org.springframework.stereotype.Component;

@Component
public class NotesConverter implements TwoWaysConverter<NotesCommand, Notes> {
    @Override
    public NotesCommand convertToCommand(Notes source) {
        if (source == null) {
            return null;
        }

        NotesCommand notesCommand = new NotesCommand();
        notesCommand.setId(source.getId());
        notesCommand.setRecipeNotes(source.getRecipeNotes());

        return notesCommand;
    }

    @Override
    public Notes convertToDomain(NotesCommand source) {
        if (source == null) {
            return null;
        }

        Notes notes = new Notes();
        notes.setId(source.getId());
        notes.setRecipeNotes(source.getRecipeNotes());

        return notes;
    }
}
