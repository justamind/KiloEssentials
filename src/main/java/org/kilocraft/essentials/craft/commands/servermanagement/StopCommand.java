package org.kilocraft.essentials.craft.commands.servermanagement;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.indicode.fabric.permissions.Thimble;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.kilocraft.essentials.api.KiloServer;
import org.kilocraft.essentials.api.chat.TextFormat;
import org.kilocraft.essentials.api.util.CommandHelper;
import org.kilocraft.essentials.api.util.CommandSuggestions;
import org.kilocraft.essentials.craft.KiloCommands;
import org.kilocraft.essentials.craft.KiloEssentials;

public class StopCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        KiloCommands.getCommandPermission("server.stop");
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("stop")
                .then(CommandManager.argument("args", StringArgumentType.greedyString())
                        .suggests((context, builder1) -> CommandSuggestions.suggestInput.getSuggestions(context, builder1))
                    .executes(c -> execute(c, StringArgumentType.getString(c, "args")))
                )
                .requires(s -> Thimble.hasPermissionOrOp(s, KiloCommands.getCommandPermission("server.manage.stop"), 4))
                .executes(c -> execute(c, ""));

        dispatcher.register(builder);
    }

    private static int execute(CommandContext<ServerCommandSource> context, String s) {
        boolean isConfirmed = s.startsWith("-confirmed");
        boolean isForceCrash = s.startsWith("-crash");

        if (!isForceCrash) {
            if (!CommandHelper.isConsole(context.getSource())) {
                if (isConfirmed) {
                    TextFormat.sendToUniversalSource(context.getSource(), "&cStopping the server...", false);
                    if (!CommandHelper.isConsole(context.getSource())) KiloEssentials.getLogger().warn("%s is trying to stop the server", context.getSource().getName());
                    KiloServer.getServer().shutdown();
                } else {
                    LiteralText literalText = new LiteralText("Please confirm your action by clicking on this message!");
                    literalText.styled((style) -> {
                        style.setColor(Formatting.RED);
                        style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("[!] Click here to stop the server").formatted(Formatting.YELLOW)));
                        style.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/stop -confirmed"));
                    });

                    context.getSource().sendFeedback(literalText, false);
                }
            } else if (CommandHelper.isConsole(context.getSource())) {
                KiloServer.getServer().shutdown();
            }
        } else {
            if (isConfirmed) {
                throw new RuntimeException("[!] \"" + context.getSource().getName() + "\" forced a crash");
            } else {
                LiteralText literalText = new LiteralText("Please confirm your action by clicking on this message!");
                literalText.styled((style) -> {
                    style.setColor(Formatting.RED);
                    style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("[!] Click here to stop the server").formatted(Formatting.YELLOW)));
                    style.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/stop -crash -confirmed"));
                });

                context.getSource().sendFeedback(literalText, false);
            }
        }

        return 1;
    }

}
