/*=========================
* @author: Rohan Jyoti
* @name: mServlet.java
* @purpose: Communispace Coding Challenge
* Generate simple identicon web application
*
*
* Additional Notes:
* Tomcat/Catalina Structure:
* $CATALINA_HOME/webapps/mServlet/WEB-INF/classes/com/emp/mServlet/
* web.xml details -->
*  servlet-name: mServlet
*  servlet-class: com.emp.mServlet.mServlet
*  url-pattern: /
*
* Running Example accessible on AWS through:
* http://ec2-54-211-223-243.compute-1.amazonaws.com:8080/mServlet/
*=========================*/



package com.emp.mServlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.security.*;

public class mServlet extends HttpServlet
{
	static class mMD5
	{
		private static String toHex(byte[] arr)
		{
			StringBuffer mSB = new StringBuffer();
			for(int i = 0; i < arr.length; i++)
			{
				mSB.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1,3));
			}
			return mSB.toString();
		}
		
		public static String md5Hex(String str)
		{
			try
			{
				MessageDigest mMD = MessageDigest.getInstance("MD5");
				return toHex(mMD.digest(str.getBytes("CP1252")));
			}
			catch (NoSuchAlgorithmException e){}
			catch (UnsupportedEncodingException e){}
			return null;
		}
	}
	
	private String def_output;
	
	public void init() throws ServletException
	{
		def_output = "Simple Identicon Generator Coding Challenge";
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException
	{
		//Overall Steps:
		//1. Get User's IP address, and generate Identicon using Gravitar Service
		//2. Allow option for user to enter email address instead
		//3. Allow option for user to revert back to using IP addr

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h1>" + def_output + "</h1>");
		
		//==========1. Get User's IP Address
		String sys_ipaddr = request.getRemoteAddr();
		String input = "";
		if(sys_ipaddr != null) input = sys_ipaddr;
		//==========
		
		out.println("<h2>Your IP is: " + sys_ipaddr + "</h2>");
		out.println("Servlet will use your IP to generate identicon...<br>");
		out.println("or<br>");

		
		//==========2. Allow user to use email address instead
		out.println("<form>Enter Your Email Address: <input type=\"text\" name=\"email\"><br></form>");

		//Note that there is no sanity checking for right now. User can input whatever string they want.
		String email = request.getParameter("email");
		if(email == null) 
			out.println("No email entered. Using IP Address.<br>");
		 else 
		 {
		 	out.println("Using Email: " + email + "<br>");
		 	input = email;
		}
		//==========

		
		//==========3. Allow user to revert back to using IP Address
		out.println("<form><button type=\"submit\" formaction=\"mServlet\">Use IP</button></form>");
		//==========
		
		
		//==========Compute the hash and generate the identicon
		String hash = mMD5.md5Hex(input.toLowerCase());
		//out.println("<h2>Hash: " + hash + "</h2>");
		
		String gravatar_url = "http://www.gravatar.com/avatar/" + hash + "?f=y&d=identicon&s=512";
		out.println("<img src=" + gravatar_url + " />");
		//==========
		
		
		
		out.close();
		
	}

	public void destroy(){}

}