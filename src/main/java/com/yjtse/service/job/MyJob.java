package com.yjtse.service.job;

import com.yjtse.dto.ConnectionPool;
import com.yjtse.entity.Cron;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;

public class MyJob implements Job {

    private int timeout;

    private static int i = 0;

    //调度工厂实例化后，经过timeout时间开始执行调度

    /**
     * 要调度的具体任务
     */
    @Override
//    public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap map = jobExecutionContext.getJobDetail().getJobDataMap();
        if (map != null) {
            Cron cron = (Cron) map.get("cron");
            if (!StringUtils.isEmpty(cron.getSocketId())) {
                /**
                 * 想不出更好的办法了，直接jdbc，用古老的办法执行update吧
                 */
                System.out.println(LocalDateTime.now() + ": job 1 doing something...");
                ConnectionPool connectionPool = new ConnectionPool(
                        "com.mysql.jdbc.Driver",
                        "jdbc:mysql://127.0.0.1:3306/socketlamp?useUnicode=true&characterEncoding=utf8",
                        "root",
                        "xieyingjie927JJ*");
                try {
                    connectionPool.createPool();
                    Connection connection = connectionPool.getConnection();
                    String sql = "update business_socket set " +
                            "`status`= ? " +
                            "WHERE " +
                            "`socket_id`= ?";
                    //参数用?表示，相当于占位符;用mysql的日期函数current_date()来获取当前日期
                    //预编译sql语句
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    //先对应SQL语句，给SQL语句传递参数
                    preparedStatement.setString(1, cron.getStatusTobe());
                    preparedStatement.setString(2, cron.getSocketId());
                    //执行SQL语句
                    preparedStatement.execute();
                    System.out.println(LocalDateTime.now() + ": job 1 set socketStatus done ");
                    String sql2 = "update business_cron set " +
                            "`available`= ? " +
                            "WHERE " +
                            "`id`= ?";
                    PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                    preparedStatement2.setString(1, "0");
                    preparedStatement2.setInt(2, cron.getId());
                    preparedStatement2.execute();
                    System.out.println(LocalDateTime.now() + ": job 1 set timer available done ");
                    connection.close();
                    /**
                     * prepareStatement这个方法会将SQL语句加载到驱动程序conn集成程序中，但是并不直接执行
                     * 而是当它调用execute()方法的时候才真正执行；
                     *
                     * 上面SQL中的参数用?表示，相当于占位符，然后在对参数进行赋值。
                     * 当真正执行时，这些参数会加载在SQL语句中，把SQL语句拼接完整才去执行。
                     * 这样就会减少对数据库的操作
                     */
                    System.out.println(LocalDateTime.now() + ": job 1 done ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}