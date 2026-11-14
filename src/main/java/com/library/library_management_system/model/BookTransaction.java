package com.library.library_management_system.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.*;

@Entity
@Table(name = "book_transactions")

public class BookTransaction {
    public enum TransactionStatus {
        ISSUED,
        RETURNED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enrollment_no", nullable = false)
    private Long enrollmentNo;

    @Column(name = "book_code", nullable = false)
    private String bookCode;

    @Column(name = "librarian_id", nullable = false)
    private String librarianId;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "fine_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal fineAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.ISSUED;

    public BookTransaction(){

    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public Long getEnrollmentNo(){
        return enrollmentNo;
    }
    public void setstudentId(Long enrollmentNo){
        this.enrollmentNo = enrollmentNo;
    }

    public String getBookCode(){
        return bookCode;
    }
    public void setBookCode(String bookCode){
        this.bookCode = bookCode;
    }

    public String getLibrarianId(){
        return librarianId;
    }
    public void setLibrarianId(String librarianId){
        this.librarianId = librarianId;
    }

    public LocalDate getIssueDate(){
        return issueDate;
    }
    public void setIssueDate(LocalDate issueDate){
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate(){
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate){
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate(){
        return returnDate;
    }
    public void setReturnDate(LocalDate returnDate){
        this.returnDate = returnDate;
    }

    public TransactionStatus getStatus(){
        return status;
    }
    public void setStatus(TransactionStatus status){
        this.status = status;
    }

    public BigDecimal getFineAmount(){
        return fineAmount;
    }
    public void setFineAmount(BigDecimal fineAmount){
        this.fineAmount = fineAmount;
    }
}
