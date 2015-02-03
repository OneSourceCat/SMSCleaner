package com.example.network;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.example.bayes.SMSFilterSystem;

@SuppressWarnings("serial")
public class SMSServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("�ƶ��ж���ʼ��"+System.currentTimeMillis());
		String text = request.getParameter("sms");
		if(text!=null){
			System.out.println("text:"+text);
			//��Ҷ˹����
			SMSFilterSystem filterSystem = new SMSFilterSystem();
			//��ȡ������
			String result = filterSystem.SMSFilter(text);
			System.out.println("result="+result);
			//��������ظ��û�
			OutputStream out = response.getOutputStream();
			out.write(result.getBytes());
			System.out.println("�ƶ��ж�������"+System.currentTimeMillis());
		}
	}

}





















