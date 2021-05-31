package com.blogapp.web.controllers;

import com.blogapp.service.post.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping("/posts")
    public String getIndex() {
        return "index";
    }

    @GetMapping ("/post/create")
    public String getPostForm() {
        return "create";
    }

    @PostMapping("/post")
    public String savePost() {

        return null;
    }

}
