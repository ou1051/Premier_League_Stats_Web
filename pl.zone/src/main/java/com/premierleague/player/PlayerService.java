package com.premierleague.player;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }

    public Player getPlayerById(Integer id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found: " + id));
    }

    public List<Player> getPlayersByTeam(String team) {
        return playerRepository.findByTeam(team); // spring will automatically create SQL and send it to DB
    }

    public List<Player> getPlayersByName(String name) {
        return playerRepository.findByWebNameContainingIgnoreCase(name); // assuming user will search in webName
    }

    public List<Player> getPlayersByPosition(String position) {
        return playerRepository.findByPosition(position);
    }

    public List<Player> getPlayersByTeamAndPosition(String team, String position) {
        return playerRepository.findByTeamAndPosition(team, position);
    }

    public Player addPlayer(Player player) {
        if (playerRepository.existsById(player.getPlayerId())) {
            throw new IllegalStateException("Player already exists: " + player.getPlayerId());
        }
        return playerRepository.save(player);
    }
}