package com.blogapp.data.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue
    //@Column(length = 500)
    private UUID id;

    private String authorName;

    @Column(nullable = false, length = 150)
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Comment(String authorName, String content) {
        this.authorName = authorName;
        this.content = content;
    }
}
