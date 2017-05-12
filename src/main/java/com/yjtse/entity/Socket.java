package com.yjtse.entity;


/**
 * Created by yjtse on 2017/4/5.
 */

public class Socket {

    private Integer id;// 自增ID

    private String socketId;// 插座id

    private String socketName;

    private String ownerId; //所属用户id

    private String status; //插座状态

    public Socket(Integer id, String socketId, String socketNmame, String ownerId, String status) {
        this.id = id;
        this.socketId = socketId;
        this.socketName = socketNmame;
        this.ownerId = ownerId;
        this.status = status;
    }

    public Socket(String socketId, String socketNmame, String ownerId, String status) {
        this.socketId = socketId;
        this.socketName = socketNmame;
        this.ownerId = ownerId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public String getSocketNmame() {
        return socketName;
    }

    public void setSocketNmame(String socketNmame) {
        this.socketName = socketNmame;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
