package com.fly.business.modules.quartz.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务调度表 sys_quartz_job
 */
@Data
@TableName("sys_quartz_job")
public class SysQuartzJob implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableId(value = "job_id", type = IdType.AUTO)
    private Long jobId;

    /**
     * 任务名称
     */
    @TableField("job_name")
    private String jobName;

    /**
     * 任务组名
     */
    @TableField("job_group")
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    @TableField("invoke_target")
    private String invokeTarget;

    /**
     * cron执行表达式
     */
    @TableField("cron_expression")
    private String cronExpression;

    /**
     * cron计划策略
     */
    @TableField("misfire_policy")
    private String misfirePolicy = "0";

    /**
     * 是否并发执行（0允许 1禁止）
     */
    @TableField("concurrent")
    private String concurrent;

    /**
     * 状态（0正常 1暂停）
     */
    @TableField("job_status")
    private String jobStatus;

    /**
     * 创建者
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新者
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime;

    /**
     * 备注信息
     */
    @TableField("remark")
    private String remark;

    /**
     * 任务执行时调用的方法参数，非数据库字段
     */
    @TableField(exist = false)
    private String methodParams;
}
