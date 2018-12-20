package me.shadorc.shadbot.data.database;

import discord4j.core.object.util.Snowflake;

public abstract class Setting<T> {
	
	private final long guildId;

	public Setting(Snowflake guildId) {
		this.guildId = guildId.asLong();
	}
	
	public abstract T get();
	
	public abstract void set(T value);
	
	public abstract void delete();

}
