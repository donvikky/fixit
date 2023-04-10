package com.fixit.web.repository;

import com.fixit.web.entity.Craft;
import com.fixit.web.entity.Profile;
import com.fixit.web.entity.State;
import com.fixit.web.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Page findAll(Pageable pageable);
    List<Profile> findAllByCrafts(Craft craft);
    Profile findByUser(User user);
    Optional<Profile> findByEmail(String email);
    Page findByCraftsAndState(Craft craft, State state, Pageable pageable);

    @Query("SELECT p FROM Profile p " +
            "JOIN p.crafts c WHERE c.id = :craftId " +
            "AND p.state = :state " +
            "AND p.receiveJobNotification = true " +
            "AND p.id != :profileId")
    List<Profile> findProfilesByCraftsAndState(@Param("craftId") int craftId, State state, @Param("profileId") int profileId);
    Page findByCrafts(Craft craft, Pageable pageable);
    @Modifying
    @Query("update Profile p set p.telegramId = :telegramId where p.id = :id")
    int updateTelegramId(@Param("id") int id, @Param("telegramId") String telegramId);
    Optional<Profile> findByTelegramId(String telegramId);

    Optional<Profile> findByMobileNumber(String mobileNumber);

}
