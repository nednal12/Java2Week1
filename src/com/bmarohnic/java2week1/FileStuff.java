/*
 * project		Java2Week1
 * 
 * package		com.bmarohnic.java2week1
 * 
 * @author		Brent Marohnic
 * 
 * date			Sep 7, 2013
 */
package com.bmarohnic.java2week1;

import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class FileStuff.
 */
public class FileStuff {
	
	/**
	 * Store string file.
	 *
	 * @param context the context
	 * @param filename the filename
	 * @param content the content
	 * @return the boolean
	 */
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
