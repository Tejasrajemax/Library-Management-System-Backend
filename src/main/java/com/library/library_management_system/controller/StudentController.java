package com.library.library_management_system.controller;
import com.library.library_management_system.model.Student;
import java.util.*;
// "import org.springframework.web.bind.annotation.*" also we can use this here but to learn we use this
import com.library.library_management_system.service.StudentService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




// In Spring this class Handles HTTP/REST request
@RestController
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

//  To Save the data
    @PostMapping("/students")
    public Student createStudent(@RequestBody Student student){
        return studentService.createStudent(student);
    }

// To Access the data
    @GetMapping("/students")
    public List<Student> getAllStudents(){
        return studentService.getAllStudents();
    }
    
// To Access Student with specific ID
    @GetMapping("/students/{enrollmentNo}")
    public Student getStudentById(@PathVariable Long enrollmentNo){
        return studentService.getStudentById(enrollmentNo);
    }

// To Update the data of specific student
    @PutMapping("/students/{enrollmentNo}")
    public Student updateStudent(@PathVariable Long enrollmentNo, @RequestBody Student student){
        return studentService.updateStudent(enrollmentNo, student);
    }
    
// Delete the data
    @DeleteMapping ("/students/{enrollmentNo}")
    public String deleteStudent(@PathVariable Long enrollmentNo){
        boolean deleted = studentService.deleteStudent(enrollmentNo);
        if(!deleted){
            return "Student not found";
        }
        return "Student Deleted Successfully";
    }
}
    


