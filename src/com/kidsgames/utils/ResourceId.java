package com.kidsgames.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

public class ResourceId {
	public static String DRAWABLE = "drawable"; 

	public static int getResId(String variableName,String type, Context context) {
		return context.getResources().getIdentifier(variableName, 
				type, context.getPackageName());
	}
	
	public static String getJson(Context context, String packName)
	{
		String str = "";
		try{
		   StringBuilder buf=new StringBuilder();
		    InputStream json= null;
		    if (packName.equals("pack1"))
		    	json = context.getAssets().open("logic1.txt");
		    else
		    	json = context.getAssets().open("logic2.txt");
		
		    BufferedReader in=
		        new BufferedReader(new InputStreamReader(json));

		    while ((str=in.readLine()) != null) {
		      buf.append(str);
		    }
		    in.close();
		    return buf.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
