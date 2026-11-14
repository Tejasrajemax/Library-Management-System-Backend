
package com.library.library_management_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.library_management_system.model.FinePayment;

public interface FinePaymentRepository extends JpaRepository<FinePayment, Long> {

    // Find payment using Cashfree order ID
    Optional<FinePayment> findByOrderId(String orderId);

    // Check whether an order already exists
    boolean existsByOrderId(String orderId);
}
