package com.blogapp.service.cloud;

import com.blogapp.service.post.PostServiceImpl;
import com.blogapp.web.dto.PostDto;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
            Map<?, ?> response = cloudStorageService.uploadImage(file, params);
            log.info("File Uploaded --> {}", response);
        }
        catch (IOException exception) {
            log.info("Error occurred --> {}", exception.getMessage());
        }
    }

    /*@Test
    void uploadMultipartImageFile()
    {
        File file = new File("/home/ayodele/Pictures/asa.jpeg");

        assertThat(file.exists()).isTrue();

        Map<Object, Object> params = new HashMap();
        params.put("public_id", "asa_profile");
        params.put("folder", "blogapp");
        params.put("overwrite", "true");

        try {
            Map<?, ?> response = cloudStorageService.uploadImage(file, params);
            log.info("File Uploaded --> {}", response);
        }
        catch (IOException exception) {
            log.info("Error occurred --> {}", exception.getMessage());
        }
    }*/

    @Test
    void uploadMultipartImageFile() throws IOException {
        PostDto postDto = new PostDto();
        postDto.setTitle("Test");
        postDto.setContent("Test");

        Path path = Paths.get("/home/ayodele/Pictures/asa.jpeg");
        assertThat(path.toFile().exists());
        MultipartFile multipartFile = new MockMultipartFile("images.jpeg",
                "images.jpeg", "image/jpeg", Files.readAllBytes(path));

        log.info("Multipart Object created --> {}", multipartFile);

        assertThat(multipartFile).isNotNull();
        assertThat(multipartFile.isEmpty()).isFalse();
        postDto.setImageFile(multipartFile);

        log.info("File name --> {}", postDto.getImageFile().getOriginalFilename());

        cloudStorageService.uploadImage(multipartFile, ObjectUtils.asMap(
                "public_id", "blogapp/" +
                        PostServiceImpl.extractFileName(Objects.requireNonNull(postDto.getImageFile().getOriginalFilename()))
        ));

        assertThat(postDto.getImageFile().getOriginalFilename()).isEqualTo("images.jpeg");
    }
}