package ra.project_md4_tqthang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ra.project_md4_tqthang.model.Users;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findUsersByUserName(String username);
    List<Users> findUsersByFullName(String fullName);
    Users findUsersByUserId(Long userId);

    @Query("select u from Users u where u.fullName like concat('%',:fullName,'%') ")
    Page<Users> findUsersByFullNameAndSorting(String fullName, Pageable pageable);

    boolean existsByEmail(String s);
}
