package com.yjtse.service;

import com.yjtse.dao.AppointmentDao;
import com.yjtse.dao.BookDao;
import com.yjtse.dto.AppointExecution;
import com.yjtse.entity.Appointment;
import com.yjtse.entity.Book;
import com.yjtse.enums.AppointStateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yjtse on 2017/4/5.
 */
@Service
public class BookService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 注入Service依赖
    @Autowired
    private BookDao bookDao;

    @Autowired
    private AppointmentDao appointmentDao;


    public Book getById(long bookId) {
        return bookDao.queryById(bookId);
    }


    public List<Book> getList() {
        return bookDao.queryAll(0, 1000);
    }


    @Transactional
    /**
     * 使用注解控制事务方法的优点： 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作，RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
     */
    public AppointExecution appoint(long bookId, long studentId) {
        try {
            // 减库存
            int update = bookDao.reduceNumber(bookId);
            if (update <= 0) {// 库存不足
                return new AppointExecution(bookId, AppointStateEnum.NO_NUMBER);
            } else {
                // 执行预约操作
                int insert = appointmentDao.insertAppointment(bookId, studentId);
                if (insert <= 0) {// 重复预约
                    return new AppointExecution(bookId, AppointStateEnum.REPEAT_APPOINT);
                } else {// 预约成功
                    Appointment appointment = appointmentDao.queryByKeyWithBook(bookId, studentId);
                    return new AppointExecution(bookId, AppointStateEnum.SUCCESS, appointment);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 所有编译期异常转换为运行期异常
            return new AppointExecution(bookId, AppointStateEnum.INNER_ERROR);
        }
    }

}
