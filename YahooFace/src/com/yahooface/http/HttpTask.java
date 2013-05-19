package com.yahooface.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.yahooface.android.Account;
import com.yahooface.android.UpdateActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class HttpTask extends AsyncTask<String, Integer, String> { 
    // �ɱ䳤�������������AsyncTask.exucute()��Ӧ   
    ProgressDialog pdialog;   
    Context  context;
    UpdateActivity activity;
    public HttpTask(UpdateActivity activity,Context context,String title){
    	this.activity = activity;
    	this.context = context;
        pdialog = new ProgressDialog(context, 0);   
        pdialog.setButton("cancel", new DialogInterface.OnClickListener() {   
         public void onClick(DialogInterface dialog, int i) {   
          dialog.cancel();   
         }   
        });   
        pdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {   
         public void onCancel(DialogInterface dialog) { 
        	 pdialog.dismiss();
          //finish();   
         }   
        });   
        pdialog.setCanceledOnTouchOutside(true);
        pdialog.setCancelable(true);   
        pdialog.setMax(100);   
        pdialog.setTitle(title);
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);   
        pdialog.show();   


    }   
    @Override  
    protected String doInBackground(String... params) {   

    	if(params[0].equals("POST"))
    		return HttpPostData(params[1]); 
    	else if(params[0].equals("GET"))
    		return HttpGet(params[1]);
    	else if(params[0].equals("POSTJSON"))
    	{
    		return HttpPostJSON(params[1], params[2]);
    	}
    	return null;

    }   

    @Override  
    protected void onCancelled() {   
        super.onCancelled();   
    }   

    @Override  
    protected void onPostExecute(String result) {   
        // ����HTMLҳ�������   
        //message.setText(result); 
        //Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    	
        pdialog.dismiss();
        activity.update();
    }   

    @Override  
    protected void onPreExecute() {   
        // ����������������������ʾһ���Ի�������򵥴���   
        //message.setText(R.string.task_started);   
    }   

    @Override  
    protected void onProgressUpdate(Integer... values) {   
        // ���½���   
          //System.out.println(""+values[0]);   
          //message.setText(""+values[0]);   
    	//activity.update();
        //pdialog.setProgress(values[0]);   
    }  
    private String HttpGet(String url)
    {
    	try{   

            HttpClient client = new DefaultHttpClient();   
            // params[0]�������ӵ�url   
            HttpGet get = new HttpGet(url);   
            HttpResponse response = client.execute(get);   
            HttpEntity entity = response.getEntity();   
            long length = entity.getContentLength();   
            InputStream is = entity.getContent();   
            String s = null;   
            if(is != null) {   
                ByteArrayOutputStream baos = new ByteArrayOutputStream();   

                byte[] buf = new byte[128];   

                int ch = -1;   

                int count = 0;   

                while((ch = is.read(buf)) != -1) {   

                   baos.write(buf, 0, ch);   

                   count += ch;   

                   if(length > 0) {   
                       // ���֪����Ӧ�ĳ��ȣ�����publishProgress�������½���   
                       publishProgress((int) ((count / (float) length) * 100));   
                   }   

                   // ���߳�����100ms   
                   //Thread.sleep(100);   
                }   
                s = new String(baos.toByteArray()); 
                System.out.println(s);
                Account.newsStr = s;
            }   
            // ���ؽ��   
            return s;   
         } catch(Exception e) {   
            e.printStackTrace();   

         }
    	return null;
    }
    private String HttpPostData(String uri) {
    	try {
    		HttpClient httpclient = new DefaultHttpClient();
    		//String uri = "http://www.yourweb.com";
    		HttpPost httppost = new HttpPost(uri); 
    		//���httpͷ��Ϣ
    		//httppost.addHeader("Authorization", "your token"); //��֤token
    		//httppost.addHeader("Content-Type", "application/json");
    		//httppost.addHeader("User-Agent", "imgfornote");
    		//http post��json���ݸ�ʽ��  {"name": "your name","parentId": "id_of_parent"}
    		//JSONObject obj = new JSONObject();
    		//obj.put("name", "your name");
    		//obj.put("parentId", "your parentid");
    		//httppost.setEntity(new StringEntity(obj.toString()));	
    		HttpResponse response;
    		response = httpclient.execute(httppost);
    		//����״̬�룬����ɹ���������
    		int code = response.getStatusLine().getStatusCode();
    		if (code == 200) { 
    			String rev = EntityUtils.toString(response.getEntity());//����json��ʽ�� {"id": "27JpL~j4vsL0LX00E00005","version": "abc"}
    			Log.v("Post Data Back:",rev);
    			if(rev.length()>0&&rev.contains("images"))
    				Account.buildJsonobj(rev);
    			return rev;
    			//obj = new JSONObject(rev);
    			//String id = obj.getString("id");
    			//String version = obj.getString("version");
    		}
    		} catch (ClientProtocolException e) {	
    		} catch (IOException e) {	
    		} catch (Exception e) {	
    		}
    	return null;
    	}
    private String HttpPostJSON(String uri,String content) {
    	try {
    		HttpClient httpclient = new DefaultHttpClient();
    		//String uri = "http://www.yourweb.com";
    		HttpPost httppost = new HttpPost(uri); 
    		//���httpͷ��Ϣ
    		//httppost.addHeader("Authorization", "your token"); //��֤token
    		//httppost.addHeader("Content-Type", "application/json");
    		//httppost.addHeader("User-Agent", "imgfornote");
    		//http post��json���ݸ�ʽ��  {"name": "your name","parentId": "id_of_parent"}
    		JSONObject obj = new JSONObject();
    		obj.put("avatars", content);
    		httppost.setEntity(new StringEntity(obj.toString()));	
    		HttpResponse response;
    		response = httpclient.execute(httppost);
    		//����״̬�룬����ɹ���������
    		int code = response.getStatusLine().getStatusCode();
    		if (code == 200) { 
    			String rev = EntityUtils.toString(response.getEntity());//����json��ʽ�� {"id": "27JpL~j4vsL0LX00E00005","version": "abc"}
    			Log.v("Post Data Back:",rev);
    			return rev;
    			//obj = new JSONObject(rev);
    			//String id = obj.getString("id");
    			//String version = obj.getString("version");
    		}
    		} catch (ClientProtocolException e) {	
    		} catch (IOException e) {	
    		} catch (Exception e) {	
    		}
    	return null;
    	}
}     

