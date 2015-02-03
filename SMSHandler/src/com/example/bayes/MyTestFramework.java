package com.example.bayes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyTestFramework {
	
	private String testFilePath="D:/短信测试集/垃圾短信合集";
	
	/**
	 * 得到训练集中的文件路径
	 * @return  string[]文件路径数组
	 */
	public  String[] getFilePath(){
		File file = new File(testFilePath);
		String[] filename =  file.list();
		String[] filepath = new String[filename.length]; 
		for (int i=0;i<filepath.length;i++) {
			filepath[i] = testFilePath+"/"+filename[i];
		}
		return filepath;
	}
	
	/**
	 * 根据文件的路径得到文件的内容
	 * @param 文件路径
	 * @return 文本内容
	 */
	public String getFileContent(String path){
		String content = null;
		try{
			//文件操作
			File file = new File(path);
			InputStream is = new FileInputStream(file);
			int len = 0;
			byte[] bytes = new byte[1024];
			while((len=(is.read(bytes)))!=-1){
			    content = new String(bytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return content.trim();
	}
	
	/**
	 * 这是一个测试工厂
	 * @return
	 */
	public void TestFactory(){
		int good = 0;
		int bad=0;
		File file = new File("D:/短信测试集/测试结果/excel测试/垃圾短信合集权值7阈值1.txt");
		FileOutputStream fos = null;
		String content="";
		try {
			 fos = new FileOutputStream(file);//文件输出流
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		String[] filepath = getFilePath();
		String[] fileContent = new String[filepath.length];
		
		for(int i=0;i<fileContent.length;i++){
			fileContent[i] = getFileContent(filepath[i]).trim();
			content = fileContent[i];
			SMSFilterSystem filtersystem = new SMSFilterSystem();
			String classify = filtersystem.SMSFilter(content); //分类结果 
			System.out.println("final result==="+classify);
			if(classify.equals("正常短信")){
				good++;
			}else{
				bad++;
			}
			
			String res = "第"+i+"个 : "+ filepath[i]+"  结果为： "+ classify +"  内容："+fileContent[i]+"\r\n";
			System.out.println("final result==="+classify);
			try {
				fos.write(res.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String write = "正常 ： "+good+"\r\n"+"垃圾 ： "+bad;
		try {
			fos.write(write.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyTestFramework test = new MyTestFramework();
		test.TestFactory();
	}

}




























