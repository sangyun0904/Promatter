package com.example.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Board {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private String author;
    
    private LocalDateTime createdDate;
    
    private LocalDateTime modifiedDate;
    
    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
        this.modifiedDate = this.createdDate;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.modifiedDate = LocalDateTime.now();
    }
} 