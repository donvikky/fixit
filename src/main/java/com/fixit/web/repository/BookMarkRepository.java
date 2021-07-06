package com.fixit.web.repository;

import com.fixit.web.entity.BookMark;
import com.fixit.web.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<BookMark, Integer> {
    Page<BookMark> findByBookmarker(Profile profile, Pageable pageable);
    Optional<BookMark> findByBookmarkerAndArtisan(Profile bookmarker, Profile artisan);
}
