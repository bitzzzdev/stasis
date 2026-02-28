package com.bitz.smartticks;

import com.bitz.smartticks.command.SmartTicksCommands;
import com.bitz.smartticks.scheduler.BlockEntityScheduler;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartTicksMod implements ModInitializer {
    public static final String MOD_ID = "stasis";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        AutoConfig.register(com.bitz.smartticks.config.SmartTicksConfig.class, JanksonConfigSerializer::new);
        BlockEntityScheduler.bootstrap();
        SmartTicksCommands.register();
        LOGGER.info("Stasis initialized");
    }
}
