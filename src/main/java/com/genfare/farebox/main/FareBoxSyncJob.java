package com.genfare.farebox.main;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
public class FareBoxSyncJob {

	public static void main(String[] args) throws Exception
	{
	JobDetail job = new JobDetail();
	job.setName("cronScheduler");
	job.setJobClass(FareBox.class);
	job.setGroup("farebox");    	
	CronTrigger trigger = new CronTrigger();
	trigger.setName("cronScheduler");
	trigger.setCronExpression("0/50 * * * * ?");
	trigger.setGroup("farebox"); 
	Scheduler scheduler = new StdSchedulerFactory().getScheduler();
	scheduler.start();
	scheduler.scheduleJob(job, trigger);
	}
}
