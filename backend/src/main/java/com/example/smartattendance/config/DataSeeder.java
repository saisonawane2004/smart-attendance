package com.example.smartattendance.config;

import com.example.smartattendance.entity.*;
import com.example.smartattendance.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seed(UserRepository userRepository, ClassroomRepository classroomRepository,
                           FacultyRepository facultyRepository, StudentRepository studentRepository,
                           SubjectRepository subjectRepository, AttendanceSessionRepository sessionRepository,
                           AttendanceRepository attendanceRepository) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail("admin@smart.com");
            admin.setPassword("1234");
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            User fUser = new User();
            fUser.setName("Faculty One");
            fUser.setEmail("faculty@smart.com");
            fUser.setPassword("1234");
            fUser.setRole(Role.FACULTY);
            userRepository.save(fUser);

            Faculty faculty = new Faculty();
            faculty.setUser(fUser);
            faculty.setDepartment("CSE");
            facultyRepository.save(faculty);

            Classroom c = new Classroom();
            c.setName("BSc CS");
            c.setSection("A");
            c.setYearLevel(1);
            classroomRepository.save(c);

            User sUser = new User();
            sUser.setName("Student One");
            sUser.setEmail("student@smart.com");
            sUser.setPassword("1234");
            sUser.setRole(Role.STUDENT);
            userRepository.save(sUser);

            Student student = new Student();
            student.setUser(sUser);
            student.setRollNumber("CS001");
            student.setClassroom(c);
            studentRepository.save(student);

            User sUser2 = new User();
            sUser2.setName("Student Two");
            sUser2.setEmail("student2@smart.com");
            sUser2.setPassword("1234");
            sUser2.setRole(Role.STUDENT);
            userRepository.save(sUser2);

            Student student2 = new Student();
            student2.setUser(sUser2);
            student2.setRollNumber("CS002");
            student2.setClassroom(c);
            studentRepository.save(student2);

            Subject subject = new Subject();
            subject.setName("Software Engineering");
            subject.setFaculty(faculty);
            subject.setClassroom(c);
            subjectRepository.save(subject);

            Subject subject2 = new Subject();
            subject2.setName("Database Systems");
            subject2.setFaculty(faculty);
            subject2.setClassroom(c);
            subjectRepository.save(subject2);

            for (int i = 1; i <= 12; i++) {
                AttendanceSession session = new AttendanceSession();
                session.setSubject(i % 2 == 0 ? subject : subject2);
                session.setQrToken(UUID.randomUUID().toString());
                session.setExpiryTime(LocalDateTime.now().minusDays(i).plusMinutes(5));
                session.setLatitude(18.5204);
                session.setLongitude(73.8567);
                session.setRadius(100.0);
                sessionRepository.save(session);

                Attendance a1 = new Attendance();
                a1.setStudent(student);
                a1.setSession(session);
                a1.setTimestamp(LocalDateTime.now().minusDays(i));
                a1.setStatus(AttendanceStatus.PRESENT);
                attendanceRepository.save(a1);

                if (i % 3 != 0) {
                    Attendance a2 = new Attendance();
                    a2.setStudent(student2);
                    a2.setSession(session);
                    a2.setTimestamp(LocalDateTime.now().minusDays(i));
                    a2.setStatus(AttendanceStatus.PRESENT);
                    attendanceRepository.save(a2);
                }
            }
        };
    }
}
