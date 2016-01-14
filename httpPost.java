package com.ifs.p2p.flow.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.ifs.infra.exception.IFSException;
import com.ifs.infra.info.BaseErrorCode;

public class httpPost {

	
	 public static String getCookie(String url, Map<String, String> params) {
		    String cookie = "";
//	       // ����Ĭ�ϵ�httpClientʵ��.     
	        DefaultHttpClient httpclient = new DefaultHttpClient();  
	        // ����httppost     
	        HttpPost httppost = new HttpPost(url);  
	        // ������������    
	        Set<String> keys = params.keySet();
	        List<NameValuePair> formparams = new ArrayList<NameValuePair>(); 
	        if(!keys.isEmpty()){
	        	for (String key : keys) {
	        		formparams.add(new BasicNameValuePair(key, params.get(key)));
				}
	        }
	       // formparams.add(new BasicNameValuePair("request", jsonString));  
	        UrlEncodedFormEntity uefEntity;  
	        try {  
	            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
	            httppost.setEntity(uefEntity);  
	            System.out.println("executing request " + httppost.getURI());  
	            HttpResponse response = httpclient.execute(httppost);  
	            HttpEntity entity = response.getEntity();  
	            if (entity != null) {  
	                System.out.println("--------------------------------------");  
	                Header[] headers = response.getAllHeaders();
	                for (Header header : headers) {
	                	if("Set-Cookie".equalsIgnoreCase(header.getName())){
	                		cookie = header.getValue().split(";")[0];
	                		break;
	                	}
					}
	            }  
	        } catch (ClientProtocolException e) {  
	            e.printStackTrace();  
	        } catch (UnsupportedEncodingException e1) {  
	            e1.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	        return cookie;
	    }
	
	
	   public static String postWithCookie(String strURL, String params,String cookie) {
//	        System.out.println("�����ַ��" + strURL);
//	        System.out.println("���������" + params);
	        String result = "";
	        try {
	            URL url = new URL(strURL);// ��������
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setDoOutput(true);
	            connection.setDoInput(true);
	            connection.setUseCaches(false);
	            connection.setInstanceFollowRedirects(true);
	            connection.setConnectTimeout(org.nutz.http.Sender.Default_Conn_Timeout);
	            connection.setReadTimeout(org.nutz.http.Sender.Default_Read_Timeout);
	            connection.setRequestMethod("POST"); // ��������ʽ
	            // ���ý������ݵĸ�ʽ
	            connection.addRequestProperty("Content-Type", "application/json"); // ���÷������ݵĸ�ʽ
	            connection.addRequestProperty("Cookie", cookie); 
	            connection.connect();
	            
	            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8����
	            out.append(params);
	            out.flush();
	            out.close();
	            
	            // ��ȡ��Ӧ��
	            int retCode = (int) connection.getResponseCode();

	            if (retCode == HttpURLConnection.HTTP_OK) {
	                // ��ȡ��Ӧ
	                // InputStream is = connection.getInputStream();
	                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
	                StringBuffer responseBuffer = new StringBuffer();
	                String readLine;
	                while ((readLine = reader.readLine()) != null) {
	                    System.out.println("����ֵ��"+readLine);
	                    responseBuffer.append(readLine);
	                }
	                if (responseBuffer.length() > 0) {
	                    // utf-8����
	                    result = responseBuffer.toString();
	                }
	                connection.disconnect();
	            }else {
	                throw new IFSException("HTTP����ʧ�ܣ�", BaseErrorCode.BASE_ERROR);
	            }
	            return result;
	        } catch (Exception e) {
	            throw new IFSException("HTTP����ʧ�ܣ�", BaseErrorCode.BASE_ERROR);
	        }
	    }
	
}
