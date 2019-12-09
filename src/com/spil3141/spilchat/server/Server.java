package com.spil3141.spilchat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{
    private List<serverClient> clients = new ArrayList<serverClient>();
    private int port;
    private DatagramSocket socket;
    private Thread run,manage,send,receive;
    private boolean running = false;

    public Server(int port){
        this.port = port;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        run = new Thread(this,"Server Thread");
        run.start();
    }

    @Override
    public void run() {
        running = true;
        System.out.println("Server Started on port: " + port);
//        manageClients();
        receive();
    }

    //Threaded functions
    private void manageClients(){
        manage = new Thread("Manager Thread"){
            @Override
            public void run() {
                while(running){
                    //managing
                }
            }
        };
        manage.start();
    }
    private void receive(){
        receive = new Thread("Receiver Thread") {
            @Override
            public void run() {
                while(running){
                    // constantly receive packets
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data,data.length);
                    try {
                        socket.receive(packet);
                        String msg = new String(packet.getData());
                        process(packet);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        receive.start();
    }

    private void process(DatagramPacket packet){
        String url = new String(packet.getData());
        if(url.startsWith("/c/")){
            int id = UniqueIdentifier.getIdentifier();
            System.out.println(id + " added to clientList ");
            clients.add(new serverClient(url.substring(3,url.length()),packet.getAddress(), packet.getPort(),id));
        }else if(url.startsWith("/m/")){
//            String message = url.substring(3,url.length());
            sendToAll(url);
        }else{
            System.out.println(url);
        }
    }
    private void sendToAll(String msg){
        for(int i=0;i< clients.size();i++){
            serverClient client = clients.get(i);
            send(msg.getBytes(),client.address,client.port);
        }

    }

    private void send(final byte[] DATA, InetAddress address, int port){
        send = new Thread("Send Thread"){
            @Override
            public void run() {
                super.run();DatagramPacket packet = new DatagramPacket(DATA,DATA.length,address,port);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }


}

