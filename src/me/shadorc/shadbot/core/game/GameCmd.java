package me.shadorc.shadbot.core.game;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import discord4j.core.object.util.Snowflake;
import me.shadorc.shadbot.core.command.BaseCmd;
import me.shadorc.shadbot.core.command.CommandCategory;

public abstract class GameCmd<T extends GameManager> extends BaseCmd {

	private final Map<Snowflake, T> managers;

	public GameCmd(List<String> names, String alias) {
		super(CommandCategory.GAME, names, alias);
		this.setGameRateLimiter();
		this.managers = new ConcurrentHashMap<>();
	}

	public GameCmd(List<String> names) {
		this(names, null);
	}

	public Map<Snowflake, T> getManagers() {
		return this.managers;
	}
}