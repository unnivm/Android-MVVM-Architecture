package com.petstore.model.repository;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.petstore.model.Config;
import com.petstore.model.Pet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class PetRepository {

    /**
     * android logging
     **/
    private final String TAG = PetRepository.class.getName();
    /**
     * volley request queue
     **/
    private RequestQueue queue;
    /**
     * JSON request
     **/
    private JsonObjectRequest jsonObjectRequest;
    /**
     * secret-key header
     **/
    private Map<String, String> apiHeaders;
    /**
     * HTTP request
     **/
    private int method;
    /**
     * Live data object for pets
     **/
    private MutableLiveData<List<Pet>> petList;
    /**
     * Live data object for config
     **/
    private MutableLiveData<Config> config;

    public PetRepository(final Context context, int method) {
        queue = Volley.newRequestQueue(context);
        apiHeaders = new HashMap<>();
        /**
         * secret key
         **/
        String key = "$2b$10$mFJo2Jz.tdullIwL7gGmPeH687LUzMRDtPVOG.LbU3KNlnWVOUR1S";
        apiHeaders.put("secret-key", key);
        this.method = method;
    }

    public void getPetData() {
        petList = new MutableLiveData<>();
        Map<String, String> params = new HashMap<>();
        jsonObjectRequest = new JsonObjectRequest(method, "https://api.jsonbin.io/b/5e216cc05df640720836e76b", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                petList.setValue(parsePetResponse(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(TAG, " error -> " + error.getMessage());
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                return apiHeaders;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public MutableLiveData<List<Pet>> getPetList() {
        return petList;
    }

    public MutableLiveData<Config> getConfig() {
        return config;
    }

    public void getConfigurationData() {
        config = new MutableLiveData<>();
        Map<String, String> params = new HashMap<>();
        jsonObjectRequest = new JsonObjectRequest(method, "https://api.jsonbin.io/b/5e216ecf5df640720836e806", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                config.setValue(parseConfigResponse(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, " error -> " + error.getMessage());
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                return apiHeaders;
            }
        };
        queue.add(jsonObjectRequest);
    }

    /**
     * parse pets json object
     *
     * @param jsonObject
     */
    private List<Pet> parsePetResponse(JSONObject jsonObject) {
        System.out.println(" response :: " + jsonObject);

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("pets");
            int size = jsonArray == null ? 0 : jsonArray.length();
            List<Pet> pets = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                JSONObject petObject = jsonArray.getJSONObject(i);
                Pet pet = new Pet();
                pet.setImageUrl(petObject.getString("image_url"));
                pet.setTitle(petObject.getString("title"));
                pet.setContentUrl(petObject.getString("content_url"));
                pet.setDateAdded(petObject.getString("date_added"));
                pets.add(pet);
            }
            return pets;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * parse configuration json
     *
     * @param jsonObject
     * @return
     */
    private Config parseConfigResponse(JSONObject jsonObject) {
        System.out.println(" response :: " + jsonObject);

        try {
            JSONObject settings = jsonObject.getJSONObject("settings");
            Config config = new Config();
            config.setChatEnabled(settings.getBoolean("isChatEnabled"));
            config.setCallEnabled(settings.getBoolean("isCallEnabled"));
            config.setWorkingHours(settings.getString("workHours"));
            MutableLiveData<Config> data = new MutableLiveData<>();
            data.setValue(config);
            return config;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
