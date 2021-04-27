package com.fixit.web.repository;

import com.fixit.web.entity.Craft;
import com.fixit.web.entity.Profile;
import com.fixit.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    List<Profile> findAllByCrafts(Craft craft);
    Profile findByUser(User user);
}
