package com.yjtse.web;

import com.yjtse.dto.SocketServerUtils;

public class TestMain {
    public static void main(String[] args) {
        SocketServerUtils socketServerUtils = SocketServerUtils.getInstance();
        socketServerUtils.init(9000);

        socketServerUtils.setMessageListener(new SocketServerUtils.MessageListener() {

            @Override
            public void OnSendSuccess() {
                // TODO Auto-generated method stub

            }

            @Override
            public void OnSendFail() {
                // TODO Auto-generated method stub

            }

            int j = 0;

            @Override
            public void OnReceiveSuccess(String message) {
                // TODO Auto-generated method stub
                System.out.println("9000�˿����ݣ�" + (j++) + "****" + message);
            }

            @Override
            public void OnReceiveFail() {
                // TODO Auto-generated method stub

            }
        });
    }

}
