package com.argentumjk.client.web.implementations;

import com.argentumjk.client.connection.ClientPackages;
import com.argentumjk.client.connection.Connection;
import com.argentumjk.client.connection.ServerPackages;

import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Uint8Array;
import org.teavm.jso.websocket.WebSocket;

public class WebConnection implements Connection {
    public static final String IP = "localhost";
    public static final int PORT = 7667;

    private ClientPackages clPack;
    private ServerPackages svPack;

    private WebSocket socket;

    public WebConnection() {
        clPack = new ClientPackages();
        svPack = new ServerPackages();
    }

    @Override
    public ClientPackages getClPack() {
        return clPack;
    }

    @Override
    public ServerPackages getSvPack() {
        return svPack;
    }

    @Override
    public boolean connect() {
        socket = WebSocket.create("ws://" + IP + ":" + PORT);
        socket.setBinaryType("arraybuffer");

        socket.onMessage((e) -> {
            ArrayBuffer buffer = (ArrayBuffer) e.getData();
            byte[] bytes = convertArrayBufferToBytes(buffer);
            svPack.getCola().addLast(bytes);
        });

        socket.onError((e) -> {
            svPack.setLostConnection(true);
        });
        return true;
    }

    private byte[] convertArrayBufferToBytes(ArrayBuffer buffer) {
        Uint8Array uint8Array = Uint8Array.create(buffer);
        byte[] bytes = new byte[uint8Array.getLength()];
        for (int i = 0; i < uint8Array.getLength(); i++) {
            bytes[i] = (byte) uint8Array.get(i);
        }
        return bytes;
    }

    @Override
    public void write() {
        if (socket == null || socket.getReadyState() != 1) return;

        byte[] bytes = getClPack().removeAll();
        if (bytes.length > 0) {
            socket.send(new String(bytes));
        }
    }

    @Override
    public void dispose() {
        socket.close();
    }
}
