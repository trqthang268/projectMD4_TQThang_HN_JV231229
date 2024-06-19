package ra.project_md4_tqthang.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.project_md4_tqthang.model.Users;
import ra.project_md4_tqthang.repository.IUserRepository;

@Component
public class HandleUserExist implements ConstraintValidator<UserExist, String> {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return userRepository.findUsersByUserName(s).isEmpty();
    }
}
