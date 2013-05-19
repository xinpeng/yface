package com.yahooface.android;

import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
     
public class ImageAdapter extends BaseAdapter{            
        private Context mContext;                     // ����Context    
        private Vector<Integer> mImageIds = new Vector<Integer>();    // ����һ��������ΪͼƬԴ    
        private Vector<Boolean> mImage_bs = new Vector<Boolean>();    // ����һ��������Ϊѡ���������    
             
        private int lastPosition = -1;            //��¼��һ��ѡ�е�ͼƬλ�ã�-1��ʾδѡ���κ�ͼƬ    
        private boolean multiChoose;                //��ʾ��ǰ�������Ƿ������ѡ    
             
        public ImageAdapter(Context c, boolean isMulti){    
                mContext = c;    
                multiChoose = isMulti;    
                     
                // װ����Դ     
                mImageIds.add(R.drawable.img_1);    
                mImageIds.add(R.drawable.img_2);    
                mImageIds.add(R.drawable.img_3);    
                mImageIds.add(R.drawable.img_4);    
                mImageIds.add(R.drawable.img_5);    
                for(int i=0; i<5; i++)    
                        mImage_bs.add(false);    
        }    
             
        @Override 
        public int getCount() {    
                // TODO Auto-generated method stub    
                return mImageIds.size();    
        }    
     
        @Override 
        public Object getItem(int position) {    
                // TODO Auto-generated method stub    
                return position;    
        }    
     
        @Override 
        public long getItemId(int position) {    
                // TODO Auto-generated method stub    
                 return position;    
        }    
     
        @Override 
        public View getView(int position, View convertView, ViewGroup parent) {    
                // TODO Auto-generated method stub    
                ImageView imageView;    
                if (convertView == null)    
                {    
                        imageView = new ImageView(mContext);                // ��ImageView������Դ    
                        imageView.setLayoutParams(new GridView.LayoutParams(50, 50));     // ���ò���ͼƬ    
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);         // ������ʾ��������    
                }    
                else 
                {    
                        imageView = (ImageView) convertView;    
                }    
                imageView.setImageDrawable(makeBmp(mImageIds.elementAt(position),    
                                mImage_bs.elementAt(position)));    
                     
                return imageView;    
        }    
             
        private LayerDrawable makeBmp(int id, boolean isChosen){    
                Bitmap mainBmp = ((BitmapDrawable)mContext.getResources().getDrawable(id)).getBitmap();    
                     
                // ����isChosen��ѡȡ�Թ���ͼƬ    
                Bitmap seletedBmp;    
                if(isChosen == true)    
                        seletedBmp = BitmapFactory.decodeResource(mContext.getResources(),    
                                        R.drawable.btncheck_yes);    
                else 
                        seletedBmp = BitmapFactory.decodeResource(mContext.getResources(),    
                                        R.drawable.btncheck_no);    
                     
                // ��������ͼ    
                Drawable[] array = new Drawable[2];    
                array[0] = new BitmapDrawable(mainBmp);    
                array[1] = new BitmapDrawable(seletedBmp);    
                LayerDrawable la = new LayerDrawable(array);    
                la.setLayerInset(0, 0, 0, 0, 0);    
                la.setLayerInset(1, 0, -5, 60, 45 );    
                        
                return la;    //���ص��Ӻ��ͼ    
        }    
     
        // �޸�ѡ�е�״̬    
        public void changeState(int position){    
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