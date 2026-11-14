package com.library.library_management_system.model;

import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name = "books")

public class Book {
    
    // Java uses camelCase, while MySQL uses snake_case.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "book_code", nullable = false, unique = true)
    private String bookCode;

    @Column (nullable = false)
    private String title;

    @Column (nullable = false)
    private String author;

    @Column (name = "total_copies", nullable = false)
    private Integer totalCopies;

    @Column (name = "available_copies", nullable = false)
    private Integer availableCopies;

    @Column (name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public Book(){
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public String getBookCode(){
        return bookCode;
    }
    public void setBookCode(String bookCode){
        this.bookCode = bookCode;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getAuthor(){
        return author;
    }
    public void setAuthor(String author){
        this.author = author;
    }

    public Integer getTotalCopies(){
        return totalCopies;
    }
    public void setTotalCopies(Integer totalCopies){
        this.totalCopies = totalCopies;
    }

    public Integer getAvailableCopies(){
        return availableCopies;
    }
    public void setAvailableCopies(Integer availableCopies){
        this.availableCopies = availableCopies;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
}
