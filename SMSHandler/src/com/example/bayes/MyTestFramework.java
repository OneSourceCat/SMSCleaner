package com.example.bayes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyTestFramework {
	
	private String testFilePath="D:/���Ų��Լ�/�������źϼ�";
	
	/**
	 * �õ�ѵ�����е��ļ�·��
	 * @return  string[]�ļ�·������
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
		return content.trim();
	}
	
	/**
	 * ����һ�����Թ���
	 * @return
	 */
	public void TestFactory(){
		int good = 0;
		int bad=0;
		File file = new File("D:/���Ų��Լ�/���Խ��/excel����/�������źϼ�Ȩֵ7��ֵ1.txt");
		FileOutputStream fos = null;
		String content="";
		try {
			 fos = new FileOutputStream(file);//�ļ������
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
			String classify = filtersystem.SMSFilter(content); //������ 
			System.out.println("final result==="+classify);
			if(classify.equals("��������")){
				good++;
			}else{
				bad++;
			}
			
			String res = "��"+i+"�� : "+ filepath[i]+"  ���Ϊ�� "+ classify +"  ���ݣ�"+fileContent[i]+"\r\n";
			System.out.println("final result==="+classify);
			try {
				fos.write(res.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String write = "���� �� "+good+"\r\n"+"���� �� "+bad;
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




























