package ra.project_md4_tqthang.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.project_md4_tqthang.repository.IProductRepository;

@Component
public class HandleProductNameExist implements ConstraintValidator<ProductNameExist,String> {
    @Autowired
    private IProductRepository productRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return productRepository.findProductsByProductName(s).isEmpty();
    }
}
