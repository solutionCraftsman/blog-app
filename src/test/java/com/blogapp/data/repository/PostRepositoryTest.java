package com.blogapp.data.repository;

import com.blogapp.data.models.Author;
import com.blogapp.data.models.Comment;
import com.blogapp.data.models.Post;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
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
    void savePost()
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

        Post savedPost = postRepository.findByTitle("What is Fintech?");
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getAuthor()).isNotNull();
    }

    @Test
    void findAllPosts()
    {
        List<Post> posts = postRepository.findAll();
        assertThat(posts).isNotNull();
        assertThat(posts).hasSize(5);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void deletePostById()
    {
        Post savedPost = postRepository.findById(41).orElse(null);
        assertThat(savedPost).isNotNull();
        log.info("Post fetched from the database --> {}", savedPost);
        //delete post
        postRepository.deleteById(savedPost.getId());

        Post deletedPost = postRepository.findById(savedPost.getId()).orElse(null);
        assertThat(deletedPost).isNull();
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void updatePostAuthor()
    {
        Post savedPost = postRepository.findById(41).orElse(null);
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getAuthor()).isNull();
        log.info("Post fetched from the database --> {}", savedPost);

        //update post
        Author author = new Author();
        author.setFirstname("Ayo");
        author.setLastname("Dele");
        author.setEmail("ayo@dele.com");
        author.setPhoneNumber("12345678901");

        //map relationships
        savedPost.setAuthor(author);
        author.addPost(savedPost);

        postRepository.save(savedPost);
        log.info("Blog post After saving --> {}", savedPost);

        Post updatedPost = postRepository.findById(savedPost.getId()).orElse(null);
        assertThat(updatedPost).isNotNull();
        assertThat(updatedPost.getAuthor()).isNotNull();
        assertThat(updatedPost.getAuthor()).isEqualTo(author);
    }

    @Test
    void updateSavedPostTitle()
    {
        Post savedPost = postRepository.findById(41).orElse(null);
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo("Title post 1");
        log.info("Post fetched from the database --> {}", savedPost);

        //update post
        savedPost.setTitle("Pentax Post title");
        postRepository.save(savedPost);

        Post updatedPost = postRepository.findById(savedPost.getId()).orElse(null);
        assertThat(updatedPost).isNotNull();
        assertThat(updatedPost.getTitle()).isEqualTo("Pentax Post title");
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void addCommentToExistingPost()
    {
        Post savedPost = postRepository.findById(41).orElse(null);
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getComments()).isEmpty();
        log.info("Post fetched from the database --> {}", savedPost);

        Comment comment1 = new Comment("Onye Lafta", "HeheHahaHoHo");
        Comment comment2 = new Comment("Anada Onye Lafta", "Ngburugburugburu");

        savedPost.addComments(comment1, comment2);
        postRepository.save(savedPost);

        Post updatedPost = postRepository.findById(savedPost.getId()).orElse(null);
        assertThat(updatedPost).isNotNull();
        assertThat(updatedPost.getComments()).isNotNull();
        assertThat(updatedPost.getComments()).hasSize(2);

        log.info("Updated Post from the database --> {}", updatedPost);
    }
}