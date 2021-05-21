package com.fixit.web.repository;

import com.fixit.web.entity.Craft;
import com.fixit.web.entity.Profile;
import com.fixit.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    List<Profile> findAllByCrafts(Craft craft);
    Profile findByUser(User user);
    Optional<Profile> findByEmail(String email);
}
