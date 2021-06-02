package com.blogapp.web.controllers;

import com.blogapp.service.post.PostService;
import com.blogapp.web.dto.PostDto;
import com.blogapp.web.exceptions.PostObjectIsNullException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/posts")
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping("")
    public String getIndex() {
        return "index";
    }

    @GetMapping ("/create")
    public String getPostForm(Model model)
    {
        model.addAttribute("postDto", new PostDto());
        return "create";
    }

    @PostMapping("/save")
    public String savePost(@ModelAttribute @Valid PostDto postDto)
    {
        log.info("Post dto received --> {}", postDto);

        try {
            postService.savePost(postDto);
        }
        catch (PostObjectIsNullException exception) {
            log.info("Exception occurred --> {}", exception.getMessage());
        }

        return "redirect:/posts";
    }

}
