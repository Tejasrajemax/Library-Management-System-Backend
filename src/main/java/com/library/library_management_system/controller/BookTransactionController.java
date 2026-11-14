package com.library.library_management_system.controller;

import com.library.library_management_system.model.BookTransaction;
import com.library.library_management_system.service.BookTransactionService;

import java.util.*;

import org.springframework.web.bind.annotation.*;

@RestController

public class BookTransactionController {
    private final BookTransactionService bookTransactionService;

    public BookTransactionController(BookTransactionService bookTransactionService){
        this.bookTransactionService = bookTransactionService;
    }

    @PostMapping("/book_transactions")
    public BookTransaction createBookTransaction(@RequestBody BookTransaction bookTransaction){
        return bookTransactionService.createBookTransaction(bookTransaction);
    }

    @GetMapping("/book_transactions")
    public List<BookTransaction> getAllBookTransactions(){
        return bookTransactionService.getAllBookTransaction();
    }

    @GetMapping("/book_transactions/{bookCode}")
    public List<BookTransaction> getAllBookTransactionsByCode(@PathVariable String bookCode) {
        return bookTransactionService.getBookTransactionByCode(bookCode);
    }

    @PutMapping("/book_transactions/return/{transactionId}")
    public BookTransaction returnBook(@PathVariable Long transactionId) {
        return bookTransactionService.returnBook(transactionId);
    }
}
