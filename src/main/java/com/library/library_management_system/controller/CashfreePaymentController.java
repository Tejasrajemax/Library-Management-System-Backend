package com.library.library_management_system.controller;

import java.math.BigDecimal;
import java.util.*;

import javax.swing.Spring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.library_management_system.model.FinePayment;
import com.library.library_management_system.model.Student;
import com.library.library_management_system.repository.StudentRepository;
import com.library.library_management_system.service.CashfreePaymentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController

// Defines common url for all endpoint inside this controller i,e payments Ex. Url= "http://localhost:8080/payments/....."
@RequestMapping("/payments")
public class CashfreePaymentController {
    private final CashfreePaymentService cashfreePaymentService;
    private final StudentRepository studentRepository;
// Contructor dependency Injection : Spring automatically provides the CashfreePaymentService (ya or koi Services) object to the controller.
    public CashfreePaymentController(CashfreePaymentService cashfreePaymentService,StudentRepository studentRepository) {
        this.cashfreePaymentService = cashfreePaymentService;
        this.studentRepository = studentRepository;
    }

    // Create Cashfree payment order

// tells Spring that this method handles HTTP POST requests.
// {enrollmentNo} is a variable part of the URL. (Ex. /payments/create/66190499)
    @PostMapping("/create/{enrollmentNo}")

// PathVariable: Gets the enrollment number from the URL.
// @RequestBody: Gets the payment information from the JSON

// to send an HTTP response to the frontend, including the status code and response data.
// <?> wildcard: It means the response body can contain different types of data.
           // tells Java what type of result the method will return.
    public ResponseEntity<?> createPayment(@PathVariable Long enrollmentNo,
                                    // send or receive multiple pieces of data together as key-value pairs.
                                        @RequestBody Map<String, BigDecimal> request){
                                            BigDecimal amount = request.get("amount");
                                            FinePayment payment = cashfreePaymentService.createPaymentOrder(enrollmentNo, amount);
                                        // sends a successful HTTP 200 response containing the payment details.
                                            return ResponseEntity.ok(Map.of("Message", "Payment Order Created Successfully",
                                                                            "orderId", payment.getOrderId(),
                                                                            "paymentSessionId", payment.getPaymentSessionId(),
                                                                            "amount", payment.getAmount(),
                                                                            "status", payment.getStatus() ));
                                        }

// Verify Cashfree Payment
// Checks whether the student actually paid and, if successful, deducts the amount from the student's fine balance.
    @PostMapping("/verify/{orderId}")
    public ResponseEntity<?> verifyPayment(@PathVariable String orderId){
        FinePayment payment = cashfreePaymentService.verifyPayment(orderId);
        // Verify Students Exists or not
        Student student = studentRepository.findByEnrollmentNo(payment.getEnrollmentNo())
                                        .orElseThrow(() -> new RuntimeException("Student Not Found"));
// Spring Boot to return an HTTP response with status 200 OK. with Following information that help to send it to Frontend using Javascript
        return ResponseEntity.ok(Map.of(
        "message", "Payment Verification Completed",
        "studentName", student.getName(),
        "enrollmentNo", payment.getEnrollmentNo(),
        "amount", payment.getAmount(),
        "orderId", payment.getOrderId(),
        "receiptNumber", payment.getReceiptNumber(),
        "cfPaymentId", payment.getCfPaymentId(),
        "status", payment.getStatus(),
        "paidAt", payment.getPaidAt() ));
    }
}
