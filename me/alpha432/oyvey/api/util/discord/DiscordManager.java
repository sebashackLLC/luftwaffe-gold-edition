//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.discord;

import java.util.function.*;
import me.alpha432.oyvey.*;
import java.time.*;
import java.util.concurrent.*;

public class DiscordManager
{
    private static DiscordManager INSTANCE;
    private final long applicationId;
    private final RichPresence richPresence;
    private final ScheduledExecutorService executor;
    private ScheduledFuture<?> restartTask;
    private final long initialRestartDelay = 10L;
    private final int maxBackoffMultiplier = 5;
    private boolean autoRestart;
    private int maxRestartAttempts;
    private long restartDelay;
    private int currentRestartAttempts;
    private boolean reachedMaxAttempts;
    private boolean wasReconnecting;
    private boolean enabled;
    
    private DiscordManager(final long applicationId) {
        this.autoRestart = true;
        this.maxRestartAttempts = 5;
        this.restartDelay = 10L;
        this.currentRestartAttempts = 0;
        this.reachedMaxAttempts = false;
        this.wasReconnecting = false;
        this.enabled = false;
        this.applicationId = applicationId;
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.richPresence = new RichPresence();
        DiscordIPC.setOnError((BiConsumer)this::handleError);
    }
    
    public static DiscordManager getInstance() {
        if (DiscordManager.INSTANCE == null) {
            DiscordManager.INSTANCE = new DiscordManager(1455620241554936120L);
        }
        return DiscordManager.INSTANCE;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setAutoRestart(final boolean autoRestart) {
        this.autoRestart = autoRestart;
    }
    
    public void setMaxRestartAttempts(final int maxRestartAttempts) {
        this.maxRestartAttempts = maxRestartAttempts;
    }
    
    public long getRestartDelay() {
        return this.restartDelay;
    }
    
    public void setRestartDelay(final long restartDelay) {
        this.restartDelay = restartDelay;
    }
    
    public boolean start() {
        return this.start(true);
    }
    
    public boolean start(final boolean isInitialStart) {
        if (this.enabled) {
            return true;
        }
        if (isInitialStart) {
            this.resetRetryState();
        }
        if (this.reachedMaxAttempts) {
            OyVey.LOGGER.warn("Not starting Discord RPC - maximum restart attempts already reached");
            return false;
        }
        final boolean success = DiscordIPC.start(this.applicationId, this::onReady);
        if (success) {
            this.enabled = true;
            this.updatePresence();
            OyVey.LOGGER.info("Discord RPC started successfully");
            this.wasReconnecting = (this.currentRestartAttempts > 0);
        }
        else {
            OyVey.LOGGER.error("Failed to start Discord RPC");
            if (this.autoRestart) {
                this.scheduleRestart();
            }
        }
        return success;
    }
    
    public void stop() {
        this.enabled = false;
        this.cancelRestartTask();
        DiscordIPC.stop();
        OyVey.LOGGER.info("Discord RPC stopped");
    }
    
    public void setDetails(final String details) {
        this.richPresence.setDetails(details);
        this.updatePresence();
    }
    
    public void setState(final String state) {
        this.richPresence.setState(state);
        this.updatePresence();
    }
    
    public void setLargeImage(final String key, final String text) {
        this.richPresence.setLargeImage(key, text);
        this.updatePresence();
    }
    
    public void setSmallImage(final String key, final String text) {
        this.richPresence.setSmallImage(key, text);
        this.updatePresence();
    }
    
    public void setStartTimestamp(final long timestamp) {
        this.richPresence.setStart(timestamp);
        this.updatePresence();
    }
    
    public void setStartTimestampToNow() {
        this.setStartTimestamp(Instant.now().getEpochSecond());
    }
    
    public void setEndTimestamp(final long timestamp) {
        this.richPresence.setEnd(timestamp);
        this.updatePresence();
    }
    
    public void updatePresence() {
        if (this.enabled && DiscordIPC.isConnected()) {
            DiscordIPC.setActivity(this.richPresence);
        }
    }
    
    private void handleError(final int code, final String message) {
        OyVey.LOGGER.error("Discord RPC error {}: {}", (Object)code, (Object)message);
        this.enabled = false;
        if (this.reachedMaxAttempts) {
            OyVey.LOGGER.warn("Maximum Discord RPC restart attempts already reached. Not retrying.");
            return;
        }
        if (this.autoRestart && (this.maxRestartAttempts < 0 || this.currentRestartAttempts < this.maxRestartAttempts)) {
            this.scheduleRestart();
        }
        else if (this.currentRestartAttempts >= this.maxRestartAttempts) {
            this.reachedMaxAttempts = true;
            OyVey.LOGGER.warn("Maximum Discord RPC restart attempts reached ({})", (Object)this.maxRestartAttempts);
        }
    }
    
    private void scheduleRestart() {
        this.cancelRestartTask();
        ++this.currentRestartAttempts;
        final int backoffFactor = Math.min(this.currentRestartAttempts, 5);
        this.restartDelay = 10L * backoffFactor;
        OyVey.LOGGER.info("Scheduling Discord RPC restart in {} seconds (attempt {}/{})", (Object)this.restartDelay, (Object)this.currentRestartAttempts, (Object)((this.maxRestartAttempts < 0) ? "unlimited" : Integer.valueOf(this.maxRestartAttempts)));
        this.restartTask = this.executor.schedule(() -> {
            OyVey.LOGGER.info("Attempting to restart Discord RPC");
            DiscordIPC.stop();
            this.start(false);
        }, this.restartDelay, TimeUnit.SECONDS);
    }
    
    private void cancelRestartTask() {
        if (this.restartTask != null && !this.restartTask.isDone()) {
            this.restartTask.cancel(false);
            this.restartTask = null;
        }
    }
    
    private void onReady() {
        OyVey.LOGGER.info("Discord RPC connected as {}", (Object)DiscordIPC.getUser().username);
        if (this.wasReconnecting) {
            OyVey.LOGGER.info("Discord RPC reconnected successfully after {} attempts", (Object)this.currentRestartAttempts);
            this.resetRetryState();
        }
    }
    
    public void resetRetryState() {
        this.currentRestartAttempts = 0;
        this.reachedMaxAttempts = false;
        this.restartDelay = 10L;
        this.wasReconnecting = false;
    }
    
    public void shutdown() {
        this.stop();
        this.executor.shutdown();
    }
}
