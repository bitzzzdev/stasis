package com.bitz.smartticks.scheduler;

public interface SchedulerAwareBlockEntity {
    long stasis$getLastActiveGameTime();
    void stasis$setLastActiveGameTime(long gameTime);
    boolean stasis$isActive();
    void stasis$setActive(boolean active);
    int stasis$getTickInterval();
    void stasis$setTickInterval(int interval);
    int stasis$getLastSnapshotHash();
    void stasis$setLastSnapshotHash(int hash);
    int stasis$getLastComparatorOutput();
    void stasis$setLastComparatorOutput(int value);
    int stasis$getLastPowerCache();
    void stasis$setLastPowerCache(int power);

    default void stasis$markActive(long gameTime) {
        stasis$setActive(true);
        stasis$setLastActiveGameTime(gameTime);
        stasis$setTickInterval(1);
    }
}
