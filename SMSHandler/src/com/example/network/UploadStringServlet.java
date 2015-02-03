package com.example.network;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.bayes.CloudReceiver;

@SuppressWarnings("serial")
public class UploadStringServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stubo
		doPost(request, response);
		
	}

	
	@SuppressWarnings("static-access")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CloudReceiver cloudReceiver = new CloudReceiver();
		String strings = request.getParameter("strings");
		System.out.println(strings);
		if(strings!=null){
			cloudReceiver.startLearing(strings);
		}
	}

}
