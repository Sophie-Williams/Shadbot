package me.shadorc.shadbot.core.command;

import java.util.LinkedHashMap;
import java.util.Map;

import me.shadorc.shadbot.command.admin.IamCmd;
import me.shadorc.shadbot.command.admin.ManageCoinsCmd;
import me.shadorc.shadbot.command.admin.PruneCmd;
import me.shadorc.shadbot.command.admin.SettingsCmd;
import me.shadorc.shadbot.command.admin.member.BanCmd;
import me.shadorc.shadbot.command.admin.member.KickCmd;
import me.shadorc.shadbot.command.admin.member.SoftBanCmd;
import me.shadorc.shadbot.command.currency.CoinsCmd;
import me.shadorc.shadbot.command.currency.LeaderboardCmd;
import me.shadorc.shadbot.command.currency.TransferCoinsCmd;
import me.shadorc.shadbot.command.french.DtcCmd;
import me.shadorc.shadbot.command.french.JokeCmd;
import me.shadorc.shadbot.command.fun.ChatCmd;
import me.shadorc.shadbot.command.fun.LeetCmd;
import me.shadorc.shadbot.command.fun.ThisDayCmd;
import me.shadorc.shadbot.command.game.LotteryCmd;
import me.shadorc.shadbot.command.game.RussianRouletteCmd;
import me.shadorc.shadbot.command.game.blackjack.BlackjackCmd;
import me.shadorc.shadbot.command.game.dice.DiceCmd;
import me.shadorc.shadbot.command.game.hangman.HangmanCmd;
import me.shadorc.shadbot.command.game.roulette.RouletteCmd;
import me.shadorc.shadbot.command.game.rps.RpsCmd;
import me.shadorc.shadbot.command.game.slotmachine.SlotMachineCmd;
import me.shadorc.shadbot.command.game.trivia.TriviaCmd;
import me.shadorc.shadbot.command.gamestats.CounterStrikeCmd;
import me.shadorc.shadbot.command.gamestats.DiabloCmd;
import me.shadorc.shadbot.command.gamestats.FortniteCmd;
import me.shadorc.shadbot.command.gamestats.OverwatchCmd;
import me.shadorc.shadbot.command.hidden.ActivateRelicCmd;
import me.shadorc.shadbot.command.hidden.BaguetteCmd;
import me.shadorc.shadbot.command.hidden.HelpCmd;
import me.shadorc.shadbot.command.hidden.RelicStatusCmd;
import me.shadorc.shadbot.command.image.GifCmd;
import me.shadorc.shadbot.command.image.ImageCmd;
import me.shadorc.shadbot.command.image.Rule34Cmd;
import me.shadorc.shadbot.command.image.SuicideGirlsCmd;
import me.shadorc.shadbot.command.image.WallpaperCmd;
import me.shadorc.shadbot.command.info.InfoCmd;
import me.shadorc.shadbot.command.info.PingCmd;
import me.shadorc.shadbot.command.info.RolelistCmd;
import me.shadorc.shadbot.command.info.ServerInfoCmd;
import me.shadorc.shadbot.command.info.UserInfoCmd;
import me.shadorc.shadbot.command.music.BackwardCmd;
import me.shadorc.shadbot.command.music.BoostBassCmd;
import me.shadorc.shadbot.command.music.ClearCmd;
import me.shadorc.shadbot.command.music.ForwardCmd;
import me.shadorc.shadbot.command.music.NameCmd;
import me.shadorc.shadbot.command.music.PauseCmd;
import me.shadorc.shadbot.command.music.PlayCmd;
import me.shadorc.shadbot.command.music.PlaylistCmd;
import me.shadorc.shadbot.command.music.RepeatCmd;
import me.shadorc.shadbot.command.music.ShuffleCmd;
import me.shadorc.shadbot.command.music.SkipCmd;
import me.shadorc.shadbot.command.music.StopCmd;
import me.shadorc.shadbot.command.music.VolumeCmd;
import me.shadorc.shadbot.command.owner.CleanDatabaseCmd;
import me.shadorc.shadbot.command.owner.DatabaseCmd;
import me.shadorc.shadbot.command.owner.GenerateRelicCmd;
import me.shadorc.shadbot.command.owner.LeaveCmd;
import me.shadorc.shadbot.command.owner.LoggerCmd;
import me.shadorc.shadbot.command.owner.RestartCmd;
import me.shadorc.shadbot.command.owner.SendMessageCmd;
import me.shadorc.shadbot.command.owner.ShutdownCmd;
import me.shadorc.shadbot.command.owner.StatsCmd;
import me.shadorc.shadbot.command.utils.CalcCmd;
import me.shadorc.shadbot.command.utils.LyricsCmd;
import me.shadorc.shadbot.command.utils.TranslateCmd;
import me.shadorc.shadbot.command.utils.UrbanCmd;
import me.shadorc.shadbot.command.utils.WeatherCmd;
import me.shadorc.shadbot.command.utils.WikiCmd;
import me.shadorc.shadbot.command.utils.poll.PollCmd;
import me.shadorc.shadbot.utils.embed.log.LogUtils;

