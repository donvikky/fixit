package com.fixit.web.service;


import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService{

    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            if(!Files.exists(root) || !Files.isDirectory(root)){
                Files.createDirectory(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public String save(MultipartFile file) {
        String fileName = null;
        try {
            fileName = this.generateFileName(file);
            Files.copy(file.getInputStream(), this.root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        return fileName;
    }

    @Override
    public List<String> saveAll(MultipartFile[] files) {
        List<String> fileNames = new ArrayList<>();
        if(files.length < 1){
            return fileNames;
        }

        for(MultipartFile file: files){
            String filename = this.save(file);
            fileNames.add(filename);
        }

        return fileNames;
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void delete(String filename) {
        if(filename == null){
            return;
        }
        try {
            Path file = root.resolve(filename);
            if(Files.exists(file)){
                Files.delete(file);
            }
        }catch (IOException ioException){
            throw new RuntimeException(ioException.getMessage());
        }
    }

    @Override
    public void deleteAll(List<String> filenames) {
        if(filenames.isEmpty()){
            return;
        }
        /*
        for(String file: filenames){
            this.delete(file);
        }
        */
        filenames.stream().forEach(file -> this.delete(file));
    }

    private String generateFileName(MultipartFile file){
        final String PREFIX = "photo_";
        final String SEPARATOR = ".";
        final int STR_LENGTH = 12;
        String contentType = file.getContentType().split("/")[1];

        return String.format("%s%s%s%s",PREFIX, RandomStringUtils.randomAlphanumeric(STR_LENGTH), SEPARATOR, contentType);
    }
}
