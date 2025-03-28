package com.mthree.flighttracker.dao;

import com.mthree.flighttracker.model.UserSearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSearchHistoryDao extends JpaRepository<UserSearchHistory, Integer> {
    public List<UserSearchHistory> findByUserUsername(String username);
}
