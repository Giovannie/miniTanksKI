package miniTanks.miniTankKIMS;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



public class Converter {
    
    private final Gson gson;

    public Converter() {
        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
    }
    


    public String toString(final GameMessage message) {
        String out = gson.toJson(message);
        out = out.replace(",\"shoot\":false", "");
        return out;
    }

    public ServerMessage toObject(final String string) {

        return (ServerMessage) gson.fromJson(string, ServerMessage.class);
    }
}
