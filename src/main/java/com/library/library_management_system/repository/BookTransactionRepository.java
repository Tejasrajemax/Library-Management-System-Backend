
package com.library.library_management_system.repository;

import com.library.library_management_system.model.BookTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookTransactionRepository extends JpaRepository<BookTransaction, Long> {

    // Find all transactions of a particular book
    List<BookTransaction> findByBookCode(String bookCode);

    // Find all transactions of a particular student
    List<BookTransaction> findByEnrollmentNo(Long enrollmentNo);

    // Find all active issued books of a student
    List<BookTransaction> findByEnrollmentNoAndStatus(
            Long enrollmentNo,
            BookTransaction.TransactionStatus status
    );

    // Count active issued books of a student
    long countByEnrollmentNoAndStatus(
            Long enrollmentNo,
            BookTransaction.TransactionStatus status
    );

    // Find all transactions handled by a librarian
    List<BookTransaction> findByLibrarianId(String librarianId);
}
