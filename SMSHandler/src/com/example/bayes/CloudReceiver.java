package com.example.bayes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

public class CloudReceiver {
	static String POSFILEPATH="d:/uploadfile/good";
	static String NEGFILEPATH="d:/uploadfile/bad";
	static StringTokenizer mStringTokenizer;
	static String tString;
	static String tidString;
	
	public static void startLearing(String received){ 
		boolean body = false;
		mStringTokenizer = new StringTokenizer(received,"|");
		System.out.println(mStringTokenizer);
		while(mStringTokenizer.hasMoreTokens()){
			if(body){
				
				if(tidString.substring(0, 1).equals("1")){
					tString = mStringTokenizer.nextToken();
					posFile(tidString, tString);
					body = false;
				}else{
					tString = mStringTokenizer.nextToken();
					negFile(tidString, tString);
					body = false;
				}
			}else {
				tidString = mStringTokenizer.nextToken();
				body = true;
			}
		}
	}
	
	
	public static void posFile(String id,String content) {
		File file=new File(POSFILEPATH+"/"+id+".txt");
		try {
			FileOutputStream mFileOutputStream=new FileOutputStream(file);
			mFileOutputStream.write(content.getBytes());
			mFileOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void negFile(String id,String content) {
		File file=new File(NEGFILEPATH+"/"+id+".txt");
		try {
			FileOutputStream mFileOutputStream=new FileOutputStream(file);
			mFileOutputStream.write(content.getBytes());
			mFileOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
