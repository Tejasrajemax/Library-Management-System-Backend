package com.library.library_management_system.controller;
import com.library.library_management_system.model.Book;
import com.library.library_management_system.service.BookService;

import java.util.*;

import org.springframework.web.bind.annotation.*;

@RestController
public class BookController{

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

// To Save Data
    @PostMapping ("/books")
    public Book createBook(@RequestBody Book book){
        return bookService.createBook(book);
    }

// To access book
    @GetMapping ("/books")
    public List<Book> getAllBooks(){
        return bookService.getAllBooks();
    }

// To Access Book with Specific Id
    @GetMapping("books/{bookCode}")
    public Book getStudentById(@PathVariable String bookCode){
        return bookService.getBookById(bookCode);
    }

// Delete data
    @DeleteMapping ("books/{bookCode}")
    public String deleteBook(@PathVariable String bookCode){
        boolean deleted = bookService.deleteBook(bookCode);
        if(!deleted){
            return "Book Not Found";
        }
        return "Book Deleted Successfully";
    }
}