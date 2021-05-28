package com.blogapp.data.repository;

import com.blogapp.data.models.Author;
import com.blogapp.data.models.Post;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void save()
    {
        Post post = new Post();
        post.setTitle("What is Fintech?");
        post.setContent("Lorem Ipsum is simply dummy text of the printing and typesetting");

        postRepository.save(post);
        log.info("save() - Created a blog post --> {}", post);
        assertThat(post.getId()).isNotNull();
    }

    @Test
    void throwException_whenSavingPost_withDuplicateTitle()
    {
        Post post = new Post();
        post.setTitle("What is Fintech?");
        post.setContent("Lorem Ipsum is simply dummy text of the printing and typesetting");

        postRepository.save(post);
        log.info("duplicateTitle() 1 - Created a blog post --> {}", post);
        assertThat(post.getId()).isNotNull();

        Post post2 = new Post();
        post2.setTitle("What is Fintech?");
        post2.setContent("Lorem Ipsum is simply dummy text of the printing and typesetting");

        assertThrows(DataIntegrityViolationException.class,
                () -> postRepository.save(post2));
        log.info("duplicateTitle() 2 - blog post2 --> {}", post2);
    }

    @Test
    void whenPostIsSaved_thenSaveAuthor()
    {
        Post post = new Post();
        post.setTitle("What is Fintech?");
        post.setContent("Lorem Ipsum is simply dummy text of the printing and typesetting");

        Author author = new Author();
        author.setFirstname("Ayo");
        author.setLastname("Dele");
        author.setEmail("ayo@dele.com");
        author.setPhoneNumber("12345678901");

        //map relationships
        post.setAuthor(author);
        author.addPost(post);

        postRepository.save(post);
        log.info("postAuthor() - Blog post After saving --> {}", post);
    }

    @Test
    void findAllPosts() {
        List<Post> posts = postRepository.findAll();
        assertThat(posts).isNotNull();
        assertThat(posts).hasSize(5);
    }
}