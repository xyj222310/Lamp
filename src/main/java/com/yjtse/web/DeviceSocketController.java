package com.yjtse.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjtse.dto.SocketServerUtils;
import com.yjtse.entity.Socket;
import com.yjtse.service.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Controller
public class DeviceSocketController {

    @Autowired
    private SocketService socketService;

//    public DeviceSocketController(SocketService socketService) {
//        this.socketService = socketService;
//    }

    @PostConstruct
    public void listener() {
        SocketServerUtils socketServerUtils = SocketServerUtils.getInstance();
        socketServerUtils.init(SocketServerUtils.port);
        System.out.println("listener on,listen to port " + SocketServerUtils.port + "---------------------------");
        socketServerUtils.setConnectListener(new SocketServerUtils.ConnectListener() {

            @Override
            public void OnConnectSuccess() {
                System.out.println("connection complete---------------------------");

            }

            @Override
            public void OnConnectFail() {
                System.out.println("connect failed---------------------------");
            }
        });

        socketServerUtils.setMessageListener(new SocketServerUtils.MessageListener() {

            @Override
            public void OnSendFail() {
                // TODO Auto-generated method stub
                System.out.println("send failed");
//                socketServerUtils.Dissocket();
            }

            @Override
            public void OnSendSuccess() {
                // TODO Auto-generated method stub
                System.out.println("send Completed---------------------------");
            }

            int j = 0;

            @Override
            public void OnReceiveSuccess(String message) {
                // TODO Auto-generated method stub
                System.out.println("port:" + SocketServerUtils.port + "\nThe" + (j++) + "time receiving info:" + message);
                System.out.println("ready to query DB and return data to client---------------------------");
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    Socket socket = objectMapper.readValue(message, com.yjtse.entity.Socket.class);
                    if (socket != null && socket.getSocketId() != null) {
//                        socketService.findById(socket.getSocketId());
                        socketServerUtils.SendDataToSensor(
                                socketService.findById(socket.getSocketId()).getData().getStatus());
                    }
                } catch (IOException e) {
                    socketServerUtils.SendDataToSensor("Data resolve Failed！");
//                    socketServerUtils.Dissocket();
                    e.printStackTrace();
                }
            }

            @Override
            public void OnReceiveFail() {
                // TODO Auto-generated method stub
                System.out.println("receive failed---------------------------");
            }
        });
    }
}


