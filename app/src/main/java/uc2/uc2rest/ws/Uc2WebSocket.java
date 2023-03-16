package uc2.uc2rest.ws;

import java.io.Closeable;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class Uc2WebSocket implements Closeable  {

    private final OkHttpClient client;
    private final String websocketUrl;
    private Uc2WebSocketListner listener;
    private WebSocket webSocket;
    private boolean isOpen = false;

    public Uc2WebSocket(OkHttpClient client, String websockerUrl)
    {
        this.client = client;
        this.websocketUrl = websockerUrl;
    }

    public void createNewWebSocket(Uc2WebSocketListner listener) {

        Request request = new Request.Builder().url(websocketUrl).build();
        webSocket = client.newWebSocket(request, listener);
        isOpen = true;
    }

    @Override
    public void close() throws IOException {
        isOpen = false;
        final int code = 1000;
        if (listener != null && webSocket != null)
            listener.onClosing(webSocket, code, "");
        if (webSocket != null)
            webSocket.close(code, null);
        if (listener != null && webSocket != null)
            listener.onClosed(webSocket, code, "");
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public boolean isOpen() {
        return isOpen;
    }
}
