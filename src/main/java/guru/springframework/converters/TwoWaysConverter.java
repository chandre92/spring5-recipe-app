package guru.springframework.converters;

import guru.springframework.commands.AbstractCommand;
import guru.springframework.domain.BaseEntity;

/**
 * Base interface for converters to convert domain object into a command and
 * vice versa
 * @param <COMMAND_TYPE> command type
 * @param <DOMAIN_TYPE> domain object type
 */
public interface TwoWaysConverter<COMMAND_TYPE extends AbstractCommand, DOMAIN_TYPE extends BaseEntity> {
    COMMAND_TYPE convertToCommand(DOMAIN_TYPE source);
    DOMAIN_TYPE convertToDomain(COMMAND_TYPE source);
}
