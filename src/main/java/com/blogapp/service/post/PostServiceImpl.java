package com.blogapp.service.post;

import com.blogapp.data.models.Comment;
import com.blogapp.data.models.Post;
import com.blogapp.data.repository.PostRepository;
import com.blogapp.service.cloud.CloudStorageService;
import com.blogapp.web.dto.PostDto;
import com.blogapp.web.exceptions.PostDoesNotExistException;
import com.blogapp.web.exceptions.PostObjectIsNullException;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CloudStorageService cloudStorageService;

    @Override
    public Post savePost(PostDto postDto) throws PostObjectIsNullException
    {
        if(postDto == null) {
            throw new PostObjectIsNullException("Post cannot be null");
        }

        log.info("Post DTO --> {}", postDto);

        Post post = new Post();

        if(postDto.getImageFile() != null && !postDto.getImageFile().isEmpty()) {
            try {
                Map<?, ?> uploadResponse = cloudStorageService.uploadImage(postDto.getImageFile(), ObjectUtils.asMap(
                        "public_id", "blogapp/" + extractFileName(postDto.getImageFile().getName())
                ));

                log.info("Upload Response --> {}", uploadResponse);

                post.setCoverImageUrl((String) uploadResponse.get("url"));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(postDto, post);

        log.info("Post object after mapping --> {}", post);

        return postRepository.save(post);
    }

    @Override
    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findPostsInDescOrder() {
        return postRepository.findByOrderByDateCreatedDesc();
    }

    @Override
    public Post updatePost(PostDto postDto) {
        return null;
    }

    @Override
    public Post findById(Integer id) throws PostDoesNotExistException
    {
        if(id == null) {
            throw new NullPointerException("Post Id cannot be null");
        }

        Optional<Post> foundPost = postRepository.findById(id);

        if(foundPost.isPresent()) {
            return foundPost.get();
        }
        else {
            throw new PostDoesNotExistException("Post with Id --> {}");
        }
    }

    @Override
    public void deletePostById(Integer id) {

    }

    @Override
    public Post addCommentToPost(Integer id, Comment comment) {
        return null;
    }

    public static String extractFileName(String fileName)
    {
        return fileName.split("\\.")[0];
    }
}
