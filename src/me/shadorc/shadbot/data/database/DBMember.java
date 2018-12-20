package me.shadorc.shadbot.data.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import discord4j.core.object.util.Snowflake;
import me.shadorc.shadbot.Config;
import me.shadorc.shadbot.Shadbot;
import me.shadorc.shadbot.utils.NumberUtils;

public class DBMember implements RowMapper<DBMember>{

	private final long guildId;
	private final long memberId;
	
	public DBMember(long guildId, long memberId) {
		this.guildId = guildId;
		this.memberId = memberId;
	}
	
	@Override
    public DBMember map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new DBMember(rs.getLong("guild_id"), rs.getLong("member_id"));
    }

	public Snowflake getGuildId() {
		return Snowflake.of(this.guildId);
	}

	public Snowflake getMemberId() {
		return Snowflake.of(this.memberId);
	}

	public int getCoins() {
		return Shadbot.getJdbi().withHandle(handle -> handle.createQuery("SELECT coins FROM member WHERE guild_id = :guild_id AND member_id = :member_id")
				.bind(":guild_id", this.getGuildId().asLong())
				.bind(":member_id", this.getMemberId().asLong())
				.mapTo(Integer.class)
				.findOnly());
	}

	public void addCoins(int gains) {
		final int coins  = NumberUtils.between(this.getCoins() + gains, 0, Config.MAX_COINS);
		Shadbot.getJdbi().withHandle(handle -> handle.createUpdate("UPDATE FROM member SET coins = :coins WHERE guild_id = :guild_id AND member_id = :member_id")
				.bind(":coins", coins)
				.bind(":guild_id", this.getGuildId().asLong())
				.bind(":member_id", this.getMemberId().asLong())
				.execute());
	}

	public void resetCoins() {
		Shadbot.getJdbi().withHandle(handle -> handle.createUpdate("UPDATE FROM member SET coins = 0 WHERE guild_id = :guild_id AND member_id = :member_id")
				.bind(":guild_id", this.getGuildId().asLong())
				.bind(":member_id", this.getMemberId().asLong())
				.execute());
	}

}
