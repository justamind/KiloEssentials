package org.kilocraft.essentials.commands.misc;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import org.kilocraft.essentials.KiloCommands;
import org.kilocraft.essentials.api.feature.ConfigurableFeature;
import org.kilocraft.essentials.chat.ChatMessage;
import org.kilocraft.essentials.chat.KiloChat;
import org.kilocraft.essentials.config.KiloConfig;
import org.kilocraft.essentials.config.provided.localVariables.PlayerConfigVariables;

import static net.minecraft.server.command.CommandManager.literal;

public class DiscordCommand implements ConfigurableFeature {

    @Override
    public boolean register() {
        KiloCommands.getDispatcher().register(literal("discord").executes(DiscordCommand::execute));
        return true;
    }

    public DiscordCommand() {
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        KiloChat.sendMessageTo(context.getSource().getPlayer(),
                new ChatMessage(
                        KiloConfig.getProvider().getMessages().getLocal(
                                true,
                                "commands.discord",
                                new PlayerConfigVariables(context.getSource().getPlayer())
                        ),
                        true
                ));
        return 1;
    }

}