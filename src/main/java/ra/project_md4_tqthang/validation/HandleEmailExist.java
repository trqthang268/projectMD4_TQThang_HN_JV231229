package ra.project_md4_tqthang.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.project_md4_tqthang.repository.IUserRepository;

@Component
public class HandleEmailExist implements ConstraintValidator<EmailExist,String> {
	@Autowired
	private IUserRepository userRepository;
	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		return !userRepository.existsByEmail(s);
	}
}
