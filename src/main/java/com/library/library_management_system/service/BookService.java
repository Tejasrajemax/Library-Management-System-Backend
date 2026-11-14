package com.library.library_management_system.service;

import java.util.*;
import java.util.concurrent.*;

import org.springframework.stereotype.Service;

import com.library.library_management_system.model.Book;
import com.library.library_management_system.repository.BookRepository;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    private String genBookCode(String title){
        if(title == null || title.isBlank()){
            throw new IllegalArgumentException("Book Name Required");
        }
        String Initial = title.replaceAll("[^a-zA-Z]", "").toUpperCase(Locale.ROOT);
        if(Initial.isEmpty()){
            throw new IllegalArgumentException("Title Must Contails Letter");
        }
        String prefix = Initial.substring(0,Math.min(2,Initial.length()));

        for(int attempt = 0; attempt < 1000; attempt++){
            int num = ThreadLocalRandom.current().nextInt(1000,10000);
            String bookCode = prefix + num;

            if(!bookRepository.existsByBookCode(bookCode)){
                return bookCode;
            }
        }
        throw new IllegalArgumentException("Unable to Generate unique ID");
    }

    public Book createBook(Book book){
        book.setId(null);
        book.setBookCode(genBookCode(book.getTitle()));
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book getBookById(String bookCode){
        return bookRepository.findByBookCode(bookCode).orElse(null);
    }

    public boolean deleteBook(String bookCode){
        Book existingBook = bookRepository.findByBookCode(bookCode).orElse(null);

        if(existingBook == null){
            return false;
        }

        bookRepository.delete(existingBook);
        return true;
    }
}
