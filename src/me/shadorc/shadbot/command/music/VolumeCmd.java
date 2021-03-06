package me.shadorc.shadbot.command.music;

import discord4j.core.spec.EmbedCreateSpec;
import me.shadorc.shadbot.core.command.BaseCmd;
import me.shadorc.shadbot.core.command.CommandCategory;
import me.shadorc.shadbot.core.command.Context;
import me.shadorc.shadbot.exception.CommandException;
import me.shadorc.shadbot.music.GuildMusic;
import me.shadorc.shadbot.music.TrackScheduler;
import me.shadorc.shadbot.object.Emoji;
import me.shadorc.shadbot.utils.DiscordUtils;
import me.shadorc.shadbot.utils.NumberUtils;
import me.shadorc.shadbot.utils.embed.help.HelpBuilder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Consumer;

public class VolumeCmd extends BaseCmd {

    public VolumeCmd() {
        super(CommandCategory.MUSIC, List.of("volume"), "vol");
        this.setDefaultRateLimiter();
    }

    @Override
    public Mono<Void> execute(Context context) {
        final GuildMusic guildMusic = context.requireGuildMusic();

        return DiscordUtils.requireSameVoiceChannel(context)
                .flatMap(voiceChannelId -> {
                    final TrackScheduler scheduler = guildMusic.getTrackScheduler();
                    if (context.getArg().isEmpty()) {
                        return context.getChannel()
                                .flatMap(channel -> DiscordUtils.sendMessage(String.format(Emoji.SOUND + " (**%s**) Current volume level: **%d%%**",
                                        context.getUsername(), scheduler.getAudioPlayer().getVolume()), channel));
                    }

                    final String arg = context.getArg().get();
                    final Integer volume = NumberUtils.asPositiveInt(arg);
                    if (volume == null) {
                        return Mono.error(new CommandException(String.format("`%s` is not a valid volume.", arg)));
                    }

                    scheduler.setVolume(volume);
                    return context.getChannel()
                            .flatMap(channel -> DiscordUtils.sendMessage(String.format(Emoji.SOUND + " Volume level set to **%s%%** by **%s**.",
                                    scheduler.getAudioPlayer().getVolume(), context.getUsername()),
                                    channel));
                })
                .then();
    }

    @Override
    public Consumer<EmbedCreateSpec> getHelp(Context context) {
        return new HelpBuilder(this, context)
                .setDescription("Show or change current volume level.")
                .addArg("volume", "must be between 0 and 100", true)
                .build();
    }
}
