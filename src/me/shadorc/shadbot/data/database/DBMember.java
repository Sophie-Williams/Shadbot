package me.shadorc.shadbot.data.database;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import discord4j.core.object.util.Snowflake;
import me.shadorc.shadbot.Config;
import me.shadorc.shadbot.utils.NumberUtils;

import java.util.concurrent.atomic.AtomicInteger;

@JsonAutoDetect(getterVisibility = Visibility.NONE)
public class DBMember {

    private final long guildId;
    @JsonProperty("id")
    private final long memberId;
    @JsonProperty("coins")
    private final AtomicInteger coins;

    public DBMember(Snowflake guildId, Snowflake id) {
        this.guildId = guildId.asLong();
        this.memberId = id.asLong();
        this.coins = new AtomicInteger(0);
    }

    public DBMember() {
        this(Snowflake.of(0L), Snowflake.of(0L));
    }

    public Snowflake getGuildId() {
        return Snowflake.of(this.guildId);
    }

    public Snowflake getId() {
        return Snowflake.of(this.memberId);
    }

    public int getCoins() {
        return this.coins.get();
    }

    public void addCoins(int gains) {
        this.coins.set(NumberUtils.between(this.getCoins() + gains, 0, Config.MAX_COINS));
    }

    public void resetCoins() {
        this.coins.set(0);
    }

    @Override
    public String toString() {
        return String.format("DBMember [guildId=%s, memberId=%s, coins=%s]", this.guildId, this.memberId, this.coins);
    }

}
