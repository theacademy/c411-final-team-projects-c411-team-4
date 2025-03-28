package com.mthree.flighttracker.test.controller;

import com.mthree.flighttracker.FlighttrackerApplication;
import com.mthree.flighttracker.controller.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FlighttrackerApplication.class)
@Sql(scripts = {"/test_clean_tables.sql", "/test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PasswordEncoderTest {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void testPasswordMatchesAfterEncoding() {
        final String originalPassword = "myP@ssw0rd";
        final String encodedPassword = passwordEncoder.encode(originalPassword);
        assertTrue(passwordEncoder.matches(originalPassword, encodedPassword));
    }

    @Test
    public void testPasswordNonMatches() {
        final String originalPassword = "myP@ssw0rd";
        final String encodedPassword = passwordEncoder.encode(originalPassword);
        assertFalse(passwordEncoder.matches(originalPassword, originalPassword));
        assertThrows(IllegalArgumentException.class, () -> passwordEncoder.matches(encodedPassword, originalPassword));
        assertThrows(IllegalArgumentException.class, () -> passwordEncoder.matches("", encodedPassword));
        assertThrows(IllegalArgumentException.class, () -> passwordEncoder.matches(originalPassword, ""));
    }
}
