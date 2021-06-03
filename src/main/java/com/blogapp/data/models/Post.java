package com.blogapp.data.models;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@Table(name = "blog_post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(length = 500)
    private String content;

    private String coverImageUrl;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Author author;

    @CreationTimestamp
    private LocalDateTime dateCreated;

    @UpdateTimestamp
    private LocalDateTime dateModified;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;

    public void addComments(Comment... comment)
    {
        if(comments == null) {
            comments = new ArrayList<>();
        }
        comments.addAll(Arrays.asList(comment));
    }
}
