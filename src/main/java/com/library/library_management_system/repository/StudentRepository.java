package com.library.library_management_system.repository;

import java.util.Optional;
import com.library.library_management_system.model.Student;

// "JpaRepository" Spring Data JPA ka interface hai jo hume database ke common
// operations already provide karta hai. otherwise humne sara SQL Query khud handle krna pdhta
// JpaRepository hume readymade database operations me help krta hi
import org.springframework.data.jpa.repository.JpaRepository;

// Interface use because Spring Data JPA automatically repository k implementation create krta hi
// otherwise agar hum simple public class use krte to hume database operation implement krna padta
// jpaRepository sare readymade methods dega
public interface StudentRepository extends JpaRepository <Student, Long> {
    Optional<Student> findByEnrollmentNo(Long enrollmentNo);

    boolean existsByEnrollmentNo(Long enrollmentNo);
}