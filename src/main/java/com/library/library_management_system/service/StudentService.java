package com.library.library_management_system.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

//  Import Student and StudentRepository module here
import com.library.library_management_system.model.Student;
import com.library.library_management_system.repository.StudentRepository;

// Stereotype package to use Service
import org.springframework.stereotype.Service;

// In spring it tells that this class contains Business or Service Logics to manage object
@Service
public class StudentService {

    // Repository Injection or dependency Injection
    // service needs the repository to communicate with the database.

    private final StudentRepository studentRepository;

    // Spring automatically gives StudentRepository object to Constructor
    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    // Generate Random Enrollment
    private Long generateEnrollmentNo() {
        for (int attempt = 0; attempt < 1000; attempt++) {
            long enrollmentNo =
                    ThreadLocalRandom.current().nextLong(10000000L, 100000000L);

            if (!studentRepository.existsByEnrollmentNo(enrollmentNo)) {
                return enrollmentNo;
            }
        }
        throw new IllegalStateException(
                "Unable to generate a unique enrollment number.");
    }

    public Student createStudent(Student student) {
        student.setId(null);
        student.setEnrollmentNo(generateEnrollmentNo());
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public Student getStudentById(Long enrollmentNo){
        return studentRepository.findById(enrollmentNo).orElse(null);
    }

    public Student updateStudent(Long enrollmentNo, Student updatedStudent){
        // Find the Student is it in database
        Student existingStudent = studentRepository.findById(enrollmentNo).orElse(null);

        if (existingStudent == null){
            return null;
        }
        // Field Which required to be updated
        existingStudent.setName(updatedStudent.getName());
        existingStudent.setPhone(updatedStudent.getPhone());

        // Make database updated with new one
        return studentRepository.save(existingStudent);
    }

    public boolean deleteStudent(Long enrollmentNo){
        if(!studentRepository.existsById(enrollmentNo)){
            return false;
        }
        studentRepository.deleteById(enrollmentNo);
        return true;
    }
}