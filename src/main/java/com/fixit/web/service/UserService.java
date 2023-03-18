package com.fixit.web.service;

import com.fixit.web.entity.User;
import com.fixit.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> listAll() {
        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User get(int id) {
        return (User) userRepository.findById(id).get();
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByProviderId(String id){
        return userRepository.findByProviderId(id);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByVerificationToken(String token){
        return userRepository.findByVerificationToken(token);
    }

    public int updateUserByVerificationToken(String token){
        return userRepository.updateUserByVerificationToken(token);
    }
}
