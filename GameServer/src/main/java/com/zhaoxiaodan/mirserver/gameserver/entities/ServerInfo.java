package com.zhaoxiaodan.mirserver.gameserver.entities;

import javax.persistence.*;

@Entity
public class ServerInfo {
    @Id
    @GeneratedValue
    public int id;
    @OrderColumn
    @Column(unique = true)
    public String name;
    public String loginServerIp;
    public int loginServerPort;
    public String gameServerIp;
    public int gameServerPort;
}
