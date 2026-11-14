package com.library.library_management_system.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.library.library_management_system.model.Book;
import com.library.library_management_system.model.BookTransaction;
import com.library.library_management_system.model.Librarian;
import com.library.library_management_system.model.Student;
import com.library.library_management_system.model.BookTransaction.TransactionStatus;
import com.library.library_management_system.repository.BookRepository;
import com.library.library_management_system.repository.BookTransactionRepository;
import com.library.library_management_system.repository.LibrarianRepository;
import com.library.library_management_system.repository.StudentRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class BookTransactionService {
    private final BookTransactionRepository bookTransactionRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final LibrarianRepository librarianRepository;
    

    public BookTransactionService(
        BookTransactionRepository bookTransactionRepository,
        BookRepository bookRepository,
        StudentRepository studentRepository,
        LibrarianRepository librarianRepository) {

        this.bookTransactionRepository = bookTransactionRepository;
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
        this.librarianRepository = librarianRepository;
    }
    // used to guarantee that a group of database operation either succeed completely or fail completely
    // Issued Book
    @Transactional
    public BookTransaction createBookTransaction(BookTransaction bookTransaction){
        // Validate Student
        Student student = studentRepository
            .findByEnrollmentNo(bookTransaction.getEnrollmentNo())
            .orElseThrow(() -> new RuntimeException("Student not found"));

        // Validate Book
        Book book = bookRepository
            .findByBookCode(bookTransaction.getBookCode())
            .orElseThrow(() -> new RuntimeException("Book Not Found"));
        
        // Validate Librarian
        Librarian librarian = librarianRepository
            .findByLibrarianId(bookTransaction.getLibrarianId())
            .orElseThrow(() -> new RuntimeException("Librarian Not Found"));

        // Check max 4 Active Book
        long ActiveB = bookTransactionRepository
            .countByEnrollmentNoAndStatus(student.getEnrollmentNo(), TransactionStatus.ISSUED);
        if(ActiveB >= 4){
            throw new RuntimeException("Student Already issued 4 Books");
        }

        // Check Fine
        BigDecimal MinFine = new BigDecimal("20.00");
        if(student.getFineBalance() != null &&
            student.getFineBalance().compareTo(MinFine) >=0 ){
                throw new RuntimeException("Having Fine More than Rs. 20 Not ");
            }
        
        // Check Book Available or not
        if(book.getAvailableCopies() == null || book.getAvailableCopies() <= 0){
            throw new RuntimeException("Book is currently Unavailable");
        }

        // Set transaction Details
        LocalDate today = LocalDate.now();

        bookTransaction.setId(null);
        bookTransaction.setIssueDate(today);
        bookTransaction.setDueDate(today.plusDays(15));
        bookTransaction.setReturnDate(null);
        bookTransaction.setFineAmount(BigDecimal.ZERO);
        bookTransaction.setStatus(TransactionStatus.ISSUED);

        // Reduced Available Copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // Save Transaction
        return bookTransactionRepository.save(bookTransaction);
    }

    // Get all transaction
    public List<BookTransaction> getAllBookTransaction(){
        return bookTransactionRepository.findAll();
    }

    // Get all transaction history by book code
    public List<BookTransaction> getBookTransactionByCode(String bookCode) {
        return bookTransactionRepository.findByBookCode(bookCode);
    }

    // Get all transaction history by student enrollment
    public List<BookTransaction> getBookTransactionsByStudent(Long enrollmentNo){
        return bookTransactionRepository.findByEnrollmentNo(enrollmentNo);
    }

    @Transactional
    public BookTransaction returnBook(Long transactionId){
        
        // Find Trasaction
        BookTransaction transaction = bookTransactionRepository
            .findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction Not Found"));
        
        // Check Trasaction
        if(transaction.getStatus() == TransactionStatus.RETURNED){
            throw new RuntimeException("Book Already Returned");
        }

        // Find Student
        Student student = studentRepository
            .findByEnrollmentNo(transaction.getEnrollmentNo()).orElseThrow(() -> new RuntimeException("Student Not Found"));
        
        // Calculate Overdue Days
        LocalDate returnDate = LocalDate.now();
        long DueDays = Math.max(0,ChronoUnit.DAYS.between(transaction.getDueDate(), returnDate));

        // Calculate Fine
        BigDecimal fine = BigDecimal.valueOf(DueDays)
            .multiply(new BigDecimal("5.00"));

        // Update Student FIne
        BigDecimal NowFine = student.getFineBalance();
        if(NowFine == null){
            NowFine = BigDecimal.ZERO;
        }
        student.setFineBalance(NowFine.add(fine));

        // Find book by BookCode
        // Find Book using Book Code
        Book book = bookRepository
                .findByBookCode(transaction.getBookCode())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Update Book Availability
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        // update transaction
        transaction.setReturnDate(returnDate);
        transaction.setFineAmount(fine);
        transaction.setStatus(TransactionStatus.RETURNED);

         // 10. Save changes
        studentRepository.save(student);
        bookRepository.save(book);
        return bookTransactionRepository.save(transaction);
    }
}
