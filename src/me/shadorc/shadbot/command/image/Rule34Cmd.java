package me.shadorc.shadbot.command.image;

import java.io.IOException;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import me.shadorc.shadbot.core.command.AbstractCommand;
import me.shadorc.shadbot.core.command.CommandCategory;
import me.shadorc.shadbot.core.command.Context;
import me.shadorc.shadbot.core.command.annotation.Command;
import me.shadorc.shadbot.core.command.annotation.RateLimited;
import me.shadorc.shadbot.exception.MissingArgumentException;
import me.shadorc.shadbot.utils.BotUtils;
import me.shadorc.shadbot.utils.ExceptionUtils;
import me.shadorc.shadbot.utils.FormatUtils;
import me.shadorc.shadbot.utils.MathUtils;
import me.shadorc.shadbot.utils.NetUtils;
import me.shadorc.shadbot.utils.StringUtils;
import me.shadorc.shadbot.utils.TextUtils;
import me.shadorc.shadbot.utils.command.Emoji;
import me.shadorc.shadbot.utils.command.LoadingMessage;
import me.shadorc.shadbot.utils.embed.EmbedUtils;
import me.shadorc.shadbot.utils.embed.HelpBuilder;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

@RateLimited
@Command(category = CommandCategory.IMAGE, names = { "rule34" }, alias = "r34")
public class Rule34Cmd extends AbstractCommand {

	private static final int MAX_TAGS_LENGTH = 400;

	@Override
	public void execute(Context context) throws MissingArgumentException {
		if(!context.getChannel().isNSFW()) {
			BotUtils.sendMessage(TextUtils.mustBeNSFW(context.getPrefix()), context.getChannel());
			return;
		}

		if(!context.hasArg()) {
			throw new MissingArgumentException();
		}

		LoadingMessage loadingMsg = new LoadingMessage("Loading message...", context.getChannel());
		loadingMsg.send();

		try {
			JSONObject mainObj = XML.toJSONObject(NetUtils.getBody("https://rule34.xxx/index.php?"
					+ "page=dapi"
					+ "&s=post"
					+ "&q=index"
					+ "&tags=" + NetUtils.encode(context.getArg().replace(" ", "_"))));

			JSONObject postsObj = mainObj.getJSONObject("posts");

			if(postsObj.getInt("count") == 0) {
				loadingMsg.edit(TextUtils.noResult(context.getArg()));
				return;
			}

			JSONObject postObj;
			if(postsObj.get("post") instanceof JSONArray) {
				JSONArray postsArray = postsObj.getJSONArray("post");
				postObj = postsArray.getJSONObject(MathUtils.rand(postsArray.length() - 1));
			} else {
				postObj = postsObj.getJSONObject("post");
			}

			String[] tags = postObj.getString("tags").trim().split(" ");

			if(postObj.getBoolean("has_children") || this.isNotLegal(tags)) {
				loadingMsg.edit(Emoji.WARNING + " Sorry, I don't display images containing children or tagged with `loli` or `shota`.");
				return;
			}

			String formattedtags = StringUtils.truncate(FormatUtils.formatArray(tags, tag -> "`" + tag.toString().trim() + "`", " "), MAX_TAGS_LENGTH);
			String fileUrl = this.getValidURL(postObj.getString("file_url"));
			String sourceUrl = this.getValidURL(postObj.get("source").toString());

			EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
					.setLenient(true)
					.withAuthorName("Rule34 (Search: " + context.getArg() + ")")
					.withUrl(fileUrl)
					.withThumbnail("http://rule34.paheal.net/themes/rule34v2/rule34_logo_top.png")
					.appendField("Resolution", String.format("%dx%d", postObj.getInt("width"), postObj.getInt("height")), false)
					.appendField("Source", sourceUrl, false)
					.appendField("Tags", formattedtags, false)
					.withImage(fileUrl)
					.withFooterText("If there is no preview, click on the title to see the media (probably a video)");
			loadingMsg.edit(builder.build());

		} catch (JSONException | IOException err) {
			ExceptionUtils.handle("getting an image from Rule34", context, err);
		}
	}

	private String getValidURL(String url) {
		if(url == null || !url.startsWith("//")) {
			return url;
		}
		return "http:" + url;
	}

	private boolean isNotLegal(String... tags) {
		return Arrays.asList(tags).stream().anyMatch(tag -> tag.contains("loli") || tag.contains("shota"));
	}

	@Override
	public EmbedObject getHelp(Context context) {
		return new HelpBuilder(this, context.getPrefix())
				.setDescription("Show a random image corresponding to a tag from Rule34 website.")
				.addArg("tag", false)
				.build();
	}
}
