package ra.project_md4_tqthang.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.project_md4_tqthang.repository.ICategoryRepository;

@Component
public class HandleCategoryNameExist implements ConstraintValidator<CategoryNameExist,String> {
    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return categoryRepository.existsByCategoryName(s);
    }
}
