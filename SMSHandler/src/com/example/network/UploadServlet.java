package com.example.network;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
/**
 * ���������û��ϴ��Ķ���  ������web�����µ�uploadfile��
 * @author chongrui
 *
 */
public class UploadServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//ע�⵼����ʱ����common��
		//1.���칤��
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//�����ϴ��ļ��Ļ�������С����ʱ�ļ������Ŀ¼
		factory.setSizeThreshold(1024*1024*8);
		File repository = new File(getServletContext().getRealPath("/WEB-INF/tmp"));
		factory.setRepository(repository);
		
		
		//2.��ȡ������
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		
		//3.�����������ݽ��н���
		try {
			List<FileItem> items = upload.parseRequest(request);
			
			//4.����fileitem 
			for (FileItem fileItem : items) {
				
				//5.�ж�ÿ��fileitem�Ƿ����ļ��ϴ�
				if(fileItem.isFormField()){
					//�����ϴ��ļ�
					String name = fileItem.getFieldName();
					String value = fileItem.getName();
					System.out.println("��ͨform��-----"+name+"::"+value);
				}else{
					//���ϴ��ļ�
					//����ļ���
					String filename = fileItem.getName();
					//��ȡ�ļ�����
					InputStream is = fileItem.getInputStream();
					System.out.println("�ļ��ϴ���-----"+filename);
					String uploadpath = getServletContext().getRealPath("/WEB-INF/uploadfile");
					System.out.println(uploadpath);
					OutputStream os = new FileOutputStream(new File(uploadpath+"/"+filename));
					byte[] bytes = new byte[1024];
					int len = 0;
					while((len=is.read(bytes))!=-1){
						os.write(bytes, 0, len);
					}
					is.close();
					os.close();
					fileItem.delete(); //ɾ����ʱ�ļ�
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

