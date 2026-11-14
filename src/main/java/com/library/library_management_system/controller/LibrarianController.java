package com.library.library_management_system.controller;

import com.library.library_management_system.model.Librarian;
import com.library.library_management_system.service.LibrarianService;

import java.util.*;

import org.springframework.web.bind.annotation.*;


@RestController
public class LibrarianController {
    private final LibrarianService librarianService;

    public LibrarianController(LibrarianService librarianService){
        this.librarianService = librarianService;
    }

    @PostMapping("/librarians")
    public Librarian createLibrarian(@RequestBody Librarian librarian){
        return librarianService.createLibrarian(librarian);
    }
    
    @GetMapping("/librarians")
    public List<Librarian> getAllLibrarians(){
        return librarianService.getAllLibrarians();
    }

    @GetMapping("/librarians/{librarianId}")
    public Librarian getLibrarianById(@PathVariable String librarianId){
        return librarianService.getLibrarianById(librarianId);
    }

    @PutMapping("/librarians/{librarianId}")
    public Librarian updateLibrarian(@PathVariable String librarianId, @RequestBody Librarian librarian){
        return librarianService.updateLibrarian(librarianId, librarian);
    }

    @DeleteMapping("/librarians/{librarianId}")
    public String deleteLibrarian(@PathVariable String librarianId){
        boolean deleted = librarianService.deleteLibrarian(librarianId);
        if(!deleted){
            return "Librarian Not Found";
        }
        return "Librarian Deleted Successfully";
    }
    
}