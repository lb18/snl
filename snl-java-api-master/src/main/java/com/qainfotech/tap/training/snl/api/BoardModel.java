package com.qainfotech.tap.training.snl.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

/**
 *
 * @author Ramandeep <RamandeepSingh@QAInfoTech.com>
 */
public class BoardModel {
    
    private static JSONObject getStep(Integer number, Integer type, Integer target){
        return new JSONObject("{\"number\":"+number+",\"type\":"+type+", \"target\":"+target+"}");
    }

    public static void init(UUID uuid) throws FileNotFoundException, UnsupportedEncodingException{
        JSONArray steps = new JSONArray();
        for(int position = 0; position <=100; position++){
            steps.put(position, getStep(position, 0, position));
        }
        // snakes
        steps.put(99, getStep(99, 1, 3));
        steps.put(93, getStep(93, 1, 67));
        steps.put(55, getStep(55, 1, 13));
        steps.put(70, getStep(70, 1, 32));
        steps.put(23, getStep(23, 1, 7));
        // ladders
        steps.put(2, getStep(2, 2, 24));
        steps.put(11, getStep(11, 2, 33));
        steps.put(25, getStep(25, 2, 85));
        steps.put(37, getStep(37, 2, 61));
        steps.put(68, getStep(68, 2, 90));
        steps.put(79, getStep(79, 2, 97));
        
        JSONObject data = new JSONObject();
        data.put("players", new JSONArray());
        data.put("turn", 0);
        data.put("steps", steps);
        
        PrintWriter writer = new PrintWriter(uuid.toString() + ".board", "UTF-8");
        writer.println(data.toString(2));
        writer.close();
    }
    
    public static void save(UUID uuid, JSONObject content)
            throws FileNotFoundException, UnsupportedEncodingException{
        PrintWriter writer = new PrintWriter(uuid.toString() + ".board", "UTF-8");
        writer.println(content.toString(2));
        writer.close();
    }
    
    public static JSONObject data(UUID uuid) throws IOException{
        return new JSONObject(new String(
                Files.readAllBytes(Paths.get(uuid.toString() + ".board"))));
    }
}
