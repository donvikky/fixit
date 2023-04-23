package com.fixit.web.service;

import com.fixit.web.entity.Craft;
import com.fixit.web.entity.Profile;
import com.fixit.web.entity.State;
import com.fixit.web.entity.User;
import com.fixit.web.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private ProfileRepository profileRepository;

    @Value("${spring.data.web.pageable.default-page-size}")
    private int pageSize;


    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public List<Profile> listAll() {
        return profileRepository.findAll();
    }

    public Page<Profile> listAll(final int pageNumber) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return profileRepository.findAll(pageable);
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
    public Page searchByStateAndService(Craft craft, State state, final int pageNumber){
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return profileRepository.findByCraftsAndState(craft, state, pageable);
    }

    public Page searchByService(Craft craft, final int pageNumber){
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return profileRepository.findByCrafts(craft, pageable);
    }

    public int updateTelegramId(int id, String telegramId){
        return profileRepository.updateTelegramId(id, telegramId);
    }

    public Optional<Profile> findByTelegramId(String telegramId){
        return profileRepository.findByTelegramId(telegramId);
    }

    public Optional<Profile> findByMobileNumber(String mobileNumber){
        return profileRepository.findByMobileNumber(mobileNumber);
    };

    public List<Profile> getProfilesForNotificationByStateAndCraft(int craftId, State state, int profileId){
        return profileRepository.findProfilesByCraftsAndState(craftId, state, profileId);
    }

    public void incrementProfileViews(int profileId){
        profileRepository.incrementProfileViews(profileId);
    }
}
