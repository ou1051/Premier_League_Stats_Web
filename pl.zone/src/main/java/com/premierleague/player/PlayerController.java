package com.premierleague.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/api/players")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable Integer id) {
        return playerService.getPlayerById(id);
    }

    @GetMapping
    public List<Player> getAllPlayers(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String team,
                                   @RequestParam(required = false) String position) {

        if (team != null && position != null) {
            return playerService.getPlayersByTeamAndPosition(team, position);
        } else if (team != null) {
            return playerService.getPlayersByTeam(team);
        } else if (position != null) {
            return playerService.getPlayersByPosition(position);
        } else if (name != null) {
            return playerService.getPlayersByName(name);
        }
        return playerService.getPlayers();
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        Player createdPlayer = playerService.addPlayer(player);
        return new ResponseEntity<>(createdPlayer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Integer id, @RequestBody Player player) {
        Player updatedPlayer = playerService.updatePlayer(id, player);
        return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Integer id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

}
