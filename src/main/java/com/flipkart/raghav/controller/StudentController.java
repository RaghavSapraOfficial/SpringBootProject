package com.flipkart.raghav.controller;

import com.flipkart.raghav.model.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class StudentController {
    public List<Student> students = new ArrayList<>(Arrays.asList(
            new Student(1,"Navin",60),
            new Student(2,"Kiran",70)
    ));

    @GetMapping("/students")
    public List<Student> getAllStudents(){
        return students;
    }
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest httpServletRequest){
        return (CsrfToken) httpServletRequest.getAttribute("_csrf");
    }

    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student){
        students.add(student);
        return student;
    }

}
