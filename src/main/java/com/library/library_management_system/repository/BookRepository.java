package com.library.library_management_system.repository;

import java.util.Optional;
import com.library.library_management_system.model.Book;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository <Book, Long>{
    Optional<Book> findByBookCode(String bookCode);
    // Use this to Check and Access the BookCode
    boolean existsByBookCode(String bookCode);
}
