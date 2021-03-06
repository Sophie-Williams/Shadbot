package me.shadorc.shadbot.command.utils;

import discord4j.core.object.Embed;
import discord4j.core.object.Embed.Field;
import discord4j.core.spec.EmbedCreateSpec;
import me.shadorc.shadbot.api.urbandictionary.UrbanDefinition;
import me.shadorc.shadbot.api.urbandictionary.UrbanDictionaryResponse;
import me.shadorc.shadbot.core.command.BaseCmd;
import me.shadorc.shadbot.core.command.CommandCategory;
import me.shadorc.shadbot.core.command.Context;
import me.shadorc.shadbot.object.Emoji;
import me.shadorc.shadbot.object.message.LoadingMessage;
import me.shadorc.shadbot.utils.NetUtils;
import me.shadorc.shadbot.utils.Utils;
import me.shadorc.shadbot.utils.embed.EmbedUtils;
import me.shadorc.shadbot.utils.embed.help.HelpBuilder;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Consumer;

public class UrbanCmd extends BaseCmd {

    public UrbanCmd() {
        super(CommandCategory.UTILS, List.of("urban"), "ud");
        this.setDefaultRateLimiter();
    }

    @Override
    public Mono<Void> execute(Context context) {
        final String arg = context.requireArg();

        final LoadingMessage loadingMsg = new LoadingMessage(context.getClient(), context.getChannelId());
        return Mono.fromCallable(() -> {
            final String url = String.format("https://api.urbandictionary.com/v0/define?term=%s", NetUtils.encode(arg));

            final UrbanDictionaryResponse urbanDictionary = Utils.MAPPER.readValue(NetUtils.getJSON(url), UrbanDictionaryResponse.class);

            if (urbanDictionary.getDefinitions().isEmpty()) {
                return loadingMsg.setContent(String.format(Emoji.MAGNIFYING_GLASS + " (**%s**) No urban definitions found for `%s`",
                        context.getUsername(), arg));
            }

            final UrbanDefinition urbanDefinition = urbanDictionary.getDefinitions().get(0);

            final String definition = StringUtils.abbreviate(urbanDefinition.getDefinition(), Embed.MAX_DESCRIPTION_LENGTH);
            final String example = StringUtils.abbreviate(urbanDefinition.getExample(), Field.MAX_VALUE_LENGTH);

            return loadingMsg.setEmbed(EmbedUtils.getDefaultEmbed()
                    .andThen(embed -> {
                        embed.setAuthor(String.format("Urban Dictionary: %s", urbanDefinition.getWord()), urbanDefinition.getPermalink(), context.getAvatarUrl())
                                .setThumbnail("http://www.packal.org/sites/default/files/public/styles/icon_large/public/workflow-files/florianurban/icon/icon.png")
                                .setDescription(definition);
                        if (!example.isBlank()) {
                            embed.addField("Example", example, false);
                        }
                    }));
        })
                .flatMap(LoadingMessage::send)
                .doOnTerminate(loadingMsg::stopTyping)
                .then();
    }

    @Override
    public Consumer<EmbedCreateSpec> getHelp(Context context) {
        return new HelpBuilder(this, context)
                .setDescription("Show Urban Dictionary definition for a search.")
                .addArg("search", false)
                .build();
    }

}
