package com.example.prisoners.dilemma.repositories;

import com.example.prisoners.dilemma.entities.PlayerWealthChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerWealthChangesRepo extends JpaRepository<PlayerWealthChange, PlayerWealthChange.PK> {
}
