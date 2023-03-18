package com.fixit.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class FilestackStorageServiceImpl implements FileStorageService{

    private static final Logger logger = LoggerFactory.getLogger(FilestackStorageServiceImpl.class);

    @Value("${filestack.base.url}")
    private String baseUrl;
    @Value("${filestack.api.key}")
    private String apiKey;

    ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void init() {

    }

    @Override
    public String save(MultipartFile file) {
        String endpoint  = String.format("%s/store/S3?key=%s", baseUrl, apiKey);
        String result = "";
        HttpResponse<JsonNode> response = null;

        try {
            response = Unirest.post(endpoint)
                    .body(file.getInputStream())
                    .asJson();
            if (response.getStatus() == 200) {
                JsonNode jsonResponse = response.getBody();
                result = jsonResponse.getObject().getString("url");
            } else {
                logger.info("Upload failed with status code: " + response.getStatus());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
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
    public Resource load(String fileName) {
        return null;
    }

    @Override
    public void delete(String filename) {

    }

    @Override
    public void deleteAll(List<String> filenames) {

    }


}
