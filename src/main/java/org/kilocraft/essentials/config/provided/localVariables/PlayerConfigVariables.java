package org.kilocraft.essentials.config.provided.localVariables;

import net.minecraft.server.network.ServerPlayerEntity;
import org.kilocraft.essentials.api.chat.TextFormat;
import org.kilocraft.essentials.config.variablehelper.LocalConfigVariable;

import java.util.HashMap;

public class PlayerConfigVariables implements LocalConfigVariable {
    private ServerPlayerEntity player;

    public PlayerConfigVariables(ServerPlayerEntity playerEntity) {
        this.player = playerEntity;
    }

    @Override
    public String getPrefix() {
        return "PLAYER";
    }

    @Override
    public HashMap<String, String> variables() {
        return new HashMap<String, String>(){{
            put("NAME", player.getName().asString());
            put("DISPLAYNAME", player.getDisplayName().asString());
            put("PING", String.valueOf(player.pingMilliseconds));
            put("FORMATTED_PING", TextFormat.getFormattedPing(player.pingMilliseconds));
        }};
    }

}