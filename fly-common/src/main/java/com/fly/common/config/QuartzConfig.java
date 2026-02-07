package com.fly.common.config;

import org.quartz.spi.JobFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.io.IOException;

/**
 * Quartz配置类
 * 尽管Spring Boot自动配置了大部分Quartz设置，但显式配置可以提供更多控制，
 * 例如自定义JobFactory以支持依赖注入。
 */
@Configuration
public class QuartzConfig {

    /**
     * 配置JobFactory，使其支持Spring的依赖注入
     */
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    /**
     * 配置SchedulerFactoryBean
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(jobFactory);

        // 设置调度器名称
        factory.setSchedulerName("FlyAdminScheduler");

        // 覆盖已存在的任务
        factory.setOverwriteExistingJobs(true);

        // 延迟启动，等待数据库初始化完成
        factory.setStartupDelay(2);

        return factory;
    }

    /**
     * 内部类：支持自动注入的JobFactory
     */
    public static class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory {

        private transient ApplicationContext applicationContext;

        public void setApplicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        @Override
        protected Object createJobInstance(org.quartz.spi.TriggerFiredBundle bundle) throws Exception {
            final Object job = super.createJobInstance(bundle);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(job);
            return job;
        }
    }
}
