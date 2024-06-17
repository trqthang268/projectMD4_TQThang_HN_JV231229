package ra.project_md4_tqthang.service;

import org.springframework.data.domain.Page;
import ra.project_md4_tqthang.exception.CustomException;
import ra.project_md4_tqthang.model.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getCategories();
    Category findCateById(Long id) throws CustomException;
    void deleteCategory(Long categoryId) throws CustomException;
    Category updateCategory(Category category) throws CustomException;
    Category addCategory(Category category) throws CustomException;
    Page<Category> getCategoryByPage(Integer page, Integer pageItem, String orderBy, String orderDirection);
}