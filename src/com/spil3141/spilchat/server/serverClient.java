package com.spil3141.spilchat.server;

import java.net.InetAddress;

public class serverClient {

    public String nickname;
    public InetAddress address;
    public int port;
    private final int ID;
    public int attempt = 0;

    public serverClient(String nickname, InetAddress address, int port, final int ID){
    this.nickname = nickname;
    this.address = address;
    this.port = port;
    this.ID = ID;
    }

    public int getID(){
        return this.ID;
    }
}
