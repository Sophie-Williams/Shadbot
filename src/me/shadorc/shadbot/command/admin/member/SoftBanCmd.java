package me.shadorc.shadbot.command.admin.member;

import discord4j.core.object.entity.Member;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.EmbedCreateSpec;
import me.shadorc.shadbot.core.command.Context;
import me.shadorc.shadbot.utils.embed.help.HelpBuilder;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

public class SoftBanCmd extends RemoveMemberCmd {

    public SoftBanCmd() {
        super("softban", "softbanned", Permission.BAN_MEMBERS);
    }

    @Override
    public Mono<Void> action(Member member, String reason) {
        return member.ban(spec -> spec.setReason(reason).setDeleteMessageDays(7))
                .then(member.unban());
    }

    @Override
    public Consumer<EmbedCreateSpec> getHelp(Context context) {
        return new HelpBuilder(this, context)
                .setDescription("Ban and instantly unban user.\nIt's like kicking him but it "
                        + "also deletes his messages from the last 7 days.")
                .addArg("@user", false)
                .addArg("reason", true)
                .build();
    }

}
