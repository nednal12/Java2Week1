package com.bmarohnic.java2week1;

import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.util.Log;

public class FileStuff {
	
	public static Boolean storeStringFile(Context context, String filename, String content){
		try{
			FileOutputStream fos = null;
			
			fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			
			fos.write(content.getBytes());
			fos.close();
		} catch (IOException e) {
			Log.e("Write Error", filename);
		}
		
		return true;
	}
	
}
