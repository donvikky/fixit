package com.fixit.web.repository;

import com.fixit.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(propagation = Propagation.REQUIRES_NEW) //To avoid TransactionalException / RollBackException
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.username = ?1 AND u.enabled = true")
    User findByUsername(String username);
    Optional<User> findByProviderId(String providerId);
    Optional<User> findByVerificationToken(String token);

    @Modifying
    @Query("update User u set u.enabled = true where u.verificationToken = ?1")
    int updateUserByVerificationToken(String token);
}
