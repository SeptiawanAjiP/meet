package com.applozic.mobicomkit.uiwidgets.session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Septiawan Aji Pradan on 3/8/2017.
 */

public class SessionManager {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setDeviceToken(String tokenDevice){
        editor.putString("device",tokenDevice);
        editor.commit();
    }

    public String getDeviceToken(){
        String tokenDevice = sharedPreferences.getString("device",null);
        return tokenDevice;
    }

    public void setUserId(String userId){
        editor.putString("user_id",userId);
        editor.commit();
    }

    public String getUserId(){
        String userId = sharedPreferences.getString("user_id",null);
        return userId;
    }

}
