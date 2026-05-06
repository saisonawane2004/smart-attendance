package com.example.smartattendance.service;

import com.example.smartattendance.entity.*;
import com.example.smartattendance.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final ClassroomRepository classroomRepository;
    private final SubjectRepository subjectRepository;

    public AdminService(UserRepository userRepository, StudentRepository studentRepository, FacultyRepository facultyRepository,
                        ClassroomRepository classroomRepository, SubjectRepository subjectRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.classroomRepository = classroomRepository;
        this.subjectRepository = subjectRepository;
    }

    public Page<Student> students(int page, int size) { return studentRepository.findAll(PageRequest.of(page, size)); }
    public Page<Faculty> faculty(int page, int size) { return facultyRepository.findAll(PageRequest.of(page, size)); }
    public Page<Subject> subjects(int page, int size) { return subjectRepository.findAll(PageRequest.of(page, size)); }
    public Page<Classroom> classrooms(int page, int size) { return classroomRepository.findAll(PageRequest.of(page, size)); }

    public Student addStudent(String name, String email, String password, String rollNumber, Long classId) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Role.STUDENT);
        userRepository.save(user);

        Student student = new Student();
        student.setUser(user);
        student.setRollNumber(rollNumber);
        student.setClassroom(classroomRepository.findById(classId).orElseThrow());
        return studentRepository.save(student);
    }

    public Faculty addFaculty(String name, String email, String password, String department) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Role.FACULTY);
        userRepository.save(user);

        Faculty faculty = new Faculty();
        faculty.setUser(user);
        faculty.setDepartment(department);
        return facultyRepository.save(faculty);
    }

    public Classroom addClassroom(String name, String section, Integer yearLevel) {
        Classroom classroom = new Classroom();
        classroom.setName(name);
        classroom.setSection(section);
        classroom.setYearLevel(yearLevel == null ? 1 : yearLevel);
        return classroomRepository.save(classroom);
    }

    public Subject addSubject(String name, Long facultyId, Long classId) {
        Subject subject = new Subject();
        subject.setName(name);
        subject.setFaculty(facultyRepository.findById(facultyId).orElseThrow());
        subject.setClassroom(classroomRepository.findById(classId).orElseThrow());
        return subjectRepository.save(subject);
    }
}
