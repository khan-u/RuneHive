package com.runehive.game.world.cronjobs;

import com.runehive.game.world.cronjobs.impl.DoubleExperienceJob;
import com.runehive.game.world.cronjobs.impl.OpenAIRelayJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Jobs {

    private static final SchedulerFactory factory = new StdSchedulerFactory();

    public static void load() throws SchedulerException {
        final Scheduler scheduler = factory.getScheduler();

        // Double Experience Job - Fired every hour
        final JobDetail doubleExpJob = JobBuilder.newJob(DoubleExperienceJob.class).build();
        final CronTrigger doubleExpTrigger = newTrigger().withSchedule(cronSchedule("0 0 * ? * *")).build();
        scheduler.scheduleJob(doubleExpJob, doubleExpTrigger);
        System.out.println("Double Experience Job: " + doubleExpTrigger.getNextFireTime());

        // OpenAI Relay Job - Fired every 30 minutes for cleanup and monitoring
        final JobDetail openAIRelayJob = JobBuilder.newJob(OpenAIRelayJob.class).build();
        final CronTrigger openAIRelayTrigger = newTrigger().withSchedule(cronSchedule("0 */30 * ? * *")).build();
        scheduler.scheduleJob(openAIRelayJob, openAIRelayTrigger);
        System.out.println("OpenAI Relay Job: " + openAIRelayTrigger.getNextFireTime());

        scheduler.start();
    }
}
