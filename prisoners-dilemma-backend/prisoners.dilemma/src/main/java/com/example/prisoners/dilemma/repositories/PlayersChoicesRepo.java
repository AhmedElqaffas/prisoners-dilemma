package com.example.prisoners.dilemma.repositories;

import com.example.prisoners.dilemma.entities.PlayerChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayersChoicesRepo extends JpaRepository<PlayerChoice, PlayerChoice.GameResultId> {
}
