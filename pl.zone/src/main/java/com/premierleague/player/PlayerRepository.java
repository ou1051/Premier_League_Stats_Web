package com.premierleague.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    List<Player> findByWebNameContainingIgnoreCase(String webName);
    List<Player> findByTeam(String team);
    List<Player> findByPosition(String position);
    List<Player> findByTeamAndPosition(String team, String position);
}

