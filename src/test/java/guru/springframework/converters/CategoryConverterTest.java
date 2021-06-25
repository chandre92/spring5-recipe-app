package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.domain.Category;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class CategoryConverterTest {
    private static final Long ID_VALUE = 1L;
    private static final String DESCRIPTION = "description";

    private static Category expectedCategory;
    private static CategoryCommand expectedCategoryCommand;

    @InjectMocks
    private CategoryConverter converter;

    @BeforeAll
    static void beforeAll() {
        expectedCategory = new Category();
        expectedCategory.setId(ID_VALUE);
        expectedCategory.setDescription(DESCRIPTION);

        expectedCategoryCommand = new CategoryCommand();
        expectedCategoryCommand.setDescription(DESCRIPTION);
        expectedCategoryCommand.setId(ID_VALUE);
    }

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
        assertNotNull(converter.convertToCommand(new Category()));
    }

    @Test
    void convertToDomain_withEmpty() {
        // Act && Assert
        assertNotNull(converter.convertToDomain(new CategoryCommand()));
    }

    @Test
    void convertToCommand() {
        // Act
        CategoryCommand categoryCommand = converter.convertToCommand(expectedCategory);

        // Assert
        assertThat(categoryCommand.getId()).isEqualTo(expectedCategory.getId());
        assertThat(categoryCommand.getDescription()).isEqualTo(expectedCategory.getDescription());
    }

    @Test
    void convertToDomain() {
        // Act
        Category category = converter.convertToDomain(expectedCategoryCommand);

        // Assert
        assertThat(category.getId()).isEqualTo(expectedCategoryCommand.getId());
        assertThat(category.getDescription()).isEqualTo(expectedCategoryCommand.getDescription());
    }
}