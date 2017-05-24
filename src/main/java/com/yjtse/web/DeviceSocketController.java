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


    public Socket socket = null;

    @PostConstruct
    public void listener() {
        SocketServerUtils socketServerUtils = SocketServerUtils.getInstance();
        try {
            socketServerUtils.init(SocketServerUtils.port);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connection init failed! " + SocketServerUtils.port + "---------------------------");
        }
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
                System.out.println("send failed");
            }

            @Override
            public void OnSendSuccess() {
                System.out.println("send Completed---------------------------");
            }

            int j = 0;

            @Override
            public void OnReceiveSuccess(String message) {
                System.out.println("port:" + SocketServerUtils.port + "\nThe" + (j++) + "time receiving info:" + message);
                System.out.println("ready to query DB and return data to client---------------------------");
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    socket = objectMapper.readValue(message, com.yjtse.entity.Socket.class);
                    if (socket != null && socket.getSocketId() != null) {
                        Socket result = socketService.findById(socket.getSocketId()).getData();
                        socketServerUtils.SendDataToSensor(
                                result.getStatus());
                        result.setAvailable("1");
                        socketService.updateSocket(result);
                    }
                } catch (IOException e) {
                    socketServerUtils.SendDataToSensor("Data resolve Failed！");
                    SocketServerUtils.getInstance().getMessageListener().OnSendFail();
                    e.printStackTrace();
                }
            }

            @Override
            public void OnReceiveFail() {
                System.out.println("receive failed---------------------------");
                if (socket != null && socket.getSocketId() != null) {
                    Socket result = socketService.findById(socket.getSocketId()).getData();
                    result.setAvailable("-1");
                    socketService.updateSocket(result);
                }
            }
        });
    }
}


