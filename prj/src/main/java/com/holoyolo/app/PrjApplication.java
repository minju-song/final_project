package com.holoyolo.app;

import org.mybatis.spring.annotation.MapperScan;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.holoyolo.app.accBookHistory.service.ApiJob;

@SpringBootApplication
@MapperScan(basePackages = "com.holoyolo.app.**.mapper")
public class PrjApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrjApplication.class, args);
		
		System.out.println("테스트");
		
        // JobDetail 생성
        JobDetail job = JobBuilder.newJob(ApiJob.class)
                .withIdentity("apijobtest18")
                .build();

        // Trigger 생성 (매일 오전 1시에 실행)
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("apitriggertest18")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever())
                .build();

        // Scheduler 생성 및 실행
        Scheduler scheduler;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