public class CommandInitializer {

	private final static Map<String, BaseCmd> COMMANDS_MAP = new LinkedHashMap<>();

	public static void initialize() {
		CommandInitializer.add(
				// Utility Commands
				new WeatherCmd(), new CalcCmd(), new TranslateCmd(), new WikiCmd(), new PollCmd(),
				new UrbanCmd(), new LyricsCmd(),
				// Fun Commands
				new ChatCmd(), new ThisDayCmd(), new LeetCmd(),
				// Image Commands
				new GifCmd(), new ImageCmd(), new WallpaperCmd(), new SuicideGirlsCmd(),
				new Rule34Cmd(),
				// Game Commands
				new RpsCmd(), new HangmanCmd(), new TriviaCmd(), new RussianRouletteCmd(),
				new SlotMachineCmd(), new RouletteCmd(), new BlackjackCmd(), new DiceCmd(),
				new LotteryCmd(),
				// Currency Commands
				new CoinsCmd(), new LeaderboardCmd(), new TransferCoinsCmd(),
				// Music Commands
				new PlayCmd(), new PauseCmd(), new StopCmd(), new SkipCmd(), new RepeatCmd(),
				new BackwardCmd(), new ForwardCmd(), new VolumeCmd(), new NameCmd(),
				new PlaylistCmd(), new ShuffleCmd(), new ClearCmd(), new BoostBassCmd(),
				// Game Stats Commands
				new FortniteCmd(), new DiabloCmd(), new CounterStrikeCmd(), new OverwatchCmd(),
				// Info Commands
				new PingCmd(), new InfoCmd(), new UserInfoCmd(), new ServerInfoCmd(),
				new RolelistCmd(),
				// French Commands
				new JokeCmd(), new DtcCmd(),
				// Admin Commands
				new ManageCoinsCmd(), new PruneCmd(), new KickCmd(), new SoftBanCmd(), new BanCmd(),
				new IamCmd(), new SettingsCmd(),
				// Owner Commands
				new LoggerCmd(), new StatsCmd(), new RestartCmd(), new LeaveCmd(),
				new GenerateRelicCmd(), new SendMessageCmd(), new ShutdownCmd(), new DatabaseCmd(),
				new CleanDatabaseCmd(),
				// Hidden Commands
				new ActivateRelicCmd(), new HelpCmd(), new BaguetteCmd(), new RelicStatusCmd());
	}

	private static void add(BaseCmd... cmds) {
		for(final BaseCmd cmd : cmds) {
			for(final String name : cmd.getNames()) {
				if(COMMANDS_MAP.putIfAbsent(name, cmd) != null) {
					LogUtils.error(String.format("Command name collision between %s and %s.",
							name, COMMANDS_MAP.get(name).getClass().getSimpleName()));
				}
			}
		}
		LogUtils.info("%d commands initialized.", cmds.length);
	}

	public static Map<String, BaseCmd> getCommands() {
		return COMMANDS_MAP;
	}

	public static BaseCmd getCommand(String name) {
		return COMMANDS_MAP.get(name);
	}
}
