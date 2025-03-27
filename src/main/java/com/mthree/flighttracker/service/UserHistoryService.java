package com.mthree.flighttracker.service;

import com.mthree.flighttracker.model.UserSearchHistory;

import java.util.List;

public interface UserHistoryService {
    /**
     * @param username to search history for
     * @return the user's history
     */
    public List<UserSearchHistory> getHistoryByUsername(String username);

    /**
     * @param history for the user
     */
    public void addHistoryToUser(UserSearchHistory history);
}
