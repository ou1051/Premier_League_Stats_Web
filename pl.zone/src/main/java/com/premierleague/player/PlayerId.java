package com.premierleague.player;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class PlayerId implements Serializable { // this class is made since DB keys are madeout of season, playerId, collectedAt

    private String season;
    private Integer playerId;
    private LocalDateTime collectedAt;

    protected PlayerId() {}

    public PlayerId(String season, Integer playerId, LocalDateTime collectedAt) {
        this.season = season;
        this.playerId = playerId;
        this.collectedAt = collectedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerId other)) return false;
        return Objects.equals(season, other.season)
                && Objects.equals(playerId, other.playerId)
                && Objects.equals(collectedAt, other.collectedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(season, playerId, collectedAt);
    }
}
