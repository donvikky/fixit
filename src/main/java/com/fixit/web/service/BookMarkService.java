package com.fixit.web.service;

import com.fixit.web.entity.BookMark;
import com.fixit.web.entity.Profile;
import com.fixit.web.repository.BookMarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookMarkService {

    private BookMarkRepository bookMarkRepository;

    @Value("${spring.data.web.pageable.default-page-size}")
    private int pageSize;

    @Autowired
    public BookMarkService(BookMarkRepository bookMarkRepository) {
        this.bookMarkRepository = bookMarkRepository;
    }

    public List<BookMark> listAll() {
        return bookMarkRepository.findAll();
    }

    public BookMark save(BookMark bookMark) {
        return bookMarkRepository.save(bookMark);
    }

    public BookMark get(int id) {
        return (BookMark) bookMarkRepository.findById(id).get();
    }

    public void delete(int id) {
        bookMarkRepository.deleteById(id);
    }

    public Page<BookMark> findByBookmarker(Profile bookmarker, final int pageNumber){
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return bookMarkRepository.findByBookmarker(bookmarker, pageable);
    }

    public Optional<BookMark> findByBookmarkerAndArtisan(Profile bookmarker, Profile artisan){
        return bookMarkRepository.findByBookmarkerAndArtisan(bookmarker, artisan);
    }
}
