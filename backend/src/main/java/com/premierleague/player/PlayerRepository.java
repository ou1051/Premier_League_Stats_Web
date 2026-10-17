package com.premierleague.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, PlayerId> {

    @Query("select max(p.collectedAt) from Player p")
    LocalDateTime findLatestCollectedAt(); // one row per player per run -> scope lookups to latest run

    List<Player> findByCollectedAt(LocalDateTime collectedAt);
    List<Player> findByCollectedAtAndWebNameContainingIgnoreCase(LocalDateTime collectedAt, String webName);
    List<Player> findByCollectedAtAndTeam(LocalDateTime collectedAt, String team);
    List<Player> findByCollectedAtAndPosition(LocalDateTime collectedAt, String position);
    List<Player> findByCollectedAtAndTeamAndPosition(LocalDateTime collectedAt, String team, String position);

    Optional<Player> findFirstByPlayerIdOrderByCollectedAtDesc(Integer playerId);
    boolean existsByPlayerId(Integer playerId);
    void deleteByPlayerId(Integer playerId);
}
