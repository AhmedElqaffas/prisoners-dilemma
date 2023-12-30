package com.example.prisoners.dilemma.repositories;

import com.example.prisoners.dilemma.entities.PrisonerDilemmaAuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAuthRepo extends JpaRepository<PrisonerDilemmaAuthUser, UUID> {
    Optional<PrisonerDilemmaAuthUser> findByEmail(String email);
}
