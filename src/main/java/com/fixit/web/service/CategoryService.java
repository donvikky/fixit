package com.fixit.web.service;

import com.fixit.web.entity.Category;
import com.fixit.web.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    @Value("${spring.data.web.pageable.default-page-size}")
    private int pageSize;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> listAll() {
        return categoryRepository.findAll();
    }

    public Page<Category> listAll(final int pageNumber) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return categoryRepository.findAll(pageable);
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public Category get(int id) {
        return (Category) categoryRepository.findById(id).get();
    }

    public void delete(int id) {
        categoryRepository.deleteById(id);
    }
}
