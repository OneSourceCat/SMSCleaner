package ynu.software.shixun.bayes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ynu.software.shixun.main.MessageActivity;

import android.R.string;
import android.content.Context;
import android.util.Log;


/**
 * 这是一个测试类   实现自动化正确率统计
 * @author Chongrui
 *
 */
public class TestFramework {
	//测试集路径
//	private String testFilePath="e:/testbad";
	public static boolean bayes_enable;
	
	/**
	 * 得到训练集中的文件路径
	 * @return  string[]文件路径数组
	 */
	public static boolean  Isenabled(Context context) {
		if (context == null) return false;
		bayes_enable = context.getSharedPreferences("ynu.software.shixun.main_preferences", 0).getBoolean("enable", true);
		return bayes_enable;
	}
	
//	public  String[] getFilePath(){
//		File file = new File(testFilePath);
//		String[] filename =  file.list();
//		String[] filepath = new String[filename.length]; 
//		for (int i=0;i<filepath.length;i++) {
//			filepath[i] = testFilePath+"/"+filename[i];
//		}
//		return filepath;
//	}
	
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
		return content;
	}
	
	/**
	 * 这是一个过滤系统的测试方法
	 * @return 
	 * @return  结果集
	 */
	public boolean startSystemTest(String messageText){
//		File file = new File("e:\\testbad.txt");
//		int good_count=0; //正常短信计数器
//		int bad_count = 0; //垃圾短信计算器
		try {
//			FileOutputStream fos = new FileOutputStream(file);
//			//获取所有文本信息
//			String[] filepath = getFilePath();
//			String[] filecontent = new String[filepath.length];
//			
//			for(int i=0;i<filepath.length;i++){
				SMSFilterSystem filterSystem = new SMSFilterSystem();
//				filecontent[i] = getFileContent(filepath[i]);
				String classiy = filterSystem.SMSFilter(messageText);
				System.out.println("classify="+classiy);
				if(classiy.equals("正常短信")){
					return true;
//					good_count++;
				}else if(classiy.equals("垃圾短信")){
					return false;
//					bad_count++;
				}
//				String write = "第"+(i+1)+"份   "+filepath[i]+"------辨别为 ： "+classiy+"  内容："+filecontent[i]+"\r\n";
//				fos.write(write.getBytes());
//			}
			
//			String count = "\r\n正常短信条数:"+good_count+"\r\n垃圾短信条数："+bad_count;
//			fos.write(count.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		return false;
		return false;
	}
	
	
	public static boolean MainEnter(String messageText) {
		TestFramework testFramework = new TestFramework();
		return testFramework.startSystemTest(messageText);
	}
}







































