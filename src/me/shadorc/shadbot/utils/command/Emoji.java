package me.shadorc.shadbot.utils.command;

public enum Emoji {

	CHECK_MARK("white_check_mark"),
	WARNING("warning"),
	ACCESS_DENIED("no_entry_sign"),
	RED_CROSS("x"),

	GREY_EXCLAMATION("grey_exclamation"),
	RED_EXCLAMATION("exclamation"),
	RED_FLAG("triangular_flag_on_post"),

	INFO("information_source"),
	MAGNIFYING_GLASS("mag"),
	STOPWATCH("stopwatch"),
	GEAR("gear"),

	MONEY_WINGS("money_with_wings"),
	HOURGLASS("hourglass_flowing_sand"),
	PURSE("purse"),
	MONEY_BAG("moneybag"),
	BANK("bank"),
	DICE("game_die"),
	TICKET("tickets"),

	SPADES("spades"),
	CLUBS("clubs"),
	HEARTS("hearts"),
	DIAMONDS("diamonds"),

	THERMOMETER("thermometer"),
	BEACH("beach_umbrella"),
	CLOUD("cloud"),
	WIND("wind_blowing_face"),
	RAIN("cloud_rain"),
	DROPLET("droplet"),
	MAP("map"),

	MUSICAL_NOTE("musical_note"),
	PLAY("arrow_forward"),
	PAUSE("pause_button"),
	REPEAT("repeat"),
	SOUND("sound"),
	MUTE("mute"),

	SLEEPING("sleeping_accommodation"),
	THUMBSDOWN("thumbsdown"),
	SPEECH("speech_balloon"),
	KEYBOARD("keyboard"),
	CLAP("clap"),

	SCISSORS("scissors"),
	GEM("gem"),
	LEAF("leaves");

	private final String discordNotation;

	Emoji(String discordNotation) {
		this.discordNotation = discordNotation;
	}

	@Override
	public String toString() {
		return String.format(":%s:", discordNotation);
	}
}
