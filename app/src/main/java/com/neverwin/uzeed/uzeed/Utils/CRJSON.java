package com.neverwin.uzeed.uzeed.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CRJSON extends JSONObject {

    public CRJSON(String str) throws JSONException {
        super(str);
    }

    public static JSONObject newObj(String str) {
        try {
            return new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONArray newArray(String str) {
        try {
            return new JSONArray(str);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getJSON(String[] path, JSONObject obj) {
        try {
            JSONObject result = obj;
            int i;
            for (i = 0; i < path.length; i++) result = (JSONObject) result.getJSONObject(path[i]);
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getString(String[] path, JSONObject obj) {
        try {
            JSONObject result = obj;
            int i = 0;
            for (i = 0; i < path.length - 1; i++) result = result.getJSONObject(path[i]);
            return result.getString(path[i]);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getInt(String name, JSONObject obj) {
        try {
            return obj.getInt(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static double getDouble(String name, JSONObject obj) {
        try {
            return obj.getDouble(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static boolean getBoolean(String[] path, JSONObject obj) {
        String res = getString(path, obj);
        return Boolean.parseBoolean(res);
    }
}
