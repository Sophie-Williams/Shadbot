package me.shadorc.shadbot.command.gamestats;

import discord4j.core.spec.EmbedCreateSpec;
import me.shadorc.shadbot.api.TokenResponse;
import me.shadorc.shadbot.api.gamestats.diablo.hero.HeroResponse;
import me.shadorc.shadbot.api.gamestats.diablo.profile.HeroId;
import me.shadorc.shadbot.api.gamestats.diablo.profile.ProfileResponse;
import me.shadorc.shadbot.core.command.BaseCmd;
import me.shadorc.shadbot.core.command.CommandCategory;
import me.shadorc.shadbot.core.command.Context;
import me.shadorc.shadbot.data.credential.Credential;
import me.shadorc.shadbot.data.credential.Credentials;
import me.shadorc.shadbot.exception.CommandException;
import me.shadorc.shadbot.object.Emoji;
import me.shadorc.shadbot.object.message.LoadingMessage;
import me.shadorc.shadbot.utils.FormatUtils;
import me.shadorc.shadbot.utils.NetUtils;
import me.shadorc.shadbot.utils.TimeUtils;
import me.shadorc.shadbot.utils.Utils;
import me.shadorc.shadbot.utils.embed.EmbedUtils;
import me.shadorc.shadbot.utils.embed.help.HelpBuilder;
import me.shadorc.shadbot.utils.embed.log.LogUtils;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class DiabloCmd extends BaseCmd {

    private enum Region {
        EU, US, TW, KR;
    }

    private final AtomicLong lastTokenGeneration;
    private TokenResponse token;

    public DiabloCmd() {
        super(CommandCategory.GAMESTATS, List.of("diablo"), "d3");
        this.setDefaultRateLimiter();

        this.lastTokenGeneration = new AtomicLong(0);
        this.token = null;
    }

    @Override
    public Mono<Void> execute(Context context) {
        final List<String> args = context.requireArgs(2);

        final Region region = Utils.parseEnum(Region.class, args.get(0),
                new CommandException(String.format("`%s` is not a valid Region. %s",
                        args.get(0), FormatUtils.options(Region.class))));

        final String battletag = args.get(1).replaceAll("#", "-");

        final LoadingMessage loadingMsg = new LoadingMessage(context.getClient(), context.getChannelId());
        return Mono.fromCallable(() -> {
            if (this.isTokenExpired()) {
                this.generateAccessToken();
            }

            final String url = String.format("https://%s.api.blizzard.com/d3/profile/%s/?access_token=%s",
                    region.toString().toLowerCase(), NetUtils.encode(battletag), this.token.getAccessToken());

            final ProfileResponse profile = Utils.MAPPER.readValue(NetUtils.getJSON(url), ProfileResponse.class);

            if (profile.getCode().map("NOTFOUND"::equals).orElse(false)) {
                return loadingMsg.setContent(String.format(
                        Emoji.MAGNIFYING_GLASS + " (**%s**) This user doesn't play Diablo 3 or doesn't exist.",
                        context.getUsername()));
            }

            final List<HeroResponse> heroResponses = new ArrayList<>();
            for (final HeroId heroId : profile.getHeroeIds()) {
                final String heroUrl = String.format("https://%s.api.blizzard.com/d3/profile/%s/hero/%d?access_token=%s",
                        region, NetUtils.encode(battletag), heroId.getId(), this.token.getAccessToken());

                final HeroResponse hero = Utils.MAPPER.readValue(NetUtils.getJSON(heroUrl), HeroResponse.class);
                if (hero.getCode().isEmpty()) {
                    heroResponses.add(hero);
                }
            }

            // Sort heroes by ascending damage
            heroResponses.sort(Comparator.comparingDouble(hero -> hero.getStats().getDamage()));
            Collections.reverse(heroResponses);

            return loadingMsg.setEmbed(EmbedUtils.getDefaultEmbed()
                    .andThen(embed -> embed.setAuthor("Diablo 3 Stats", null, context.getAvatarUrl())
                            .setThumbnail("http://osx.wdfiles.com/local--files/icon:d3/D3.png")
                            .setDescription(String.format("Stats for **%s** (Guild: **%s**)"
                                            + "%n%nParangon level: **%s** (*Normal*) / **%s** (*Hardcore*)"
                                            + "%nSeason Parangon level: **%s** (*Normal*) / **%s** (*Hardcore*)",
                                    profile.getBattleTag(), profile.getGuildName(),
                                    profile.getParagonLevel(), profile.getParagonLevelSeasonHardcore(),
                                    profile.getParagonLevelSeason(), profile.getParagonLevelSeasonHardcore()))
                            .addField("Heroes", FormatUtils.format(heroResponses,
                                    hero -> String.format("**%s** (*%s*)", hero.getName(), hero.getClassName()), "\n"), true)
                            .addField("Damage", FormatUtils.format(heroResponses,
                                    hero -> String.format("%s DPS", FormatUtils.number(hero.getStats().getDamage())), "\n"), true)));
        })
                .flatMap(LoadingMessage::send)
                .doOnTerminate(loadingMsg::stopTyping)
                .then();
    }

    private boolean isTokenExpired() {
        return this.token == null
                || TimeUtils.getMillisUntil(this.lastTokenGeneration.get()) >= TimeUnit.SECONDS.toMillis(this.token.getExpiresIn());
    }

    private void generateAccessToken() throws IOException {
        final String url = String.format("https://us.battle.net/oauth/token?grant_type=client_credentials&client_id=%s&client_secret=%s",
                Credentials.get(Credential.BLIZZARD_CLIENT_ID),
                Credentials.get(Credential.BLIZZARD_CLIENT_SECRET));
        this.token = Utils.MAPPER.readValue(NetUtils.getJSON(url), TokenResponse.class);
        this.lastTokenGeneration.set(System.currentTimeMillis());
        LogUtils.info("Blizzard token generated: %s", this.token.getAccessToken());
    }

    @Override
    public Consumer<EmbedCreateSpec> getHelp(Context context) {
        return new HelpBuilder(this, context)
                .setDescription("Show player's stats for Diablo 3.")
                .addArg("region", String.format("user's region (%s)", FormatUtils.format(Region.class, ", ")), false)
                .addArg("battletag#0000", false)
                .setExample(String.format("`%s%s eu Shadorc#2503`", context.getPrefix(), this.getName()))
                .build();
    }

}
