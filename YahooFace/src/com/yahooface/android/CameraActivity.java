package com.yahooface.android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CameraActivity extends Activity{

	public static final int NONE = 0;     
    public static final int PHOTOHRAPH = 1;// ����     
    public static final int PHOTOZOOM = 2; // ����     
    public static final int PHOTORESOULT = 3;// ���     
    public static String backstr = null; 
    public static final String IMAGE_UNSPECIFIED = "image/*";   
    private String uploadFilePath = "/mnt/sdcard/temp.JPG";
    private String actionUrl = "http://192.168.0.71:8086/HelloWord/myForm";
    private String newName = "image.jpg";
    private ProgressDialog progressDialog = null;
    private Handler handler = new Handler();
    private String url = null;
    private int type = 0;
    private TextView yahooname;
    Bitmap photo = null;


    ImageView imageView = null;     
    Button button0 = null;     
    Button button1 = null; 
    
    boolean hasphoto = false;
     
    @Override     
    public void onCreate(Bundle savedInstanceState) {     
        super.onCreate(savedInstanceState);    
        setContentView(R.layout.camera);     
        imageView = (ImageView) findViewById(R.id.imageID);     
        button0 = (Button) findViewById(R.id.btn_01);     
        button1 = (Button) findViewById(R.id.btn_02); 
        yahooname = (TextView)findViewById(R.id.yahooname);
        yahooname.setText(Account.getUser());
        button0.setOnClickListener(new OnClickListener() {     
            @Override     
            public void onClick(View v) { 
            	if(!hasphoto)
            	{
//            		Intent intent = new Intent(CameraActivity.this,PersonActivity.class);
//            		startActivity(intent);
	                Intent intent = new Intent(Intent.ACTION_PICK, null);     
	                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);     
	                startActivityForResult(intent, PHOTOZOOM);  
            	}
            	else
            	{
            		//Intent intent = new Intent(CameraActivity.this,PersonActivity.class);
            		//startActivity(intent);
            		type = 1;
            		url = CameraActivity.this.getResources().getString(R.string.serverip)+ CameraActivity.this.getResources().getString(R.string.uploadurl)+"?username="+Account.getUser();
            		beginQuery();
            	}
            }     
        });     
     
        button1.setOnClickListener(new OnClickListener() {     
     
            @Override     
            public void onClick(View v) {  
            	if(!hasphoto)
            	{
	                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);     
	                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));     
	                startActivityForResult(intent, PHOTOHRAPH); 
            	}
            	else
            	{
            		type = 2;
            		url = CameraActivity.this.getResources().getString(R.string.serverip)+ CameraActivity.this.getResources().getString(R.string.uploadurl2)+"?username="+Account.getUser();
            		beginQuery();
            	}
            }     
        }); 

    }     
    private void upload()
    {
		BaseHttpPost.url = url;//
		backstr = BaseHttpPost.Comm(null,Bitmap2Bytes(photo)).toString();
		System.out.println(backstr);
    }
    private byte[] Bitmap2Bytes(Bitmap bm){   
        ByteArrayOutputStream baos = new ByteArrayOutputStream();     
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);     
        return baos.toByteArray();   
       } 
    
    @Override     
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {     
        if (resultCode == NONE)     
            return;     
        // ����     
        if (requestCode == PHOTOHRAPH) 
        {     
            //�����ļ�����·��������ڸ�Ŀ¼��     
            File picture = new File(Environment.getExternalStorageDirectory() + "/temp.jpg"); 
            Log.v("file path",Environment.getExternalStorageDirectory() + "/temp.jpg");
            //startPhotoZoom(Uri.fromFile(picture));
            photo = getBitmapFromUri(Uri.fromFile(picture));
            hasphoto = true;
            button0.setText("Search");
            button1.setText("Tag");
        	imageView.setImageBitmap(photo);
        }     
             
        if (data == null)     
            return;     
             
        // ��ȡ�������ͼƬ     
        if (requestCode == PHOTOZOOM) {
        	photo = getBitmapFromUri(data.getData());
        	imageView.setImageBitmap(photo);
            //startPhotoZoom(data.getData());     
        }     
        // ������     
        if (requestCode == PHOTORESOULT) {     
            Bundle extras = data.getExtras();     
            if (extras != null) {     
                photo = extras.getParcelable("data");     
                ByteArrayOutputStream stream = new ByteArrayOutputStream();     
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0 - 100)ѹ���ļ�     
                imageView.setImageBitmap(photo);     
            }     
     
        }     
        hasphoto = true;
        button0.setText("Search");
        button1.setText("Tag");
        super.onActivityResult(requestCode, resultCode, data);     
    }     
    private Bitmap getBitmapFromUri(Uri uri)
    {
     try
     {
      // ��ȡuri���ڵ�ͼƬ
      Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
      return bitmap;
     }
     catch (Exception e)
     {
      Log.e("[Android]", e.getMessage());
      Log.e("[Android]", "Ŀ¼Ϊ��" + uri);
      e.printStackTrace();
      return null;
     }
    } 
    public void startPhotoZoom(Uri uri) {     
        Intent intent = new Intent("com.android.camera.action.CROP");     
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);     
        intent.putExtra("crop", "true");     
        // aspectX aspectY �ǿ�ߵı���     
        intent.putExtra("aspectX", 1);     
        intent.putExtra("aspectY", 1);     
        // outputX outputY �ǲü�ͼƬ���     
        intent.putExtra("outputX", 64);     
        intent.putExtra("outputY", 64);     
        intent.putExtra("return-data", true);     
        startActivityForResult(intent, PHOTORESOULT);     
    }     

    public void beginQuery()
	{
		progressDialog = ProgressDialog.show(CameraActivity.this, "uploading...", "", true);
		progressDialog.setCancelable(true);
		new Thread(new Runnable(){

        	public void run() {
        	// TODO Auto-generated method stub
        	upload();
        	//mapList=MyAPI.getAllDatas();
        	setListAdapter();
        	progressDialog.dismiss();
        	}}).start();
	}
	public void setListAdapter(){
		handler.post(new Runnable() {
		public void run() {
		showResult();
		//list.setAdapter(saImageItems);
		}
		});
	}

	private void showResult()
	{
		//Ѱ����ת Ȧ�˲����� ��������
		if(type == 1)
		{		
			Intent intent = new Intent(CameraActivity.this,DetectionResultActivity.class);
			//intent.putExtra("backstr", backstr);
			retrieve(backstr);
			startActivity(intent);
			this.finish();
			Toast.makeText(getApplicationContext(), "��鿴ʶ����", Toast.LENGTH_SHORT).show();
		}
		else if( type == 2)
		{
			Intent intent = new Intent(CameraActivity.this,CameraActivity.class);
			//intent.putExtra("backstr", backstr);
			retrieve(backstr);
			startActivity(intent);
			this.finish();
			Toast.makeText(getApplicationContext(), "Ȧ�˳ɹ����뵽Flickrҳ��鿴", Toast.LENGTH_LONG).show();
		}

	}
	void retrieve(String str)
	{
		Account.list = new ArrayList<Person>();
		try {
			JSONArray myJsonArray = new JSONArray(str);
			for(int i=0 ; i < myJsonArray.length() ;i++)
			   {
					Person p = new Person();
				    //��ȡÿһ��JsonObject����
				    JSONObject myjObject = myJsonArray.getJSONObject(i);
				    p.username = myjObject.getString("username");
				    p.about_id = myjObject.getString("about_id");
				    Account.list.add(p);
			   }

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//str = str.replace("[", "");
		//str = str.replace("]", "");
		//String[] persons = str.split("},{");
	}
    /* ��ʾDialog��method */

    private void showDialog(String mess)

    {
      new AlertDialog.Builder(CameraActivity.this).setTitle("Message")
       .setMessage(mess)
       .setNegativeButton("ȷ��",new DialogInterface.OnClickListener()
       {
         public void onClick(DialogInterface dialog, int which)
         {          
         }
       }).show();
    }

	

}
