package com.yjtse.web;

import com.yjtse.dto.SocketServerUtils;
import com.yjtse.service.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class DeviceSocketController {

    @Autowired
    private SocketService socketService;

    public static void main(String[] args) {
        SocketServerUtils socketServerUtils = SocketServerUtils.getInstance();
        socketServerUtils.init(9000);

        socketServerUtils.setMessageListener(new SocketServerUtils.MessageListener() {

            @Override
            public void OnSendSuccess() {
                // TODO Auto-generated method stub
                System.out.println("发送完成");
            }

            @Override
            public void OnSendFail() {
                // TODO Auto-generated method stub
                System.out.println("发送失败");
            }

            int j = 0;

            @Override
            public void OnReceiveSuccess(String message) {
                // TODO Auto-generated method stub
                System.out.println("端口：" + SocketServerUtils.port + "第" + (j++) + "次接收到信息:" + message);
            }

            @Override
            public void OnReceiveFail() {
                // TODO Auto-generated method stub
                System.out.println("接收信息失败");
            }
        });
    }

}
