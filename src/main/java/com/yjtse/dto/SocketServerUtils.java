package com.yjtse.dto;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerUtils {

    final public static int port = 9999;

    final public static int timeOut = 15000;
    /**
     * 连接WIFI的输入流
     */
    private InputStream socketInput = null;
    /**
     * 连接WIFI的输出流
     */
    private OutputStream socketOutput = null;
    /**
     * 连接状态的回调实例
     */
    private ConnectListener connectListener = null;
    /**
     * 消息收发的回调实例
     */
    private MessageListener messageListener = null;
    /**
     * Socket实例
     */
    private Socket socket = null;
    /**
     * 连接是否成功
     */
    private boolean isConnected = false;
    /**
     * 接收数据的结果
     */
    //private String result = "";
    private byte[] result = new byte[1024];

    public interface ConnectListener {
        void OnConnectSuccess();

        void OnConnectFail();
    }

    public interface MessageListener {
        void OnSendSuccess();

        void OnReceiveSuccess(String message);

        void OnSendFail();

        void OnReceiveFail();
    }

    //单例模式
    private static SocketServerUtils instance = null;

    private SocketServerUtils() {
    }

    public static SocketServerUtils getInstance() {

        synchronized (SocketServerUtils.class) {
            if (instance == null) {
                synchronized (SocketServerUtils.class) {
                    if (instance == null) {
                        instance = new SocketServerUtils();
                    }
                }
            }
        }
        return instance;
    }

    public void init(final int portInt) throws IOException {

        ServerSocket serverSocket = new ServerSocket(portInt);
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*
                 * 连接次数计数器
            	 */
                while (true) {
                    try {
                        socket = serverSocket.accept();

                        /**
                         * 设置超时时间
                         */
                        socket.setSoTimeout(timeOut);
                        if (connectListener != null) {
                            connectListener.OnConnectSuccess();
                        }
                        //开一个线程专门处理io
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Recvive(socket);
                            }
                        }).start();


                    } catch (Exception e) {
                        e.printStackTrace();
                        connectListener.OnConnectFail();
                    }
                }
            }
        }).start();

    }

    private void Recvive(Socket socket) {
        try {
            socketInput = socket.getInputStream();
            socketOutput = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (socketInput.read(result) != -1) {
//                if (socket.isClosed()) {
//                    connectListener.OnConnectFail();
//                }
//                try {//连接成功便一直接收数据
//                    if (socketInput.read(result) != -1) {
                String s = new String(result);
                if (messageListener != null) {
                    messageListener.OnReceiveSuccess(s);
                }
                s = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (messageListener != null) {
                messageListener.OnReceiveFail();
            }
        }
    }


    /**
     * 从各传感器接受
     */
    public void RecieveFromDevice() {
        while (true) {
            try {//连接成功便一直接收数据
                DataInputStream dataInputStream = new DataInputStream(socketInput);
                String result = dataInputStream.readUTF();
                if (messageListener != null) {
                    messageListener.OnReceiveSuccess(result.trim());
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (messageListener != null) {
                    messageListener.OnReceiveFail();
                }
//                Dissocket();
            }
        }

    }

    /**
     * 发送数据到传感器
     *
     * @param data
     */
    public void SendDataToSensor(byte[] data) {
        if (socketOutput != null) {
            try {
                socketOutput.write(data);
                if (messageListener != null) {
                    messageListener.OnSendSuccess();
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (messageListener != null) {
                    messageListener.OnSendFail();
                }
//                Dissocket();
            }
        }
    }

    public void SendDataToSensor(String data) {
        if (socketOutput != null) {
            try {
                byte[] sendData = data.getBytes("utf-8");
                socketOutput.write(sendData);
                if (messageListener != null) {
                    messageListener.OnSendSuccess();
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (messageListener != null) {
                    messageListener.OnSendFail();
                }
//                Dissocket();
            }
        }
    }


    /**
     * 销毁Socket连接
     */
    public void Dissocket() {
        if (socket != null) {
            try {
                socket.close();
                socketInput.close();
                socketOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 重启链接
     *
     * @return
     */
    public void restartConn() throws IOException {
        Dissocket();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        init(SocketServerUtils.port);
    }

    public ConnectListener getConnectListener() {
        return connectListener;
    }

    public void setConnectListener(ConnectListener connectListener) {
        this.connectListener = connectListener;
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }
}
