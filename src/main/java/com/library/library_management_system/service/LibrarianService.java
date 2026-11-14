
package com.library.library_management_system.service;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import com.library.library_management_system.model.Librarian;
import com.library.library_management_system.repository.LibrarianRepository;

import org.springframework.stereotype.Service;

@Service
public class LibrarianService {

    private final LibrarianRepository librarianRepository;

    public LibrarianService(LibrarianRepository librarianRepository) {
        this.librarianRepository = librarianRepository;
    }

    private String generateLibrarianId(String name){
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("Librarian Name Required");
        }
        String letters = name.replaceAll("[^a-zA-Z]", "").toUpperCase(Locale.ROOT);
        if(letters.isEmpty()){
            throw new IllegalArgumentException("Name must contains Letters");
        }
        // Use Math.min here to handle the Exception because if I used (0,2) then it doesn't check name
        String prefix = letters.substring(0,Math.min(2,letters.length()));

        for(int attempt = 0; attempt < 1000; attempt++){
            // ThreadLocalRandom.current : used to retrive random number that can be generated under multithreading environment
            int number = ThreadLocalRandom.current().nextInt(1000,10000);
            String librarianId = prefix + number;

            if (!librarianRepository.existsByLibrarianId(librarianId)){
                return librarianId;
            }
        }
        throw new IllegalArgumentException("Unable to Generate Unique ID");
    }

    public Librarian createLibrarian(Librarian librarian) {
        librarian.setId(null)    ;
        librarian.setLibrarianId(generateLibrarianId(librarian.getName()));
        return librarianRepository.save(librarian);
    }

    public List<Librarian> getAllLibrarians() {
        return librarianRepository.findAll();
    }

    public Librarian getLibrarianById(String librarianId) {
        return librarianRepository.findByLibrarianId(librarianId).orElse(null);
    }

    public Librarian updateLibrarian(String librarianId, Librarian updatedLibrarian) {
        Librarian existingLibrarian =
                librarianRepository.findByLibrarianId(librarianId).orElse(null);

        if (existingLibrarian == null) {
            return null;
        }

        existingLibrarian.setName(updatedLibrarian.getName());
        existingLibrarian.setPhone(updatedLibrarian.getPhone());
        existingLibrarian.setPassword(updatedLibrarian.getPassword());

        return librarianRepository.save(existingLibrarian);
    }

    public boolean deleteLibrarian(String librarianId) {
        Librarian existingLibrarian =
                librarianRepository.findByLibrarianId(librarianId).orElse(null);

        if (existingLibrarian == null) {
            return false;
        }

        librarianRepository.delete(existingLibrarian);
        return true;
    }
}
