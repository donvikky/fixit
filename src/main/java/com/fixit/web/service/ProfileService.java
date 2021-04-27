package com.fixit.web.service;

import com.fixit.web.entity.Craft;
import com.fixit.web.entity.Profile;
import com.fixit.web.entity.User;
import com.fixit.web.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

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

}
