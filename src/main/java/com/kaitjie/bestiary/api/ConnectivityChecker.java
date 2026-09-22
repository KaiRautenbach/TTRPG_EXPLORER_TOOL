package com.kaitjie.bestiary.api;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectivityChecker {

    public boolean isOnline() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("8.8.8.8", 53), 2000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}