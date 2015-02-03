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
 * 用来处理用户上传的短信  保存在web工程下的uploadfile下
 * @author chongrui
 *
 */
public class UploadServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//注意导包的时候用common包
		//1.构造工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//设置上传文件的缓冲区大小、临时文件保存的目录
		factory.setSizeThreshold(1024*1024*8);
		File repository = new File(getServletContext().getRealPath("/WEB-INF/tmp"));
		factory.setRepository(repository);
		
		
		//2.获取解析器
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		
		//3.对请求体内容进行解析
		try {
			List<FileItem> items = upload.parseRequest(request);
			
			//4.遍历fileitem 
			for (FileItem fileItem : items) {
				
				//5.判断每个fileitem是否是文件上传
				if(fileItem.isFormField()){
					//不是上传文件
					String name = fileItem.getFieldName();
					String value = fileItem.getName();
					System.out.println("普通form项-----"+name+"::"+value);
				}else{
					//是上传文件
					//获得文件名
					String filename = fileItem.getName();
					//获取文件主体
					InputStream is = fileItem.getInputStream();
					System.out.println("文件上传项-----"+filename);
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
					fileItem.delete(); //删除临时文件
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

