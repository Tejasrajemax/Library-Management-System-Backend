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
    public Map<String, Object> createLibrarian(@RequestBody Librarian librarian){
        Librarian savedLibrarian = librarianService.createLibrarian(librarian);

        return librarianResponse(savedLibrarian);
    }

    // Convert librarian details into a safe response
    private Map<String, Object> librarianResponse(Librarian librarian){
        if(librarian == null){
            return null;
        }
// Used LinkedHashMap to maintain te Insertion Order
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", librarian.getId());
        response.put("librarianId", librarian.getLibrarianId());
        response.put("name", librarian.getName());
        response.put("phone", librarian.getPhone());
        response.put("createdAt", librarian.getCreatedAt());

        return response;
    }
    
    @GetMapping("/librarians")
    public List<Map<String, Object>> getAllLibrarians(){
        List<Librarian> librarians = librarianService.getAllLibrarians();

// response created as Empty list fro safe response
        List<Map<String,Object>> response = new ArrayList<>();

// librarians used here to get all librarian record from database
        for(Librarian librarian : librarians){

// librarianResponse(): to remove all the password while gathering all the librarian records
            response.add(librarianResponse(librarian));
        }
        return response;
    }

    @GetMapping("/librarians/{librarianId}")
    public Map<String, Object> getLibrarianById(@PathVariable String librarianId){
        Librarian librarian = librarianService.getLibrarianById(librarianId);
        return librarianResponse(librarian);
    }

    @PutMapping("/librarians/{librarianId}")
    public Map<String, Object> updateLibrarian(@PathVariable String librarianId, @RequestBody Librarian librarian){
        Librarian updatedLibrarian = librarianService.updateLibrarian(librarianId, librarian);
        return librarianResponse(updatedLibrarian);
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