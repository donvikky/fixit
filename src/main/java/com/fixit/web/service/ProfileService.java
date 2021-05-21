package com.fixit.web.service;

import com.fixit.web.entity.Craft;
import com.fixit.web.entity.Profile;
import com.fixit.web.entity.User;
import com.fixit.web.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public List<Profile> listAll() {
        return profileRepository.findAll();
    }

    public void save(Profile profile) {
        profileRepository.save(profile);
    }

    public Profile get(int id) {
        return (Profile) profileRepository.findById(id).get();
    }

    public void delete(int id) {
        profileRepository.deleteById(id);
    }

    public List<Profile> findByCraft(Craft craft){
        return profileRepository.findAllByCrafts(craft);
    }

    public Profile findByUser(User user){
        return profileRepository.findByUser(user);
    }

    public Optional<Profile> findByEmail(String email){
        return profileRepository.findByEmail(email);
    }

}
