package com.fixit.web.repository;

import com.fixit.web.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class UserRepositoryTest {

    private User user1;
    private User user2;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setUsername("test@test.com");
        user1.setPassword("1234");
        user1.setEnabled(true);

        user2 = new User();
        user2.setUsername("test2@test2.com");
        user2.setPassword("1234");
        user2.setEnabled(false);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void itShouldFetchEnabledUserByUsername() {
        userRepository.save(user1);
        User savedUser = userRepository.findByUsername("test@test.com");
        assertEquals(savedUser.getUsername(), user1.getUsername());
    }
    @Test
    void itShouldNotFetchDisabledUserByUsername(){
        userRepository.save(user2);
        User savedUser = userRepository.findByUsername("test2@test2.com");
        assertNull(savedUser);
    }

    @Test
    void itShouldUpdateUserByVerificationToken(){
        user2.setVerificationToken("1000");
        userRepository.save(user2);
        userRepository.updateUserByVerificationToken("1000");

        User savedUser = userRepository.findByUsername("test2@test2.com");
        assertNotNull(savedUser);
        assertTrue(savedUser.getEnabled());
    }
}