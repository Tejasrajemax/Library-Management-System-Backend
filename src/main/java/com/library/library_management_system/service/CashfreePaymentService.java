package com.library.library_management_system.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.library.library_management_system.model.FinePayment;
import com.library.library_management_system.model.Student;
import com.library.library_management_system.model.FinePayment.PaymentStatus;
import com.library.library_management_system.repository.FinePaymentRepository;
import com.library.library_management_system.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class CashfreePaymentService {

    private final FinePaymentRepository finePaymentRepository;
    private final StudentRepository studentRepository;

    // To sent HTTP request from spring boot application to cashfree
    private final RestClient restClient;
    // Store Cashfree Sandbox App id
    private final String clientId;
    // Store cashfree Sandbox Secret key
    private final String clientSecret;
    // Stores Cashfree API version that application wants to use
    private final String apiVersion;

    public CashfreePaymentService(FinePaymentRepository finePaymentRepository,
                                StudentRepository studentRepository,
                                RestClient.Builder restClientBuilder,

                                @Value("${cashfree.client-id}") String clientId,
                                @Value("${cashfree.client-secret}") String clientSecret,
                                @Value("${cashfree.base-url}") String baseUrl,
                                @Value("${cashfree.api-version}") String apiVersion) {
                                    
                                    this.finePaymentRepository = finePaymentRepository;
                                    this.studentRepository = studentRepository;
                                    this.clientId = clientId;
                                    this.clientSecret = clientSecret;
                                    this.apiVersion = apiVersion;

// creates a reusable HTTP client configured to communicate with Cashfree Sandbox, so you don't need to write the complete Cashfree URL every time you make an API request.
                                    this.restClient = restClientBuilder.baseUrl(baseUrl).build();
                                    
                                }

// Create CashFree Payment Order
    public FinePayment createPaymentOrder(Long enrollmentNo, BigDecimal amount){
        // Find Student
        Student student = studentRepository.findByEnrollmentNo(enrollmentNo)
                                        .orElseThrow(() -> new RuntimeException("Student Not Found"));
        
        // Validate Payment
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Payment must greater than Zero");
        }

        // Check Dues Fine
        BigDecimal currentFine = student.getFineBalance();
        if(currentFine == null || currentFine.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("No Any Fine Dues");
        }

        if (amount.compareTo(currentFine) > 0){
            throw new RuntimeException("Payment Can't Exceed Due Fine");
        }
        
        // Generate Unique Cashfree Order ID
        String orderId = "LIB_" + UUID.randomUUID().toString().replace("-", "");

        // Prepare Customer Details
// Map<String, Object> is used to build a quick JSON request body for the Cashfree API without creating a separate Java DTO (Data Transfer Object) class.
        Map<String, Object> customerDetails = Map.of(
                    "customer_id", String.valueOf(enrollmentNo),
                    "customer_name", student.getName(),
                    "customer_phone", student.getPhone()
        );

        // Prepare Order Request
        Map<String, Object> request = Map.of(
            "order_id", orderId,
            "order_amount", amount,
            "order_currency", "INR",
            "customer_details", customerDetails,
            // tells Cashfree that the next value is the webpage address where the student should return after checkout.
            "order_meta", Map.of(
                "return_url",
                "http://localhost:8080/payment-result.html?order_id={order_id}"
            )
        );

        
        try{
            // Send Request to CashFree
            // restClient: want to send an HTTP POST request.
            // Post: want to create a new payment order on Cashfree
            // uri("/orders"): specifies which Cashfree API endpoint we want to access.
                            // "Form this URl: https://sandbox.cashfree.com/pg/orders" (where we send our payment order.)
            Map response = restClient.post().uri("/orders")

                                // tells Cashfree "The data I'm sending is in JSON format."
                                    .contentType(MediaType.APPLICATION_JSON)

                                // sends your Cashfree App ID in the HTTP request header.
                                // & (that tells Cashfree which merchant application is making the request.)
                                    .header("x-client-id", clientId)

                                // sends your Cashfree Secret Key & authenticates your backend with Cashfree
                                    .header("x-client-secret", clientSecret)

                                // tells Cashfree which API version your application wants to use.
                                    .header("x-api-version", apiVersion)

                                // where we attach the actual payment order information. Line 80
                                    .body(request)

                                //tells Spring Now execute the HTTP request and retrieve the response from Cashfree.
                                    .retrieve()

                                // converts Cashfree's JSON response into a Java Map.
                                    .body(Map.class);

            // Validate Payment Session ID or response
            if(response == null || response.get("payment_session_id") == null){
                throw new RuntimeException("CashFree Did not Return a Payment Session");
            }

            // Save Payment Details
            FinePayment payment = new FinePayment();

            payment.setEnrollmentNo(enrollmentNo);
            payment.setOrderId(orderId);
            payment.setPaymentSessionId(response.get("payment_session_id").toString());
            payment.setAmount(amount);
            payment.setCurrency("INR");
            payment.setStatus(FinePayment.PaymentStatus.CREATED);

            return finePaymentRepository.save(payment);


        }
        catch (RestClientResponseException e) {
            throw new RuntimeException("Cashfree order creation failed: " +
                    e.getResponseBodyAsString(), e);
        }
    }

