package ra.project_md4_tqthang.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ra.project_md4_tqthang.repository.IUserRepository;

@Component
@RequiredArgsConstructor
public class HandleEmailExist implements ConstraintValidator<EmailExist,String> {
	private final IUserRepository userRepository;
	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		return !userRepository.existsByEmail(s);
	}
}
