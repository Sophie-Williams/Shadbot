package me.shadorc.shadbot.data.database;

import java.util.List;

import discord4j.core.object.util.Snowflake;
import me.shadorc.shadbot.Shadbot;

public class DBGuild {

	private final Long guild_id;

	public DBGuild(Snowflake guild_id) {
		this.guild_id = guild_id.asLong();
	}

	public Snowflake getGuildId() {
		return Snowflake.of(this.guild_id);
	}

	public List<DBMember> getMembers() {
		return Shadbot.getJdbi().withHandle(handle -> handle.createQuery("SELECT * FROM member WHERE guild_id = :guild_id")
				.bind("guild_id", this.getGuildId().asLong())
				.mapTo(DBMember.class)
				.list());
	}

	/*
	public List<Long> getAllowedTextChannels() {
		return Shadbot.getJdbi().withHandle(handle -> handle.createQuery("SELECT channel_id FROM allowed_text_channel WHERE guild_id = :guild_id")
				.bind("guild_id", this.getGuildId().asLong())
				.mapTo(Long.class)
				.list());
	}

	public List<Long> getAllowedVoiceChannels() {
		return Shadbot.getJdbi().withHandle(handle -> handle.createQuery("SELECT channel_id FROM allowed_voice_channel WHERE guild_id = :guild_id")
				.bind("guild_id", this.getGuildId().asLong())
				.mapTo(Long.class)
				.list());
	}

	public List<Long> getAllowedRoles() {
		return Shadbot.getJdbi().withHandle(handle -> handle.createQuery("SELECT role_id FROM allowed_role WHERE guild_id = :guild_id")
				.bind("guild_id", this.getGuildId().asLong())
				.mapTo(Long.class)
				.list());
	}

	public List<Snowflake> getAutoRoles() {
		return this.getListSetting(SettingEnum.AUTO_ROLES, Long.class)
				.stream()
				.map(Snowflake::of)
				.collect(Collectors.toList());
	}

	public List<String> getBlacklistedCmd() {
		return this.getListSetting(SettingEnum.BLACKLIST, String.class);
	}

	public Integer getDefaultVol() {
		return Integer.parseInt(Objects.toString(
				this.settings.get(SettingEnum.DEFAULT_VOLUME.toString()),
				Integer.toString(Config.DEFAULT_VOLUME)));
	}

	public Map<String, Long> getIamMessages() {
		return (Map<String, Long>) Optional.ofNullable(this.settings.get(SettingEnum.IAM_MESSAGES.toString()))
				.orElse(new HashMap<>());
	}

	public Optional<String> getJoinMessage() {
		return Optional.ofNullable((String) this.settings.get(SettingEnum.JOIN_MESSAGE.toString()));
	}

	public Optional<String> getLeaveMessage() {
		return Optional.ofNullable((String) this.settings.get(SettingEnum.LEAVE_MESSAGE.toString()));
	}

	public Optional<Snowflake> getMessageChannelId() {
		return Optional.ofNullable((Long) this.settings.get(SettingEnum.MESSAGE_CHANNEL_ID.toString()))
				.map(Snowflake::of);
	}

	public String getPrefix() {
		return Objects.toString(
				this.settings.get(SettingEnum.PREFIX.toString()),
				Config.DEFAULT_PREFIX);
	}

	private <T> List<T> getListSetting(SettingEnum setting, Class<T> listClass) {
		return Optional.ofNullable((List<?>) this.settings.get(setting.toString()))
				.orElse(new ArrayList<>())
				.stream()
				.map(listClass::cast)
				.collect(Collectors.toList());
	}

	public void setSetting(SettingEnum setting, Object value) {
		this.settings.put(setting.toString(), value);
	}

	public void removeSetting(SettingEnum setting) {
		this.settings.remove(setting.toString());
	}

	public void addMember(DBMember dbMember) {
		this.members.add(dbMember);
	}
	
	*/

}
