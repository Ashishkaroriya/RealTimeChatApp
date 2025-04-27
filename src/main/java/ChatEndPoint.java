import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat")
public class ChatEndPoint {
    private static final Map<Session, String> users = new ConcurrentHashMap<>();
    private static final List<String> chatHistory = new ArrayList<>();
    private static final int MAX_HISTORY = 100;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @OnOpen
    public void onOpen(Session session) throws IOException {
        users.put(session, "User" + new Random().nextInt(999));
        broadcastSystem("New user connected. Total: " + users.size());
        sendUserList();
        sendHistory(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        if (message == null || message.trim().isEmpty()) return;

        String nickname = users.get(session);

        if (message.startsWith("/typing")) {
            broadcastTyping(nickname);
            return;
        }

        if (message.startsWith("/name ")) {
            String newName = message.substring(6).trim();
            if (newName.isEmpty()) {
                sendSystem(session, "Nickname cannot be empty.");
                return;
            }
            if (users.containsValue(newName)) {
                sendSystem(session, "Nickname already in use.");
                return;
            }
            String oldName = users.get(session);
            users.put(session, newName);
            broadcastSystem(oldName + " changed name to " + newName);
            sendUserList();
            return;
        }

        if (message.startsWith("/w ")) {
            String[] parts = message.split(" ", 3);
            if (parts.length < 3) {
                sendSystem(session, "Usage: /w <nickname> <message>");
                return;
            }
            String target = parts[1];
            String privateMsg = parts[2];
            boolean found = false;
            for (Map.Entry<Session, String> entry : users.entrySet()) {
                if (entry.getValue().equals(target)) {
                    sendPrivate(entry.getKey(), nickname, privateMsg);
                    sendPrivate(session, nickname, privateMsg); // echo to sender
                    found = true;
                    break;
                }
            }
            if (!found) {
                sendSystem(session, "User not found: " + target);
            }
            return;
        }

        // Regular message
        String time = getTime();
        String formatted = nickname + ": " + message;
        chatHistory.add(formatted);
        if (chatHistory.size() > MAX_HISTORY) {
            chatHistory.remove(0);
        }
        broadcastMessage("message", nickname, message, time);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String name = users.get(session);
        users.remove(session);
        broadcastSystem(name + " left the chat. Users: " + users.size());
        sendUserList();
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }

    private void sendHistory(Session session) throws IOException {
        for (String msg : chatHistory) {
            String[] parts = msg.split(":", 2);
            if (parts.length == 2) {
                sendJson(session, "message", parts[0], parts[1].trim(), getTime());
            }
        }
    }

    private void broadcastMessage(String type, String sender, String message, String time) throws IOException {
        String json = buildJson(type, sender, message, time);
        for (Session s : users.keySet()) {
            s.getBasicRemote().sendText(json);
        }
    }

    private void broadcastSystem(String message) throws IOException {
        String json = buildJson("system", "", message, getTime());
        for (Session s : users.keySet()) {
            s.getBasicRemote().sendText(json);
        }
    }

    private void broadcastTyping(String user) throws IOException {
        String json = buildJson("typing", user, "", null);
        for (Session s : users.keySet()) {
            s.getBasicRemote().sendText(json);
        }
    }

    private void sendUserList() throws IOException {
        String userListJson = "{ \"type\": \"users\", \"users\": ["
                + String.join(",", users.values().stream().map(n -> "\"" + n + "\"").toList())
                + "] }";
        for (Session s : users.keySet()) {
            s.getBasicRemote().sendText(userListJson);
        }
    }

    private void sendSystem(Session session, String message) throws IOException {
        sendJson(session, "system", "", message, getTime());
    }

    private void sendPrivate(Session session, String sender, String message) throws IOException {
        sendJson(session, "system", sender, "[Private] " + message, getTime());
    }

    private void sendJson(Session session, String type, String sender, String message, String time) throws IOException {
        session.getBasicRemote().sendText(buildJson(type, sender, message, time));
    }

    private String buildJson(String type, String sender, String message, String time) {
        return "{"
                + "\"type\":\"" + type + "\","
                + "\"sender\":\"" + escapeJson(sender) + "\","
                + "\"message\":\"" + escapeJson(message) + "\""
                + (time != null ? ",\"time\":\"" + time + "\"" : "")
                + "}";
    }

    private String getTime() {
        return LocalTime.now().format(TIME_FORMATTER);
    }

    private String escapeJson(String str) {
        return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }
}
