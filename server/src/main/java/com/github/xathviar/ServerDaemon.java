package com.github.xathviar;

import com.badlogic.ashley.core.Engine;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractScheduledService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
public class ServerDaemon extends AbstractScheduledService {
    private Engine engine;
    private long lastUpdatetime;
    private StageEnum stage = StageEnum.NONE;

    public ServerDaemon() {
        super();
        EventBusSingleton.eventbus.register(this);
    }

    @Override
    protected void startUp() throws Exception {
        lastUpdatetime = -1;
        engine = new Engine();
    }

    @Override
    protected void shutDown() throws Exception {
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(0L, 75L, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void runOneIteration() throws Exception {
        if (lastUpdatetime < 0) {
            lastUpdatetime = System.nanoTime();
        }
        long currentUpdateTime = System.nanoTime();
        switch (stage) {
            case NONE:
                break;
            case CONNECTED:
                sendSeedToClient();
                break;
            case INIT:
                spawnMobs();
                break;
            default:
                engine.update(currentUpdateTime - lastUpdatetime);
        }
        lastUpdatetime = currentUpdateTime;
    }

    private void spawnMobs() {

    }

    private void sendSeedToClient() {
        MapSeed seed = new MapSeed(Double.toString(Math.random() * 0x9E3779B97F4A7C15L));
        EventBusSingleton.eventbus.post(seed);
    }

    @Subscribe
    public void handleMessage(StageEnum stageEnum) throws Exception {
        this.stage = stageEnum;
    }

    public static void main(String[] args) {
        ServerDaemon serverDaemon = new ServerDaemon();
        serverDaemon.startAsync();
    }
}
