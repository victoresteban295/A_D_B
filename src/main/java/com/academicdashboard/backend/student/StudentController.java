package com.academicdashboard.backend.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> getStudents() {
        return new ResponseEntity<List<Student>>(studentService.findAllStudents(), HttpStatus.OK);
    }

    @DeleteMapping("/{firstName}") 
    public ResponseEntity<Void> deleteStudentByFirstName(@PathVariable String firstName) {
        studentService.deleteStudent(firstName);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
