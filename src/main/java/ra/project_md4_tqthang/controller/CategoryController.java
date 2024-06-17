package ra.project_md4_tqthang.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.project_md4_tqthang.model.Category;
import ra.project_md4_tqthang.service.impl.CategoryServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/categories")
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;

    //PermitAll - GET - Danh sách danh mục được bán #4872
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categoryList = categoryService.getCategories();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);

    }


}
