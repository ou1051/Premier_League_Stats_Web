package com.premierleague.player;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="player_stats")
@IdClass(PlayerId.class) // DB PK is (season, player_id, collected_at); playerId alone isn't unique
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

    @Id // part of composite PK
    private String season;             // "2026-27"
    @Id // part of composite PK (one row per collection run)
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



    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setWebName(String webName) {
        this.webName = webName;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public void setStarts(Integer starts) {
        this.starts = starts;
    }

    public void setGoalsScored(Integer goalsScored) {
        this.goalsScored = goalsScored;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    public void setPenaltiesMissed(Integer penaltiesMissed) {
        this.penaltiesMissed = penaltiesMissed;
    }

    public void setCleanSheets(Integer cleanSheets) {
        this.cleanSheets = cleanSheets;
    }

    public void setGoalsConceded(Integer goalsConceded) {
        this.goalsConceded = goalsConceded;
    }

    public void setOwnGoals(Integer ownGoals) {
        this.ownGoals = ownGoals;
    }

    public void setSaves(Integer saves) {
        this.saves = saves;
    }

    public void setPenaltiesSaved(Integer penaltiesSaved) {
        this.penaltiesSaved = penaltiesSaved;
    }

    public void setTackles(Integer tackles) {
        this.tackles = tackles;
    }

    public void setRecoveries(Integer recoveries) {
        this.recoveries = recoveries;
    }

    public void setClearancesBlocksInterceptions(Integer clearancesBlocksInterceptions) {
        this.clearancesBlocksInterceptions = clearancesBlocksInterceptions;
    }

    public void setYellowCards(Integer yellowCards) {
        this.yellowCards = yellowCards;
    }

    public void setRedCards(Integer redCards) {
        this.redCards = redCards;
    }

    public void setExpectedGoals(Double expectedGoals) {
        this.expectedGoals = expectedGoals;
    }

    public void setExpectedAssists(Double expectedAssists) {
        this.expectedAssists = expectedAssists;
    }

    public void setExpectedGoalInvolvements(Double expectedGoalInvolvements) {
        this.expectedGoalInvolvements = expectedGoalInvolvements;
    }

    public void setExpectedGoalsConceded(Double expectedGoalsConceded) {
        this.expectedGoalsConceded = expectedGoalsConceded;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public void setCollectedAt(LocalDateTime collectedAt) {
        this.collectedAt = collectedAt;
    }
}

