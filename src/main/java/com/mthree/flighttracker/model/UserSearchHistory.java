package com.mthree.flighttracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
public class UserSearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @ManyToOne
    @JoinColumn(name = "airline_id")
    private Airline airline;

    @Setter
    @ManyToOne
    @JoinColumn(name = "dep_airport_id")
    private Airport depAirport;

    @Setter
    @ManyToOne
    @JoinColumn(name = "arr_airport_id")
    private Airport arrAirport;

    @Setter
    @ManyToOne
    @JoinColumn(name = "sole_airport_id")
    private Airport soleAirport;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
