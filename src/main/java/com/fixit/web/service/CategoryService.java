package com.fixit.web.service;

import com.fixit.web.entity.Category;
import com.fixit.web.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listAll() {
        return categoryRepository.findAll();
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
