package me.shadorc.shadbot.utils;

import java.util.Collections;
import java.util.List;

import discord4j.core.DiscordClient;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import me.shadorc.shadbot.Config;
import me.shadorc.shadbot.core.command.AbstractCommand;
import me.shadorc.shadbot.data.db.DatabaseManager;
import me.shadorc.shadbot.data.stats.VariousStatsManager;
import me.shadorc.shadbot.data.stats.VariousStatsManager.VariousEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BotUtils {

	public static Mono<Message> sendMessage(String content, Mono<MessageChannel> channel) {
		return BotUtils.sendMessage(content, null, channel);
	}

	public static Mono<Message> sendMessage(EmbedCreateSpec embed, Mono<MessageChannel> channel) {
		return BotUtils.sendMessage(null, embed, channel)
				.doOnSuccess(msg -> VariousStatsManager.log(VariousEnum.EMBEDS_SENT));
	}

	public static Mono<Message> sendMessage(String content, EmbedCreateSpec embed, Mono<MessageChannel> channelMono) {
		final MessageCreateSpec spec = new MessageCreateSpec();
		if(content != null) {
			spec.setContent(content);
		}
		if(embed != null) {
			spec.setEmbed(embed);
		}
		return channelMono.flatMap(channel -> channel.createMessage(spec))
				.doOnSuccess(message -> VariousStatsManager.log(VariousEnum.MESSAGES_SENT));
	}

	/**
	 * @param channel - the channel containing the messages to delete
	 * @param messages - the {@link List} of messages to delete
	 * @return The number of deleted messages
	 */
	public static Mono<Integer> bulkDelete(Mono<TextChannel> channel, List<Message> messages) {
		switch (messages.size()) {
			case 0:
				return Mono.just(messages.size());
			case 1:
				return messages.get(0).delete().thenReturn(messages.size());
			default:
				return channel
						.flatMap(channelItr -> channelItr.bulkDelete(Flux.fromIterable(messages)
								.map(Message::getId))
								.collectList()
								.map(messagesNotDeleted -> messages.size() - messagesNotDeleted.size()));
		}
	}

	public static Mono<Void> updatePresence(DiscordClient client) {
		return Mono.just(String.format("%shelp | %s", Config.DEFAULT_PREFIX, Utils.randValue(TextUtils.TIP_MESSAGES)))
				.flatMap(text -> client.updatePresence(Presence.online(Activity.playing(text))));
	}

	public static Flux<Member> getMembersFrom(Message message) {
		return DiscordUtils.getMembers(message.getGuild())
				.filter(member -> message.mentionsEveryone()
						|| message.getUserMentionIds().contains(member.getId())
						|| !Collections.disjoint(member.getRoleIds(), message.getRoleMentionIds()));
	}

	public static boolean hasAllowedRole(Snowflake guildId, List<Role> roles) {
		List<Snowflake> allowedRoles = DatabaseManager.getDBGuild(guildId).getAllowedRoles();
		// If the user is an administrator OR no permissions have been set OR the role is allowed
		return allowedRoles.isEmpty()
				|| roles.stream().anyMatch(role -> role.getPermissions().contains(Permission.ADMINISTRATOR))
				|| roles.stream().anyMatch(role -> allowedRoles.contains(role.getId()));
	}

	public static boolean isChannelAllowed(Snowflake guildId, Snowflake channelId) {
		List<Snowflake> allowedChannels = DatabaseManager.getDBGuild(guildId).getAllowedChannels();
		// If no permission has been set OR the channel is allowed
		return allowedChannels.isEmpty() || allowedChannels.contains(channelId);
	}

	public static boolean isCommandAllowed(Snowflake guildId, AbstractCommand cmd) {
		List<String> blacklistedCmd = DatabaseManager.getDBGuild(guildId).getBlacklistedCmd();
		return cmd.getNames().stream().noneMatch(blacklistedCmd::contains);
	}

}
