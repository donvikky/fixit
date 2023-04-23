package com.fixit.web.service;

import com.fixit.web.entity.Role;
import com.fixit.web.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> listAll() {
        return roleRepository.findAll();
    }

    public void save(Role role) {
        roleRepository.save(role);
    }

    public Role get(int id) {
        return (Role) roleRepository.findById(id).get();
    }

    public void delete(int id) {
        roleRepository.deleteById(id);
    }

    public Optional<Role> findByName(String name){
        return roleRepository.findByName(name);
    }
}
