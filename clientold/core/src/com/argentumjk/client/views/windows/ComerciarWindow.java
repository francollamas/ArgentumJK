package com.argentumjk.client.views.windows;

import com.argentumjk.client.actors.Window;

public class ComerciarWindow extends Window {
    public ComerciarWindow() {
        super("Comerciar");
    }

    @Override
    protected void close() {
        super.close();

        getClPack().writeCommerceEnd();
    }
}
