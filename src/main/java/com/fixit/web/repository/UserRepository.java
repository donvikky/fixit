package com.fixit.web.repository;

import com.fixit.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(propagation = Propagation.REQUIRES_NEW) //To avoid TransactionalException / RollBackException
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
    Optional<User> findByProviderId(String providerId);
}
