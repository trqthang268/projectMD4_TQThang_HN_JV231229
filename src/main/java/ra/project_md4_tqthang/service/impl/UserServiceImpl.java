package ra.project_md4_tqthang.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ra.project_md4_tqthang.model.Users;
import ra.project_md4_tqthang.repository.IUserRepository;
import ra.project_md4_tqthang.service.IUserService;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserRepository userRepository;

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
}
