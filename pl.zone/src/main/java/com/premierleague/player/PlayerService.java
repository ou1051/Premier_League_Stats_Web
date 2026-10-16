package com.premierleague.player;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service// should be managed by spring container
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    //----------------in the future---------------------
    //ranking: getTop10ByOrderGoalsScored
    //findTop10----

    private LocalDateTime latestCollectedAt() {
        return playerRepository.findLatestCollectedAt();
    }

    public List<Player> getPlayers() {
        return playerRepository.findByCollectedAt(latestCollectedAt()); // latest run only
    }

    public Player getPlayerById(Integer id) {
        return playerRepository.findFirstByPlayerIdOrderByCollectedAtDesc(id)
                .orElseThrow(() -> new RuntimeException("Player not found: " + id));
    }

    public List<Player> getPlayersByTeam(String team) {
        return playerRepository.findByCollectedAtAndTeam(latestCollectedAt(), team);
    }

    public List<Player> getPlayersByName(String name) {
        return playerRepository.findByCollectedAtAndWebNameContainingIgnoreCase(latestCollectedAt(), name); // latest run only, assuming user will search in webName
    }

    public List<Player> getPlayersByPosition(String position) {
        return playerRepository.findByCollectedAtAndPosition(latestCollectedAt(), position);
    }

    public List<Player> getPlayersByTeamAndPosition(String team, String position) {
        return playerRepository.findByCollectedAtAndTeamAndPosition(latestCollectedAt(), team, position);
    }

    public Player addPlayer(Player player) {
        PlayerId key = new PlayerId(player.getSeason(), player.getPlayerId(), player.getCollectedAt());
        if (playerRepository.existsById(key)) {
            throw new IllegalStateException("Player already exists: " + player.getPlayerId());
        }
        return playerRepository.save(player);
    }

    public Player updatePlayer(Integer id, Player updatedPlayer) {
        Player player = playerRepository.findFirstByPlayerIdOrderByCollectedAtDesc(id)
                .orElseThrow(() -> new IllegalStateException("Player not found: " + id));


        player.setFirstName(updatedPlayer.getFirstName());
        player.setSecondName(updatedPlayer.getSecondName());
        player.setWebName(updatedPlayer.getWebName());
        player.setTeam(updatedPlayer.getTeam());
        player.setPosition(updatedPlayer.getPosition());

        player.setMinutes(updatedPlayer.getMinutes());
        player.setGoalsScored(updatedPlayer.getGoalsScored());
        player.setAssists(updatedPlayer.getAssists());


        return playerRepository.save(player);
    }

    @Transactional // maintain data integrity
    public void deletePlayer(Integer playerId) {
        if (!playerRepository.existsByPlayerId(playerId)) { // existsById needs full composite key
            throw new IllegalStateException("Player not found: " + playerId);
        }
        playerRepository.deleteByPlayerId(playerId); // deletes all runs of this player
    }
}