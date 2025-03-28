package com.mthree.flighttracker.test.dao;

import com.mthree.flighttracker.FlighttrackerApplication;
import com.mthree.flighttracker.controller.PasswordEncoder;
import com.mthree.flighttracker.dao.UserDao;
import com.mthree.flighttracker.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FlighttrackerApplication.class)
@Sql(scripts = {"/test_clean_tables.sql", "/test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserDAOTest {
    private static final String username = "kyannelli";
    private static final String email = "kyle.yannelli@mthree.com";
    private static final String password = "badPassword";

    @Autowired
    UserDao userDao;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void testUserSaves() {
        User user = new User()
                .setUsername(username + "not")
                .setEmail(email + "not")
                .setPassword(
                        passwordEncoder.encode(password)
                );
        user = userDao.save(user);
        assertNotNull(user.getId());
    }

    @Test
    public void testUserPasswordSavesCorrectly() {
        User user = new User()
                .setUsername(username)
                .setEmail(email)
                .setPassword(
                        passwordEncoder.encode(password)
                );
        user = userDao.save(user);
        assertTrue(passwordEncoder.matches(password, user.getPassword()));
    }
}
