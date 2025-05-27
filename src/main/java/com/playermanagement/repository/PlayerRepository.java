package com.playermanagement.repository;

import com.playermanagement.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByLogin(String login);
    boolean existsByLogin(String login);
    boolean existsByScreenName(String screenName);
} 