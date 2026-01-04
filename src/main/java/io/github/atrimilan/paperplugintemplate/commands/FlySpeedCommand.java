package io.github.atrimilan.paperplugintemplate.commands;


import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Example command to change the fly speed of a player
 */
public class FlySpeedCommand {

    public static final String DESCRIPTION = "Sets the fly speed of a player";
    public static final Set<String> ALIASES = Set.of("fspeed", "fs");

    private static final FloatArgumentType FLYSPEED_LIMIT = FloatArgumentType.floatArg(0f, 10f);
    private static final String SPEED_ARG = "speed";
    private static final String PLAYER_ARG = "player";

    /**
     * @return A LiteralCommandNode of the "/flyspeed" command, ready to be registered
     */
    public LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("flyspeed")
                // flyspeed <speed>
                .then(Commands.argument(SPEED_ARG, FLYSPEED_LIMIT)
                        .requires(ctx -> ctx.getSender().hasPermission("ppt.flyspeed.self"))
                        .executes(this::setSelfFlySpeed))

                // flyspeed <player> <speed>
                .then(Commands.argument(PLAYER_ARG, ArgumentTypes.player())
                        .requires(ctx -> ctx.getSender().hasPermission("ppt.flyspeed.others"))
                        .then(Commands.argument(SPEED_ARG, FLYSPEED_LIMIT).executes(this::setTargetFlySpeed)))

                .build();
    }

    /**
     * Define the flight speed of the player executing the command
     */
    private int setSelfFlySpeed(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        if (!(ctx.getSource().getExecutor() instanceof Player target)) {
            throw new SimpleCommandExceptionType(() -> "Only players can fly!").create();
        }
        setFlySpeed(ctx, target);
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Define the flight speed of the specified player
     */
    private int setTargetFlySpeed(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Player target = ctx.getArgument(PLAYER_ARG, PlayerSelectorArgumentResolver.class).resolve(ctx.getSource())
                .getFirst();
        setFlySpeed(ctx, target);
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Define the flight speed of the selected player
     *
     * @param ctx    The command context
     * @param target The player to set the flight speed for
     */
    private void setFlySpeed(CommandContext<CommandSourceStack> ctx, Player target) {
        float speed = FloatArgumentType.getFloat(ctx, SPEED_ARG);
        target.setFlySpeed(speed * 0.1f);

        MiniMessage mm = MiniMessage.miniMessage();

        if (ctx.getSource().getExecutor() == target) {
            target.sendMessage(mm.deserialize("<green>Successfully set your flight speed to <gold><speed></gold>.",
                    Placeholder.unparsed(SPEED_ARG, String.valueOf(speed))));
            return;
        }

        CommandSender sender = ctx.getSource().getSender();

        sender.sendMessage(
                mm.deserialize("<gray>Successfully set <aqua><target></aqua>'s flight speed to <gold><speed></gold>.",
                        Placeholder.component("target", target.name()), // target.name() is already a Component
                        Placeholder.unparsed(SPEED_ARG, String.valueOf(speed))));
        target.sendMessage(
                mm.deserialize("<aqua><sender></aqua> <gray>has set your flight speed to <gold><speed></gold>.",
                        Placeholder.unparsed("sender", sender.getName()),
                        Placeholder.unparsed(SPEED_ARG, String.valueOf(speed))));
    }
}
