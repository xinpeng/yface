package com.yahooface.android;

import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yahooface.android.AsyncImageLoader.ImageCallback;

public class ImageAndTextListAdapter extends ArrayAdapter<ImageAndText> {
	private GridView gridView;
	private AsyncImageLoader asyncImageLoader;
	
    private Vector<Drawable> mImageIds = new Vector<Drawable>();    // ����һ��������ΪͼƬԴ    
    public Vector<Boolean> mImage_bs = new Vector<Boolean>();    // ����һ��������Ϊѡ���������    
         
    private int lastPosition = -1;            //��¼��һ��ѡ�е�ͼƬλ�ã�-1��ʾδѡ���κ�ͼƬ    
    private boolean multiChoose = true;                //��ʾ��ǰ�������Ƿ������ѡ    
    private Context mContext;
    private int loadedcount = 0;
    private int totalcount = 0;
    
	public ImageAndTextListAdapter(Context c,Activity activity,
			List<ImageAndText> imageAndTexts, GridView gridView) {
		super(activity, 0, imageAndTexts);
		mContext = c; 
		this.gridView = gridView;
		asyncImageLoader = new AsyncImageLoader();
		totalcount = imageAndTexts.size();
		for(int i=0; i<imageAndTexts.size(); i++)
		{
            mImage_bs.add(false); 
            mImageIds.add(i, null);
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Activity activity = (Activity) getContext();

		// Inflate the views from XML
		View rowView = convertView;
		ViewCache viewCache;
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.imageitem, null);
			viewCache = new ViewCache(rowView);
			rowView.setTag(viewCache);
		} else {
			viewCache = (ViewCache) rowView.getTag();
		}
		ImageAndText imageAndText = getItem(position);

		// Load the image and set it on the ImageView
		String imageUrl = imageAndText.getImageUrl();
		ImageView imageView = viewCache.getImageView();
		imageView.setTag(imageUrl);
		Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl,
				new ImageCallback() {
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl) {
						ImageView imageViewByTag = (ImageView) gridView
								.findViewWithTag(imageUrl);
						if (imageViewByTag != null) {
							imageViewByTag.setImageDrawable(makeBmp(imageDrawable,false));
						}
					}
				});
		if (cachedImage == null) {
			imageView.setImageResource(R.drawable.ic_launcher);
		} else {
			imageView.setImageDrawable(makeBmp(cachedImage,mImage_bs.elementAt(position)));
			if(null==mImageIds.get(position)||mImageIds.get(position)==null)
			{
				mImageIds.add(position,cachedImage);
				Log.d("added position:",String.valueOf(position));
				loadedcount++;
			}
			//Log.d("Index",String.valueOf(position));
			//mImageIds.add(position,cachedImage);
			
		}
		// Set the text on the TextView
		TextView textView = viewCache.getTextView();
		textView.setText(imageAndText.getText());
		return rowView;
	}
	
	private LayerDrawable makeBmp(Drawable cachedImage, boolean isChosen){    
        Bitmap mainBmp = ((BitmapDrawable) cachedImage).getBitmap();
        int width = mainBmp.getWidth()/10;
        int height = mainBmp.getHeight()/10;
        
        // ����isChosen��ѡȡ�Թ���ͼƬ    
        Bitmap seletedBmp;    
        if(isChosen == true)    
                seletedBmp = BitmapFactory.decodeResource(mContext.getResources(),    
                                R.drawable.btncheck_yes);    
        else 
                seletedBmp = BitmapFactory.decodeResource(mContext.getResources(),    
                                R.drawable.btncheck_no);    
        //seletedBmp = seletedBmp.createScaledBitmap(seletedBmp, width, height, false);
        // ��������ͼ    
        Drawable[] array = new Drawable[2];    
        array[0] = new BitmapDrawable(mainBmp);    
        array[1] = new BitmapDrawable(seletedBmp);    
        LayerDrawable la = new LayerDrawable(array);    
        la.setLayerInset(0, 0, 0, 0, 0);    
        la.setLayerInset(1, 0, -5, 70, 70 );    
                
        return la;    //���ص��Ӻ��ͼ    
}
	// �޸�ѡ�е�״̬    
    public void changeState(int position){
    	Log.v("loaded count,when click changestate",String.valueOf(loadedcount));
    		if(loadedcount<totalcount)//��δ��ȫ�����꣬����ѡ��
    			;//return;
            // ��ѡʱ    
            if(multiChoose == true){            
                    mImage_bs.setElementAt(!mImage_bs.elementAt(position), position);     //ֱ��ȡ������         
            }    
            // ��ѡʱ    
            else{                                                 
                    if(lastPosition != -1)    
                            mImage_bs.setElementAt(false, lastPosition);        //ȡ����һ�ε�ѡ��״̬    
                    mImage_bs.setElementAt(!mImage_bs.elementAt(position), position);     //ֱ��ȡ������    
                    lastPosition = position;                //��¼����ѡ�е�λ��    
            }    
            notifyDataSetChanged();         //֪ͨ���������и���    
    }    
}