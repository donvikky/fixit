package com.fixit.web.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {

    public void init();
    public String save(MultipartFile file);
    public List<String> saveAll(MultipartFile[] files);
    public Resource load(String fileName);
    public void delete(String filename);
    public void deleteAll(List<String> filenames);

}
