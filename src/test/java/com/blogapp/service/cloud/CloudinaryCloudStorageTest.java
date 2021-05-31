package com.blogapp.service.cloud;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class CloudinaryCloudStorageTest {

    @Autowired
    CloudStorageService cloudStorageService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void uploadImage()
    {
        File file = new File("/home/ayodele/Pictures/asa.jpeg");

        assertThat(file.exists()).isTrue();

        Map<Object, Object> params = new HashMap();
        params.put("public_id", "asa_profile");
        params.put("folder", "blogapp");
        params.put("overwrite", "true");

        try {
            Map<Object, Object> response = cloudStorageService.uploadImage(file, params);
            log.info("File Uploaded --> {}", response);
        }
        catch (IOException exception) {
            log.info("Error occurred --> {}", exception.getMessage());
        }
    }
}