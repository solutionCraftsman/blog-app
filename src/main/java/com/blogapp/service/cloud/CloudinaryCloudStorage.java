package com.blogapp.service.cloud;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryCloudStorage implements CloudStorageService {

    Cloudinary cloudinary;

    @Autowired
    public CloudinaryCloudStorage(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public Map<Object, Object> uploadImage(File file, Map<Object, Object> imageProperties) throws IOException
    {
        return cloudinary.uploader().upload(file,
                imageProperties);
    }

}
