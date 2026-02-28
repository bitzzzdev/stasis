package com.bitz.smartticks.scheduler;

import com.bitz.smartticks.SmartTicksMod;
import com.bitz.smartticks.config.SmartTicksConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public final class BlockEntityScheduler {
    private static final AtomicLong TOTAL_CONSIDERED = new AtomicLong();
    private static final AtomicLong TOTAL_SKIPPED = new AtomicLong();
    private static final AtomicLong TOTAL_TICKED = new AtomicLong();

    private BlockEntityScheduler() {}

    public static void bootstrap() {
        SmartTicksMod.LOGGER.info("BlockEntity scheduler bootstrapped");
    }

    public static boolean shouldTick(World world, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        TOTAL_CONSIDERED.incrementAndGet();
        SmartTicksConfig config = config();

        if (!config.enabled || world.isClient() || !(world instanceof ServerWorld serverWorld)) {
            TOTAL_TICKED.incrementAndGet();
            return true;
        }

        if (isCritical(blockEntity, config) || !isWhitelisted(blockEntity, config)) {
            TOTAL_TICKED.incrementAndGet();
            return true;
        }

        if (!(blockEntity instanceof SchedulerAwareBlockEntity aware)) {
            TOTAL_TICKED.incrementAndGet();
            return true;
        }

        long gameTime = serverWorld.getTime();
        if (aware.stasis$getLastActiveGameTime() == 0L) {
            aware.stasis$markActive(gameTime);
        }

        int snapshotHash = snapshotHash(world, pos, blockEntity);
        if (snapshotHash != aware.stasis$getLastSnapshotHash()) {
            aware.stasis$setLastSnapshotHash(snapshotHash);
            aware.stasis$markActive(gameTime);
        }

        int comparator = world.getReceivedRedstonePower(pos);
        if (comparator != aware.stasis$getLastComparatorOutput()) {
            aware.stasis$setLastComparatorOutput(comparator);
            aware.stasis$markActive(gameTime);
        }

        int power = state.getStrongRedstonePower(world, pos, Direction.UP);
        if (power != aware.stasis$getLastPowerCache()) {
            aware.stasis$setLastPowerCache(power);
            aware.stasis$markActive(gameTime);
        }

        long idleTicks = Math.max(0L, gameTime - aware.stasis$getLastActiveGameTime());
        int interval = 1;
        if (idleTicks >= config.freezeAfterTicks) {
            interval = 0;
        } else if (idleTicks >= config.slowAfterTicks) {
            interval = config.slowInterval;
        } else if (idleTicks >= config.mediumAfterTicks) {
            interval = config.mediumInterval;
        }

        if (config.chunkHeuristics
            && world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 64.0, false) == null) {
            interval = Math.max(interval, config.distantPlayerInterval);
        }

        aware.stasis$setTickInterval(interval);
        aware.stasis$setActive(interval == 1);

        if (interval == 0 || (gameTime % interval) != 0L) {
            TOTAL_SKIPPED.incrementAndGet();
            return false;
        }

        TOTAL_TICKED.incrementAndGet();
        return true;
    }

    public static void markActive(BlockEntity blockEntity) {
        if (blockEntity.getWorld() == null || blockEntity.getWorld().isClient()) return;
        if (blockEntity instanceof SchedulerAwareBlockEntity aware) {
            aware.stasis$markActive(blockEntity.getWorld().getTime());
        }
    }

    public static long totalConsidered() { return TOTAL_CONSIDERED.get(); }
    public static long totalSkipped() { return TOTAL_SKIPPED.get(); }
    public static long totalTicked() { return TOTAL_TICKED.get(); }
    public static double reductionPct() {
        long considered = TOTAL_CONSIDERED.get();
        return considered == 0 ? 0.0 : (TOTAL_SKIPPED.get() * 100.0) / considered;
    }
    public static void resetStats() {
        TOTAL_CONSIDERED.set(0);
        TOTAL_SKIPPED.set(0);
        TOTAL_TICKED.set(0);
    }

    private static boolean isCritical(BlockEntity be, SmartTicksConfig config) {
        return config.criticalBlacklist.contains(blockEntityId(be));
    }

    private static boolean isWhitelisted(BlockEntity be, SmartTicksConfig config) {
        return config.whitelist.isEmpty() || config.whitelist.contains(blockEntityId(be));
    }

    private static int snapshotHash(World world, BlockPos pos, BlockEntity blockEntity) {
        int hash = 1;
        hash = 31 * hash + blockEntity.getCachedState().hashCode();
        hash = 31 * hash + pos.hashCode();
        if (blockEntity instanceof Inventory inventory) {
            hash = 31 * hash + inventory.size();
            for (int i = 0; i < inventory.size(); i++) {
                hash = 31 * hash + ItemStack.hashCode(inventory.getStack(i));
            }
        }
        hash = 31 * hash + world.getReceivedRedstonePower(pos);
        return hash;
    }

    private static String blockEntityId(BlockEntity be) {
        BlockEntityType<?> type = be.getType();
        return BlockEntityType.getId(type).toString();
    }

    private static SmartTicksConfig config() {
        return AutoConfig.getConfigHolder(SmartTicksConfig.class).getConfig();
    }

    public static Set<String> defaultWhitelist() {
        Set<String> out = new HashSet<>();
        out.add("minecraft:chest");
        out.add("minecraft:furnace");
        out.add("minecraft:hopper");
        out.add("minecraft:brewing_stand");
        out.add("minecraft:comparator");
        return out;
    }

    public static Set<String> defaultCriticalBlacklist() {
        Set<String> out = new HashSet<>();
        out.add("minecraft:beacon");
        out.add("minecraft:mob_spawner");
        out.add("minecraft:trial_spawner");
        out.add("minecraft:vault");
        out.add("minecraft:lectern");
        out.add("minecraft:bell");
        out.add("minecraft:campfire");
        out.add("minecraft:soul_campfire");
        return out;
    }
}
