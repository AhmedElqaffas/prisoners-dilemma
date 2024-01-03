package com.example.prisoners.dilemma.repositories;

import com.example.prisoners.dilemma.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlayerRepo extends JpaRepository<Player, UUID> {

}
