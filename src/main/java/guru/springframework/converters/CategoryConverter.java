package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.domain.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter implements TwoWaysConverter<CategoryCommand, Category>{
    @Override
    public CategoryCommand convertToCommand(Category source) {
        if (source == null) {
            return null;
        }

        CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setId(source.getId());
        categoryCommand.setDescription(source.getDescription());
        return categoryCommand;
    }

    @Override
    public Category convertToDomain(CategoryCommand source) {
        if (source == null) {
            return null;
        }

        Category category = new Category();
        category.setId(source.getId());
        category.setDescription(source.getDescription());
        return category;
    }
}
