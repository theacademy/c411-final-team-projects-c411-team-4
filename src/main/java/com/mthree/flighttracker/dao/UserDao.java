package com.mthree.flighttracker.dao;

import com.mthree.flighttracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository <User, Integer>{

    User findByUsername(String username);
}