package com.library.library_management_system.service;

import com.library.library_management_system.controller.LibrarianController;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.library.library_management_system.config.AdminConfig;
import com.library.library_management_system.repository.LibrarianRepository;

@Service
public class AuthService {

    private final LibrarianController librarianController;
    private final LibrarianService librarianService;
    private final AdminConfig adminConfig;
    private final LibrarianRepository librarianRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminPasswordHash;

    public AuthService(
            AdminConfig adminConfig,
            LibrarianRepository librarianRepository,
            PasswordEncoder passwordEncoder, LibrarianService librarianService, LibrarianController librarianController) {

        this.adminConfig = adminConfig;
        this.librarianRepository = librarianRepository;
        this.passwordEncoder = passwordEncoder;

        this.adminPasswordHash = passwordEncoder.encode(adminConfig.getAdminPassword());
        this.librarianService = librarianService;
        this.librarianController = librarianController;
    }

// Verify Admin Password & Id
    public boolean authenticateAdmin(String adminId, String password){
        if(!adminConfig.getAdminId().equals(adminId)){
            return false;
        }
        return passwordEncoder.matches(password, adminPasswordHash);
    }

// Verify Librarian Paddword & Id
    public boolean authenticateLibrarian(String librarianId, String password){
        if(librarianId == null || password == null){
            return false;
        }
        return librarianRepository.findByLibrarianId(librarianId).map(librarian -> passwordEncoder
                                .matches(password, librarian.getPassword())).orElse(false);

    }
}
