package ra.project_md4_tqthang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ProjectMd4TqthangApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectMd4TqthangApplication.class, args);
    }

}
