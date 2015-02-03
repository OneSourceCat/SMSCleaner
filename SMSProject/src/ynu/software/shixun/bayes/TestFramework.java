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
 * ����һ��������   ʵ���Զ�����ȷ��ͳ��
 * @author Chongrui
 *
 */
public class TestFramework {
	//���Լ�·��
//	private String testFilePath="e:/testbad";
	public static boolean bayes_enable;
	
	/**
	 * �õ�ѵ�����е��ļ�·��
	 * @return  string[]�ļ�·������
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
	 * �����ļ���·���õ��ļ�������
	 * @param �ļ�·��
	 * @return �ı�����
	 */
	public String getFileContent(String path){
		String content = null;
		try{
			//�ļ�����
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
	 * ����һ������ϵͳ�Ĳ��Է���
	 * @return 
	 * @return  �����
	 */
	public boolean startSystemTest(String messageText){
//		File file = new File("e:\\testbad.txt");
//		int good_count=0; //�������ż�����
//		int bad_count = 0; //�������ż�����
		try {
//			FileOutputStream fos = new FileOutputStream(file);
//			//��ȡ�����ı���Ϣ
//			String[] filepath = getFilePath();
//			String[] filecontent = new String[filepath.length];
//			
//			for(int i=0;i<filepath.length;i++){
				SMSFilterSystem filterSystem = new SMSFilterSystem();
//				filecontent[i] = getFileContent(filepath[i]);
				String classiy = filterSystem.SMSFilter(messageText);
				System.out.println("classify="+classiy);
				if(classiy.equals("��������")){
					return true;
//					good_count++;
				}else if(classiy.equals("��������")){
					return false;
//					bad_count++;
				}
//				String write = "��"+(i+1)+"��   "+filepath[i]+"------���Ϊ �� "+classiy+"  ���ݣ�"+filecontent[i]+"\r\n";
//				fos.write(write.getBytes());
//			}
			
//			String count = "\r\n������������:"+good_count+"\r\n��������������"+bad_count;
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







































