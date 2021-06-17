package guru.springframework.converters;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasureConverter implements TwoWaysConverter<UnitOfMeasureCommand, UnitOfMeasure> {
    @Override
    public UnitOfMeasureCommand convertToCommand(UnitOfMeasure source) {
        if (source == null) {
            return null;
        }

        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(source.getId());
        unitOfMeasureCommand.setDescription(source.getDescription());

        return unitOfMeasureCommand;
    }

    @Override
    public UnitOfMeasure convertToDomain(UnitOfMeasureCommand source) {
        if (source == null) {
            return null;
        }

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(source.getId());
        unitOfMeasure.setDescription(source.getDescription());

        return unitOfMeasure;
    }
}
