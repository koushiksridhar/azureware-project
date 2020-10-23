package com.theparkcorp.azureware3;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by allvac on 4/13/2017.
 */

public class IntroManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    public IntroManager(Context context){

        this.context = context;
        pref=context.getSharedPreferences("first", 0);
        editor = pref.edit();
    }

    public void setFirst(boolean isFirst){

        editor.putBoolean("Check", isFirst);
        editor.commit();
    }

    public boolean Check(){

        return pref.getBoolean("Check", true);
    }
}