// Verify Payment
    @Transactional
    public FinePayment verifyPayment(String orderId) {

        // 1. Find payment record in MySQL
        FinePayment payment = finePaymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order Id Not Found"));

        // 2. If already paid, do not deduct again
        if (payment.getStatus() == PaymentStatus.PAID) {
            
            // Generate receipt number if it is missing
            if (payment.getReceiptNumber() == null){
                String receiptNumber = "REC-" + payment.getPaidAt().getYear() + "-" +
                                                String.format("%06d", payment.getId());
                payment.setReceiptNumber(receiptNumber);
                return finePaymentRepository.save(payment);
            }
            return payment;
        }

        // 3. Ask Cashfree for order details
        Map orderResponse = restClient.get()
                .uri("/orders/{orderId}", orderId)
                .header("x-client-id", clientId)
                .header("x-client-secret", clientSecret)
                .header("x-api-version", apiVersion)
                .retrieve()
                .body(Map.class);

        // 4. Check Cashfree order status
        System.out.println("Cashfree Order Response: " + orderResponse);
        String orderStatus = orderResponse.get("order_status").toString();

        if (!"PAID".equalsIgnoreCase(orderStatus)) {
            return payment;
        }

        // 5. Check order amount
        BigDecimal cashfreeAmount =
                new BigDecimal(orderResponse.get("order_amount").toString());

        if (cashfreeAmount.compareTo(payment.getAmount()) != 0) {
            throw new RuntimeException("Payment Amount does not Match");
        }

        // 6. Fetch actual payment transactions from Cashfree
        List<Map<String, Object>> paymentResponses = restClient.get()
        .uri("/orders/{orderId}/payments", orderId)
        .header("x-client-id", clientId)
        .header("x-client-secret", clientSecret)
        .header("x-api-version", apiVersion)
        .retrieve()
        .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});
        
        // 7. Find a successful payment
        Map<String, Object> successfulPayment = null;

        for (Map<String, Object> paymentResponse : paymentResponses) {

            String paymentStatus =
                    paymentResponse.get("payment_status").toString();

            if ("SUCCESS".equalsIgnoreCase(paymentStatus)) {
                successfulPayment = paymentResponse;
                break;
            }
        }
        System.out.println("Cashfree Payment Response: " + paymentResponses);

        // 8. If no successful transaction exists, do not deduct fine
        if (successfulPayment == null) {
            return payment;
        }

        // 9. Verify transaction amount
        BigDecimal successfulAmount =
                new BigDecimal(successfulPayment.get("payment_amount").toString());

        if (successfulAmount.compareTo(payment.getAmount()) != 0) {
            throw new RuntimeException("Successful Payment Amount does not Match");
        }

        // 10. Save Cashfree transaction ID
        String cfPaymentId = successfulPayment.get("cf_payment_id").toString();

        payment.setCfPaymentId(cfPaymentId);

        // 11. Find the student
        Student student = studentRepository
                .findByEnrollmentNo(payment.getEnrollmentNo())
                .orElseThrow(() -> new RuntimeException("Student Not Found"));

        // 12. Check remaining fine balance
        if (student.getFineBalance().compareTo(payment.getAmount()) < 0) {
            throw new RuntimeException("Fine Balance is Less than Payment Amount");
        }

        // 13. Deduct the successfully paid amount
        student.setFineBalance(
                student.getFineBalance().subtract(payment.getAmount())
        );

        studentRepository.save(student);

        // 14. Mark payment as PAID
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());

        //  Geerate a Permanent receipt number
        if(payment.getReceiptNumber() == null){
            String receiptNumber = "REC-" + payment.getPaidAt().getYear() + "-" +
                                    String.format("%06d", payment.getId());
            payment.setReceiptNumber(receiptNumber);
        }

        return finePaymentRepository.save(payment);
    }
}