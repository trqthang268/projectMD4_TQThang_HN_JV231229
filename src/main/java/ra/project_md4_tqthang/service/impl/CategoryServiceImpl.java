package ra.project_md4_tqthang.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.project_md4_tqthang.exception.CustomException;
import ra.project_md4_tqthang.model.Category;
import ra.project_md4_tqthang.repository.ICategoryRepository;
import ra.project_md4_tqthang.repository.IProductRepository;
import ra.project_md4_tqthang.service.ICategoryService;

import java.util.List;
import java.util.NoSuchElementException;

@Service

public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private IProductRepository productRepository;

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findCateById(Long id) throws CustomException {
        return categoryRepository.findCategoryByCategoryId(id);
    }

    @Override
    public void deleteCategory(Long categoryId) throws CustomException {
        Category category = findCateById(categoryId);
        boolean productExists = categoryRepository.existsProductsInCategory(categoryId);
        if (productExists){
            throw new CustomException("Không thể xóa danh mục vì có tồn tại sản phẩm", HttpStatus.BAD_REQUEST);
        }else {
            categoryRepository.delete(category);
        }
    }

    @Override
    public Category updateCategory(Category category) throws CustomException {
        categoryRepository.findById(category.getCategoryId()).orElseThrow(()->new NoSuchElementException("Khong ton tai danh muc nay"));
        return categoryRepository.save(category);
    }

    @Override
    public Category addCategory(Category category) throws CustomException {
        return categoryRepository.save(category);
    }

    @Override
    public Page<Category> getCategoryByPage(Integer page, Integer pageItem, String orderBy, String orderDirection) {
        Pageable pageable = null;
        if (orderBy != null && !orderBy.isEmpty()){
            Sort sort = switch (orderDirection){
                case "asc" -> Sort.by(orderBy).ascending();
                case "desc" -> Sort.by(orderBy).descending();
                default -> null;
            };
            assert sort != null;
            pageable = PageRequest.of(page, pageItem, sort);
        }else {
            pageable = PageRequest.of(page, pageItem);
        }
        return categoryRepository.findAllCategory(pageable);
    }
}
