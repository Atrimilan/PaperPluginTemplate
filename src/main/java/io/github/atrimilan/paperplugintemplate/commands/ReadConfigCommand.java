package io.github.atrimilan.paperplugintemplate.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

/**
 * Example command to read values from config.yml
 */
public class ReadConfigCommand {

    public static final String DESCRIPTION = "Read values from config.yml";
    public static final Set<String> ALIASES = Set.of("rc");

    private static final MiniMessage MM = MiniMessage.miniMessage();

    private final JavaPlugin plugin;

    public ReadConfigCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * @return A LiteralCommandNode of the "/read-config" command, ready to be registered
     */
    public LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("read-config")
                .requires(ctx -> ctx.getSender().hasPermission("ppt.read-config"))
                .then(Commands.literal("ultimate-answer").executes(this::readUltimateAnswer))
                .then(Commands.literal("pangram").executes(this::readPangram))
                .then(Commands.literal("boolean").executes(this::readBoolean))
                .then(Commands.literal("player").executes(this::readPlayer)).build();
    }

    private int readUltimateAnswer(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String template = "The answer to the Ultimate Question of Life, the Universe, and Everything is: <dark_red><bold><value>";
        return readValue(ctx, "example.ultimate-answer", template);
    }

    private int readPangram(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String template = "Pangram: <gold><value>";
        return readValue(ctx, "example.pangram", template);
    }

    private int readBoolean(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String template = "Isn't this a Boolean: <gold><value>";
        return readValue(ctx, "example.boolean", template);
    }

    private int readValue(CommandContext<CommandSourceStack> ctx, String path, String template)
            throws CommandSyntaxException {
        Object value = plugin.getConfig().get(path);
        if (value == null)
            throw new SimpleCommandExceptionType(() -> "Path " + path + " not found in config").create();

        ctx.getSource().getSender().sendMessage(MM.deserialize(
                template,
                Placeholder.unparsed("value", String.valueOf(value))
        ));
        return Command.SINGLE_SUCCESS;
    }

    private int readPlayer(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ConfigurationSection player = plugin.getConfig().getConfigurationSection("example.player");
        if (player == null)
            throw new SimpleCommandExceptionType(() -> "Player not found in config").create();

        String uuid = player.getString("uuid");
        if (uuid == null)
            throw new SimpleCommandExceptionType(() -> "Player UUID is missing").create();

        if (!player.contains("location.x") || !player.contains("location.y") || !player.contains("location.z"))
            throw new SimpleCommandExceptionType(() -> "Player location is missing").create();

        ctx.getSource().getSender().sendMessage(MM.deserialize(
                "Player <aqua><uuid></aqua> is at <gold><x> <y> <z></gold>",
                Placeholder.unparsed("uuid", uuid),
                Placeholder.unparsed("x", String.valueOf(Math.round(player.getDouble("location.x")))),
                Placeholder.unparsed("y", String.valueOf(Math.round(player.getDouble("location.y")))),
                Placeholder.unparsed("z", String.valueOf(Math.round(player.getDouble("location.z")))))
        );
        return Command.SINGLE_SUCCESS;
    }
}
