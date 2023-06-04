package com.academicdashboard.backend.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public void deleteStudent(String firstName) {
        studentRepository.deleteStudentByFirstName(firstName);
    }
}
