package com.example.prisoners.dilemma.repositories;

import com.example.prisoners.dilemma.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GamesRepo extends JpaRepository<Game, UUID> {

}
