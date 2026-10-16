package com.premierleague.player;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="player_stats")
public class Player {

    @Id
    private Integer playerId;

    private String firstName;
    private String secondName;
    private String webName;
    private String team;
    private String position;
    private LocalDate birthDate;

    private Integer minutes;
    private Integer starts;
    private Integer goalsScored;
    private Integer assists;
    private Integer penaltiesMissed;
    private Integer cleanSheets;
    private Integer goalsConceded;
    private Integer ownGoals;
    private Integer saves;
    private Integer penaltiesSaved;
    private Integer tackles;
    private Integer recoveries;
    private Integer clearancesBlocksInterceptions;
    private Integer yellowCards;
    private Integer redCards;

    private Double expectedGoals;
    private Double expectedAssists;
    private Double expectedGoalInvolvements;
    private Double expectedGoalsConceded;

    private String season;             // "2026-27"
    private LocalDateTime collectedAt;

    protected Player() {}

    public Integer getPlayerId() {
        return playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getWebName() {
        return webName;
    }

    public String getTeam() {
        return team;
    }

    public String getPosition() {
        return position;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public Integer getStarts() {
        return starts;
    }

    public Integer getGoalsScored() {
        return goalsScored;
    }

    public Integer getAssists() {
        return assists;
    }

    public Integer getPenaltiesMissed() {
        return penaltiesMissed;
    }

    public Integer getCleanSheets() {
        return cleanSheets;
    }

    public Integer getGoalsConceded() {
        return goalsConceded;
    }

    public Integer getOwnGoals() {
        return ownGoals;
    }

    public Integer getSaves() {
        return saves;
    }

    public Integer getPenaltiesSaved() {
        return penaltiesSaved;
    }

    public Integer getTackles() {
        return tackles;
    }

    public Integer getRecoveries() {
        return recoveries;
    }

    public Integer getClearancesBlocksInterceptions() {
        return clearancesBlocksInterceptions;
    }

    public Integer getYellowCards() {
        return yellowCards;
    }

    public Integer getRedCards() {
        return redCards;
    }

    public Double getExpectedGoals() {
        return expectedGoals;
    }

    public Double getExpectedAssists() {
        return expectedAssists;
    }

    public Double getExpectedGoalInvolvements() {
        return expectedGoalInvolvements;
    }

    public Double getExpectedGoalsConceded() {
        return expectedGoalsConceded;
    }

    public String getSeason() {
        return season;
    }

    public LocalDateTime getCollectedAt() {
        return collectedAt;
    }
}
