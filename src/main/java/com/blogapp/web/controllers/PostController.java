package com.blogapp.web.controllers;

import com.blogapp.data.models.Post;
import com.blogapp.service.post.PostService;
import com.blogapp.web.dto.PostDto;
import com.blogapp.web.exceptions.PostDoesNotExistException;
import com.blogapp.web.exceptions.PostObjectIsNullException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/posts")
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping("")
    public String getIndex(Model model)
    {
        List<Post> postList = postService.findPostsInDescOrder();
        model.addAttribute("postList", postList);

        return "index";
    }

    @GetMapping ("/create")
    public String getPostForm(Model model)
    {
        //model.addAttribute("postDto", new PostDto());
        model.addAttribute("error", false);
        return "create";
    }

    @PostMapping("/save")
    public String savePost(@ModelAttribute("postDto") @Valid PostDto postDto, BindingResult result, Model model)
    {
        log.info("Post dto received --> {}", postDto);

        if(result.hasErrors()) {
            return "create";
        }

        try {
            postService.savePost(postDto);
        }
        catch (PostObjectIsNullException exception)
        {
            log.info("Exception occurred --> {}", exception.getMessage());
        }
        catch (DataIntegrityViolationException exception)
        {
            log.info("Constraint Exception occurred --> {}", exception.getMessage());
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", "Title Not Accepted, Already Exists!");
            //model.addAttribute("postDto", postDto);

            return "create";
        }

        return "redirect:/posts";
    }

    @ModelAttribute
    public void createPostModel(Model model)
    {
        model.addAttribute("postDto", new PostDto());
    }

    @GetMapping("/view")
    public String viewPost(@RequestParam(name = "postId") Integer postId, Model model)
    {
        log.info("Post Id --> {}", postId);

        try {
            Post post = postService.findById(postId);
            model.addAttribute("post", post);
            log.info("Post Object --> {}", post);
        }
        catch (PostDoesNotExistException e) {
            return "redirect:/posts";
        }

        return "post";
    }

    @GetMapping("/info/{postId}")
    public String getPostDetails(@PathVariable(name = "postId") Integer postId, Model model)
    {
        log.info("Request for a post path --> {}", postId);

        try {
            Post savedPost = postService.findById(postId);
            model.addAttribute("postInfo", savedPost);
            log.info("Post Object --> {}", savedPost);
        }
        catch (PostDoesNotExistException e) {
            return "redirect:/posts";
        }

        return "post";
    }
}
