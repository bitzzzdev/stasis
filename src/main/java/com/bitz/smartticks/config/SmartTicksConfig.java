package com.bitz.smartticks.config;

import com.bitz.smartticks.scheduler.BlockEntityScheduler;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.HashSet;
import java.util.Set;

@Config(name = "stasis")
public class SmartTicksConfig implements ConfigData {
    public boolean enabled = true;
    public boolean debugLogging = false;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
    public int mediumAfterTicks = 20;

    @ConfigEntry.BoundedDiscrete(min = 2, max = 200)
    public int mediumInterval = 5;

    @ConfigEntry.BoundedDiscrete(min = 20, max = 1200)
    public int slowAfterTicks = 200;

    @ConfigEntry.BoundedDiscrete(min = 5, max = 1200)
    public int slowInterval = 20;

    @ConfigEntry.BoundedDiscrete(min = 100, max = 72000)
    public int freezeAfterTicks = 600;

    public boolean chunkHeuristics = false;

    @ConfigEntry.BoundedDiscrete(min = 20, max = 200)
    public int distantPlayerInterval = 20;

    public Set<String> whitelist = new HashSet<>(BlockEntityScheduler.defaultWhitelist());
    public Set<String> criticalBlacklist = new HashSet<>(BlockEntityScheduler.defaultCriticalBlacklist());
}
