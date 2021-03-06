package me.shadorc.shadbot.api.gamestats.diablo.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import reactor.util.annotation.Nullable;

import java.util.List;
import java.util.Optional;

public class ProfileResponse {

    @Nullable
    @JsonProperty("code")
    private String code;
    @JsonProperty("battleTag")
    private String battleTag;
    @JsonProperty("paragonLevel")
    private int paragonLevel;
    @JsonProperty("paragonLevelHardcore")
    private int paragonLevelHardcore;
    @JsonProperty("paragonLevelSeason")
    private int paragonLevelSeason;
    @JsonProperty("paragonLevelSeasonHardcore")
    private int paragonLevelSeasonHardcore;
    @JsonProperty("guildName")
    private String guildName;
    @JsonProperty("heroes")
    private List<HeroId> heroeIds;

    public Optional<String> getCode() {
        return Optional.ofNullable(this.code);
    }

    public String getBattleTag() {
        return this.battleTag;
    }

    public int getParagonLevel() {
        return this.paragonLevel;
    }

    public int getParagonLevelHardcore() {
        return this.paragonLevelHardcore;
    }

    public int getParagonLevelSeason() {
        return this.paragonLevelSeason;
    }

    public int getParagonLevelSeasonHardcore() {
        return this.paragonLevelSeasonHardcore;
    }

    public String getGuildName() {
        return this.guildName;
    }

    public List<HeroId> getHeroeIds() {
        return this.heroeIds;
    }

}
