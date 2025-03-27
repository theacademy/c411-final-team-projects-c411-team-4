package com.mthree.flighttracker.test.service;

import com.mthree.flighttracker.FlighttrackerApplication;
import com.mthree.flighttracker.controller.PasswordEncoder;
import com.mthree.flighttracker.dao.UserDao;
import com.mthree.flighttracker.model.Airline;
import com.mthree.flighttracker.model.User;
import com.mthree.flighttracker.model.UserSearchHistory;
import com.mthree.flighttracker.service.UserHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest(classes = FlighttrackerApplication.class)
@Sql(scripts = {"/test_clean_tables.sql", "/test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserHistoryServiceTest {
    @Autowired
    UserDao userDao;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserHistoryService historyService;

    private static User user;

    @BeforeEach
    public void setupClass() {
        if(userDao.findByUsername("kyannelli") == null) {
            user = new User()
                    .setUsername("kyannelli")
                    .setEmail("kyle.yannelli@mthree.com")
                    .setPassword(passwordEncoder.encode("badPasswordTwo"));
            user = userDao.save(user);
        }
    }

    @Test
    public void testHistoryAdd() {
        addToHistory("DL");
    }

    @Test
    public void testHistoryGet() {
        addToHistory("DL");
        addToHistory("B6");
        addToHistory("AA");
        addToHistory("WN");
        final List<UserSearchHistory> histories = historyService.getHistoryByUsername(user.getUsername());
        assertEquals(histories.size(), 4);
    }

    private void addToHistory(String airlineCode) {
        UserSearchHistory history = new UserSearchHistory();
        history.setUser(user);
        history.setAirline(new Airline().setCode(airlineCode));
        historyService.addHistoryToUser(history);
    }
}
