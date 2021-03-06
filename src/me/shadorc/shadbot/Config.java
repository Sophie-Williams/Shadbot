package me.shadorc.shadbot;

import discord4j.core.object.util.Snowflake;
import me.shadorc.shadbot.utils.ExitCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    private static final Properties PROPERTIES = Config.getProperties();

    public static final String VERSION = PROPERTIES.getProperty("version");
    public static final boolean IS_SNAPSHOT = VERSION.endsWith("SNAPSHOT");
    private static final String GITHUB_URL = PROPERTIES.getProperty("url.github");
    public static final String PATREON_URL = PROPERTIES.getProperty("url.patreon");
    public static final String SUPPORT_SERVER_URL = PROPERTIES.getProperty("url.support.server");
    public static final String USER_AGENT = String.format("Shadbot/%s/D4J-DiscordBot (%s)", VERSION, GITHUB_URL);

    public static final String DEFAULT_PREFIX = PROPERTIES.getProperty("default.prefix");
    public static final int DEFAULT_VOLUME = Integer.parseInt(PROPERTIES.getProperty("default.volume"));
    public static final int DEFAULT_PLAYLIST_SIZE = Integer.parseInt(PROPERTIES.getProperty("default.playlist.size"));
    public static final int DEFAULT_TIMEOUT = Integer.parseInt(PROPERTIES.getProperty("default.timeout"));
    public static final String DEFAULT_COMMAND_DELIMITER = PROPERTIES.getProperty("default.command.delimiter");

    public static final int MAX_COINS = Integer.parseInt(PROPERTIES.getProperty("max.coins"));

    public static final int MUSIC_SEARCHES = Integer.parseInt(PROPERTIES.getProperty("music.searches"));
    public static final int MUSIC_CHOICE_DURATION = Integer.parseInt(PROPERTIES.getProperty("music.choice.duration"));

    public static final Snowflake LOGS_CHANNEL_ID = Snowflake.of(PROPERTIES.getProperty("id.channel.log"));
    public static final Color BOT_COLOR = Color.decode(PROPERTIES.getProperty("color"));

    private static Properties getProperties() {
        final Properties properties = new Properties();
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("project.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (final IOException err) {
            LOGGER.error("An error occurred while loading configuration file. Exiting.", err);
            System.exit(ExitCode.FATAL_ERROR.value());
        }
        return properties;
    }

}
