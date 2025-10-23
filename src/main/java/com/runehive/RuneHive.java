package com.runehive;

import com.runehive.content.WellOfGoodwill;
import com.runehive.content.bloodmoney.BloodChestEvent;
import com.runehive.content.clanchannel.ClanRepository;
import com.runehive.content.itemaction.ItemActionRepository;
import com.runehive.content.lms.LMSGameEvent;
import com.runehive.content.lms.loadouts.LMSLoadoutManager;
import com.runehive.content.lms.lobby.LMSLobbyEvent;
import com.runehive.content.mysterybox.MysteryBox;
import com.runehive.content.preloads.PreloadRepository;
import com.runehive.content.shootingstar.ShootingStar;
import com.runehive.content.skill.SkillRepository;
import com.runehive.content.tradingpost.TradingPost;
import com.runehive.content.triviabot.TriviaBot;
import com.runehive.content.wintertodt.Wintertodt;
import com.runehive.fs.cache.FileSystem;
import com.runehive.fs.cache.decoder.*;
import com.runehive.game.engine.GameEngine;
import com.runehive.game.plugin.PluginManager;
import com.runehive.game.service.*;
import com.runehive.game.task.impl.ClanUpdateEvent;
import com.runehive.game.task.impl.DoubleExperienceEvent;
import com.runehive.game.task.impl.MessageEvent;
import com.runehive.game.task.impl.PlayerSaveEvent;
import com.runehive.game.world.World;
import com.runehive.game.world.WorldType;
import com.runehive.game.world.cronjobs.Jobs;
import com.runehive.game.world.entity.combat.attack.listener.CombatListenerManager;
import com.runehive.game.world.entity.combat.strategy.npc.boss.skotizo.SkotizoEvent;
import com.runehive.game.world.entity.mob.npc.definition.NpcDefinition;
import com.runehive.game.world.entity.mob.player.BannedPlayers;
import com.runehive.game.world.entity.mob.player.IPBannedPlayers;
import com.runehive.game.world.entity.mob.player.IPMutedPlayers;
import com.runehive.game.world.entity.mob.player.profile.ProfileRepository;
import com.runehive.game.world.items.ItemDefinition;
import com.runehive.io.PacketListenerLoader;
import com.runehive.net.LoginExecutorService;
import com.runehive.util.GameSaver;
import com.runehive.util.Stopwatch;
import com.runehive.util.parser.impl.*;
import dev.advo.fs.FileServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jire.runehiveps.OldToNew;
import org.jire.runehiveps.objectexamines.ObjectExamines;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import plugin.click.item.ClueScrollPlugin;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class RuneHive {

    private static final Logger logger = LogManager.getLogger(RuneHive.class);

    public static final AtomicBoolean serverStarted = new AtomicBoolean(false);
    public static final Stopwatch UPTIME = Stopwatch.start();

    // services
    private static final StartupService startupService = new StartupService();
    private static final GameEngine gameService = new GameEngine();
    private static final NetworkService networkService = new NetworkService();

    private static final RuneHive INSTANCE = new RuneHive();

    private final LoginExecutorService loginExecutorService;

    private RuneHive() {
        loginExecutorService = new LoginExecutorService(Runtime.getRuntime().availableProcessors());
    }

    private void processSequentialStartupTasks() {
        OldToNew.load();
        try {
            //object/region decoding must be done before parallel.
            new ObjectRemovalParser().run();
            final FileSystem fs = FileSystem.create("data/cache");
            new ObjectDefinitionDecoder(fs).run();
            new MapDefinitionDecoder(fs).run();
            new RegionDecoder(fs).run();
            new AnimationDefinitionDecoder(fs).run();
            CacheNpcDefinition.unpackConfig(fs.getArchive(FileSystem.CONFIG_ARCHIVE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ItemDefinition.createParser().run();
        NpcDefinition.createParser().run();
        ObjectExamines.loadObjectExamines();
        new CombatProjectileParser().run();
        CombatListenerManager.load();
        new NpcSpawnParser().run();
        new NpcDropParser().run();
        new NpcForceChatParser().run();
        new StoreParser().run();
        new GlobalObjectParser().run();
        // ShootingStar.init(); // Disabled - causes deadlock
        Wintertodt.init();
    }

    /**
     * Called after the sequential startup tasks, use this for faster startup.
     * Try not to use this method for tasks that rely on other tasks you'll run into
     * issues.
     */
    private void processParallelStartupTasks() {
        startupService.submit(new PacketSizeParser());
        startupService.submit(new PacketListenerLoader());
        startupService.submit(TriviaBot::declare);
//        startupService.submit(PersonalStoreSaver::loadPayments);
        startupService.submit(ClanRepository::loadChannels);
        //  startupService.submit(GlobalRecords::load);
        startupService.submit(SkillRepository::load);
        startupService.submit(ProfileRepository::load);
        startupService.submit(ItemActionRepository::declare);
        startupService.submit(ClueScrollPlugin::declare);
        startupService.submit(MysteryBox::load);
        startupService.submit(GameSaver::load);
        startupService.submit(PreloadRepository::declare);
        startupService.submit(TradingPost::loadAllListings);
        startupService.submit(TradingPost::loadItemHistory);
        startupService.submit(TradingPost::loadRecentItemHistory);
        startupService.shutdown();
    }

    /**
     * Called when the game engine is running and all the startup tasks have finished loading
     */
    private void onStart() {
        if (WellOfGoodwill.isActive()) {
            World.schedule(new DoubleExperienceEvent());
        }

        World.schedule(new MessageEvent());
        World.schedule(new ClanUpdateEvent());
        World.schedule(new SkotizoEvent());
        World.schedule(new PlayerSaveEvent());
//        World.schedule(new BotStartupEvent());
        World.schedule(new BloodChestEvent());
        // World.schedule(new LMSLobbyEvent()); // Disabled - causes deadlock
        // World.schedule(new LMSGameEvent()); // Disabled - related to LMS
        logger.info("Events have been scheduled");
    }

    public void start() throws Exception {
        if (Config.FORUM_INTEGRATION) {
            ForumService.start(); // used to check users logging in with website credentials

            if (Config.WORLD_TYPE == WorldType.LIVE) {
                PostgreService.start(); // used to start the postgres connection pool
                WebsitePlayerCountService.getInstance().startAsync(); // used to display player count on website
            }
        }

        new FileServer().start();

        logger.info("RuneHive is running (client version " + Config.CLIENT_VERSION + ")");
        logger.info(String.format("Game Engine=%s", Config.PARALLEL_GAME_ENGINE ? "Parallel" : "Sequential"));
        processSequentialStartupTasks();
        processParallelStartupTasks();

        startupService.awaitUntilFinished(5, TimeUnit.MINUTES);
        logger.info("Startup service finished");

        LMSLoadoutManager.load();

        PluginManager.load("plugin");

        gameService.startAsync().awaitRunning();
        logger.info("Game service started");

        onStart();

        Jobs.load();

        BannedPlayers.load();
        IPBannedPlayers.load();
        IPMutedPlayers.load();

        networkService.start(Config.SERVER_PORT);
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("shutting down server, initializing shutdown hook");
            World.save();
            com.runehive.content.ai.LazyAIManager.shutdown();
        }));

        try {
            INSTANCE.start();
        } catch (Throwable t) {
            logger.error("A problem has been encountered while starting the server.", t);
        }

    }

    public static RuneHive getInstance() {
        return INSTANCE;
    }

    public static DateTime currentDateTime() {
        return new DateTime(timeZone());
    }

    public static DateTimeZone timeZone() {
        return DateTimeZone.UTC;
    }

    public LoginExecutorService getLoginExecutorService() {
        return this.loginExecutorService;
    }
}
