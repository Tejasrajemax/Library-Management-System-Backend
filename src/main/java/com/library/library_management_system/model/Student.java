package com.library.library_management_system.model;

// jakarta.persistence package used to manage the relational data and object relational mapping (OMR)
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
public class Student {

    @Id
    // for auto-increment behaviour in mySQL
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enrollment_no", nullable = false, unique = true)
    private Long enrollmentNo;

    // This JPA Annotation that tells Hibernate (How this java field behaves in database)
    // It says that Name can not be empty
    @Column(nullable = false)
    private String name;

    private String phone;
    
    @Column(name = "fine_balance", nullable = false)
    private BigDecimal fineBalance = BigDecimal.ZERO;

    // insertable and updatable means to controls database value
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnrollmentNo() {
        return enrollmentNo;
    }

    public void setEnrollmentNo(Long enrollmentNo) {
        this.enrollmentNo = enrollmentNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getFineBalance() {
        return fineBalance;
    }

    public void setFineBalance(BigDecimal fineBalance) {
        this.fineBalance = fineBalance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}