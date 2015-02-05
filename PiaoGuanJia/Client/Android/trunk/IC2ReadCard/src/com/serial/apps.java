package com.serial;

import java.io.IOException;

import android.util.Log;

public class apps {
	static public native int Serialread(byte[] data,byte[] samid);
   
	static{
		System.loadLibrary("Serial_apps");       
    
	}    
}                          