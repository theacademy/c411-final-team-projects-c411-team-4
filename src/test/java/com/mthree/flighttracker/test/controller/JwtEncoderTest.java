package com.mthree.flighttracker.test.controller;

import com.mthree.flighttracker.FlighttrackerApplication;
import com.mthree.flighttracker.controller.JwtEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FlighttrackerApplication.class)
@Sql(scripts = {"/test_clean_tables.sql", "/test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JwtEncoderTest {
    @Autowired
    JwtEncoder jwtEncoder;

    @Test
    public void testUserGetsValidJWT() {
        final String originalUsername = "kyannelli";
        final String jwt = jwtEncoder.generateToken(originalUsername);
        assertEquals(jwtEncoder.validateToken(jwt), originalUsername);
    }

    @Test
    public void testBadJwtValidations() {
        assertEquals(null, jwtEncoder.validateToken(null));
        assertEquals(null, jwtEncoder.validateToken(""));
    }
}
