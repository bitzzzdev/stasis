package com.bitz.smartticks.command;

import com.bitz.smartticks.config.SmartTicksConfig;
import com.bitz.smartticks.scheduler.BlockEntityScheduler;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import me.shedaniel.autoconfig.AutoConfig;

public final class SmartTicksCommands {
    private SmartTicksCommands() {}

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> register(dispatcher));
    }

    private static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("smartticks")
            .then(CommandManager.literal("stats").executes(ctx -> {
                long considered = BlockEntityScheduler.totalConsidered();
                long skipped = BlockEntityScheduler.totalSkipped();
                long ticked = BlockEntityScheduler.totalTicked();
                double reduction = BlockEntityScheduler.reductionPct();
                ctx.getSource().sendFeedback(() -> Text.literal(
                    "Stasis stats | considered=" + considered +
                    " ticked=" + ticked +
                    " skipped=" + skipped +
                    " reduction=" + String.format("%.2f", reduction) + "%"
                ), false);
                return 1;
            }))
            .then(CommandManager.literal("dump").executes(ctx -> {
                SmartTicksConfig config = AutoConfig.getConfigHolder(SmartTicksConfig.class).getConfig();
                ctx.getSource().sendFeedback(() -> Text.literal(
                    "Stasis config | enabled=" + config.enabled +
                    " mediumAfter=" + config.mediumAfterTicks +
                    " slowAfter=" + config.slowAfterTicks +
                    " freezeAfter=" + config.freezeAfterTicks +
                    " whitelist=" + config.whitelist.size() +
                    " blacklist=" + config.criticalBlacklist.size()
                ), false);
                return 1;
            }))
            .then(CommandManager.literal("toggle").executes(ctx -> {
                var holder = AutoConfig.getConfigHolder(SmartTicksConfig.class);
                SmartTicksConfig config = holder.getConfig();
                config.enabled = !config.enabled;
                holder.save();
                BlockEntityScheduler.resetStats();
                ctx.getSource().sendFeedback(() -> Text.literal("Stasis " + (config.enabled ? "enabled" : "disabled")), true);
                return 1;
            }))
        );
    }
}
