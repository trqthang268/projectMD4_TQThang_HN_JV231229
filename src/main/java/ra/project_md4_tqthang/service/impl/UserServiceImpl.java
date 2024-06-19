package ra.project_md4_tqthang.service.impl;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.project_md4_tqthang.dto.request.ChangePasswordRequest;
import ra.project_md4_tqthang.dto.request.UpdateUserRequest;
import ra.project_md4_tqthang.exception.CustomException;
import ra.project_md4_tqthang.model.Users;
import ra.project_md4_tqthang.repository.IUserRepository;
import ra.project_md4_tqthang.service.IUserService;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Users> searchUserByName(String fullName) {
        return userRepository.findUsersByFullName(fullName);
    }

    @Override
    public Users searchUserById(Long id) {
        return userRepository.findUsersByUserId(id);
    }

    @Override
    public Page<Users> getUsersByPage(String searchName, Integer page, Integer pageItem, String orderBy, String orderDirection) {
        Pageable pageable = null;
        if (orderBy != null && !orderBy.isEmpty()) {
            //co sap xep
            Sort sort = null;
            switch (orderDirection) {
                case "asc":
                    sort = Sort.by(orderBy).ascending();
                    break;
                case "desc":
                    sort = Sort.by(orderBy).descending();
                    break;
            }
            pageable = PageRequest.of(page, pageItem, sort);
        }else {
            // khong sap xep
            pageable = PageRequest.of(page, pageItem);
        }
        if (searchName != null && !searchName.isEmpty()) {
            //co tim kiem
            return userRepository.findUsersByFullNameAndSorting(searchName,pageable);
        }else {
            // khong tim kiem
            return userRepository.findAll(pageable);
        }
    }

    @Override
    public Users updateUser(Long userId, UpdateUserRequest updateUserRequest) {
        Users user = userRepository.findById(userId)
                .orElseThrow(()->new NoSuchElementException("user not found"));
        user.setUserName(updateUserRequest.getUserName());
        user.setEmail(updateUserRequest.getEmail());
        user.setFullName(updateUserRequest.getFullName());
        user.setPhoneNumber(updateUserRequest.getPhoneNumber());
        user.setAddress(updateUserRequest.getAddress());
        user.setUpdateAt(new Date());

        return userRepository.save(user);

    }

    @Override
    public Users changePassword(Long userId, ChangePasswordRequest changePasswordRequest) throws CustomException {
        Users users = userRepository.findById(userId)
                .orElseThrow(()->new NoSuchElementException("user not found"));
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), users.getPassword())) {
            throw new CustomException("Old password does not match", HttpStatus.BAD_REQUEST);
        }
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new CustomException("New passwords do not match", HttpStatus.BAD_REQUEST);
        }
        users.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        return userRepository.save(users);
    }
}
