package com.mthree.flighttracker.service;

import com.mthree.flighttracker.dao.AirlineDao;
import com.mthree.flighttracker.dao.AirportDao;
import com.mthree.flighttracker.dao.UserDao;
import com.mthree.flighttracker.dao.UserSearchHistoryDao;
import com.mthree.flighttracker.model.UserSearchHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserHistoryServiceImpl implements UserHistoryService {
    @Autowired
    UserSearchHistoryDao historyDao;
    @Autowired
    UserDao userDao;
    @Autowired
    AirportDao airportDao;
    @Autowired
    AirlineDao airlineDao;

    @Override
    public List<UserSearchHistory> getHistoryByUsername(String username) {
        return historyDao.findByUserUsername(username);
    }

    @Override
    public void addHistoryToUser(UserSearchHistory history) {
        if(history.getUser() != null && history.getUser().getId() == null) {
            history.setUser(userDao.findByUsername(history.getUser().getUsername()));
        }
        if(history.getAirline() != null && history.getAirline().getId() == 0) {
            history.setAirline(airlineDao.getAirlineByName(history.getAirline().getName()));
        }
        if(history.getArrAirport() != null && history.getArrAirport().getId() == 0) {
            history.setArrAirport(airportDao.getAirportByCode(history.getArrAirport().getCode()));
        }
        if(history.getDepAirport() != null && history.getDepAirport().getId() == 0) {
            history.setDepAirport(airportDao.getAirportByCode(history.getDepAirport().getCode()));
        }
        historyDao.save(history);
    }
}
