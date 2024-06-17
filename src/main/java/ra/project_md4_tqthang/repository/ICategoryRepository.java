package ra.project_md4_tqthang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ra.project_md4_tqthang.model.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> , PagingAndSortingRepository<Category,Long> {
    @Query("select count(p)>0 from Products p where p.category.categoryId=:categoryId")
    boolean existsProductsInCategory(Long categoryId);
    Category findCategoryByCategoryId(Long categoryId);

    @Query("select c from Category c")
    Page<Category> findAllCategory(Pageable pageable);
}
