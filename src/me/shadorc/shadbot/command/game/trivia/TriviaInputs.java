package me.shadorc.shadbot.command.game.trivia;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import me.shadorc.shadbot.object.Emoji;
import me.shadorc.shadbot.object.Inputs;
import me.shadorc.shadbot.utils.DiscordUtils;
import me.shadorc.shadbot.utils.NumberUtils;
import reactor.core.publisher.Mono;

public class TriviaInputs extends Inputs {

    private final TriviaGame game;

    public TriviaInputs(DiscordClient client, TriviaGame game) {
        super(client, game.getDuration());
        this.game = game;
    }

    @Override
    public Mono<Boolean> isValidEvent(MessageCreateEvent event) {
        if (event.getMessage().getContent().isEmpty() || event.getMember().isEmpty()) {
            return Mono.just(false);
        }

        if (!event.getMessage().getChannelId().equals(this.game.getContext().getChannelId())) {
            return Mono.just(false);
        }

        return Mono.just(true);
    }

    @Override
    public boolean takeEventWile(MessageCreateEvent ignored) {
        return this.game.isScheduled();
    }

    @Override
    public Mono<Void> processEvent(MessageCreateEvent event) {
        return this.game.isCancelMessage(event.getMessage())
                .flatMap(isCancelMsg -> {
                    final Member member = event.getMember().get();
                    if (isCancelMsg) {
                        return event.getMessage().getChannel()
                                .flatMap(channel -> DiscordUtils.sendMessage(
                                        String.format(Emoji.CHECK_MARK + " Trivia game cancelled by **%s**.",
                                                member.getUsername()), channel))
                                .then(Mono.fromRunnable(this.game::stop));
                    }

                    // It's a number or a text
                    final String content = event.getMessage().getContent().get();
                    final Integer choice = NumberUtils.asIntBetween(content, 1, this.game.getAnswers().size());

                    // Message is a text and doesn't match any answers, ignore it
                    if (choice == null && this.game.getAnswers().stream().noneMatch(content::equalsIgnoreCase)) {
                        return Mono.empty();
                    }

                    // If the user has already answered and has been warned, ignore him
                    if (this.game.getPlayers().containsKey(member.getId())
                            && this.game.getPlayers().get(member.getId()).hasAnswered()) {
                        return Mono.empty();
                    }

                    final String answer = choice == null ? content : this.game.getAnswers().get(choice - 1);

                    if (this.game.getPlayers().containsKey(member.getId())) {
                        this.game.hasAnswered(member.getId());
                        return event.getMessage().getChannel()
                                .flatMap(channel -> DiscordUtils.sendMessage(
                                        String.format(Emoji.GREY_EXCLAMATION + " (**%s**) You can only answer once.",
                                                member.getUsername()), channel))
                                .then();
                    } else if (answer.equalsIgnoreCase(this.game.getCorrectAnswer())) {
                        return this.game.win(member).then();
                    } else {
                        this.game.hasAnswered(member.getId());
                        return event.getMessage().getChannel()
                                .flatMap(channel -> DiscordUtils.sendMessage(
                                        String.format(Emoji.THUMBSDOWN + " (**%s**) Wrong answer.",
                                                member.getUsername()), channel))
                                .then();
                    }
                });
    }

}
