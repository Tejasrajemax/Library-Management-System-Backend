package com.library.library_management_system.repository;

import com.library.library_management_system.model.Librarian;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
public interface LibrarianRepository extends JpaRepository <Librarian, Long>{
    Optional<Librarian> findByLibrarianId(String librarianId);
    boolean existsByLibrarianId(String librarianId);
}
