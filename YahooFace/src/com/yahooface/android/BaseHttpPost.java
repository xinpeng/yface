package com.yahooface.android;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.util.Log;

/**
 * @author LDM 
 * @fileName BaseHttpPost.java
 * @date 2012-9-24 ����9:19:35
 * �ͻ���HTTP POST���󹤾���
 */
public class BaseHttpPost {

	public static String url = "";//�ӿڵ�ַ

	/**
	 * 
	 * @param serviceName�ӿ�����(Ԥ���ģ�û�ÿ���ɾ��)
	 * @param bytes �������д��byte[]��ʽ����(���Խ�ͼƬ��¼��ת�ɳ�byte[])
	 * @return �ϴ��ɹ�ʧ�ܵķ��ر�ʶ(����������û�з��ؿ���ɾ��)
	 */
	public synchronized static String Comm(String serviceName, byte[] bytes) {
		String result = null;
		try {
			/************* ����HttpClient��HttpPost���� **************/
			HttpClient httpClient = getHttpClient();
			HttpPost httpPost = getHttpPost();
			
			/************* �������д������ **************/
			write(httpPost, bytes);
			
			/************* ��ʼִ������ ******************/
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			/***************���տͻ��˷�������״̬**************/
			int code = httpResponse.getStatusLine().getStatusCode();
			if (code == 200) {//200����ɹ�
				HttpEntity he = httpResponse.getEntity();
				//�õ��ķ������ݣ�����᷵��ʲô����������д����˵ģ����û�з��ؿ���ɾ��
				result = EntityUtils.toString(he);
			} else {//����ʧ�ܣ�codeΪ�쳣����
				Log.e("lidm", "������������� ��" + code);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e("lidm", "ClientProtocolException");
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("lidm", "IOException");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("lidm", "Exception");
		}
		return result;
	}

	/**
	 * ʹ��ByteArrayEntity�������д������
	 * @param httpPost
	 * @param bytes
	 */
	private static void write(HttpPost httpPost, byte[] bytes) {
		httpPost.setEntity(new ByteArrayEntity(bytes));
	}

	/**
	 * ��ʱ���õ�text/xml����������ÿ��������޸ģ����Ϻܶ��Լ��ң�
	 * @return
	 */
	private static HttpPost getHttpPost() {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type", "text/xml");
		return httpPost;
	}


	/**
	 * ����HttpClient�������ó���ȡ��ʱ���ȴ���ʱʱ��60��
	 * @return
	 */
	private static HttpClient getHttpClient() {
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60*1000); 
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60*1000);
		return httpClient;
	}
}