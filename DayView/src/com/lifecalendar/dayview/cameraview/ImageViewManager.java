package com.lifecalendar.dayview.cameraview;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import android.app.Activity;  
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;  
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.DisplayMetrics;
import android.util.Log;  
import android.view.MotionEvent;  
import android.view.View;  
import android.view.View.OnTouchListener;  
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;  
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ScrollView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
  
public class ImageViewManager {
    /** Called when the activity is first created. */  
      
    AbsoluteLayout mLayoutGroup = null;  
    AbsoluteLayout.LayoutParams layoutParams = null;
    ImageViewWithFrame imageView1;
    ImageViewWithFrame imageView2;
    
    String[] mImageCols = new String[] {
     		MediaStore.Images.Media._ID,
    		MediaStore.Images.Media.DATA,
    		MediaStore.Images.Media.DATE_TAKEN,
    		MediaStore.Images.Media.DESCRIPTION,
    		MediaStore.Images.Media.DISPLAY_NAME,
    		MediaStore.Images.Media.IS_PRIVATE,
    		MediaStore.Images.Media.SIZE,
    		MediaStore.Images.Media.TITLE,
    		MediaStore.Images.Media.ORIENTATION,
    		MediaStore.Images.Media.MIME_TYPE
    };

    Uri Media_ext_uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    Uri Media_int_uri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
    
    public void InitLayout(Context context, AbsoluteLayout absolutelayout, ContentResolver cr, WindowManager wm, String dateStr) {  
        //setContentView(R.layout.main);
                
    	mLayoutGroup = absolutelayout;
    	Cursor imagecursor = RetrieveLocalImagesByDate(cr, dateStr);
    	if (null == imagecursor) {
    		System.out.println("No pictures get for today or maybe program issue");
    		return;
    	}
    	
    	System.out.println("total imagecursors are: " + imagecursor.getCount());
    	
    	ImageViewWithFrame[] imageView = new ImageViewWithFrame[imagecursor.getCount()];
    	int i = 0;
    	int width = wm.getDefaultDisplay().getWidth();
    	int height = wm.getDefaultDisplay().getHeight();
    	
        int picX = 0;
        int picY = 0;
        int lastPicHeight = 0;
        int lastPicWidth = 0;
        int flag = 0;
        int lastY = picY;
        int lastX = picX;
     	while(imagecursor.moveToNext()) {
     		System.out.println("total imagecursors are inside while: " + imagecursor.getCount());
     		Bitmap bitmap = null; 
     		BitmapFactory.Options options = new BitmapFactory.Options();
    		imageView[i] = new ImageViewWithFrame(context);
    		int id = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
    		
    		bitmap = MediaStore.Images.Thumbnails.getThumbnail(cr, Long.parseLong(imagecursor.getString(id)), Images.Thumbnails.MINI_KIND, options);
    		System.out.println("thumbnail get called");
    		
    		Bitmap processedmap = bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 150/bitmap.getHeight(), 150, true);
    		System.out.println("scaledthumbnail create called");
    		
    		picX = GetRandomX(lastX, lastPicWidth, bitmap.getWidth() * 150/bitmap.getHeight(), width);
    		
    		if (picX > (lastX + lastPicWidth/2)) {
    			flag = 1;
    		} else {
    			flag = 0;
    		}
    		
    		lastX = picX;
    	    		
    		picY = GetRandomY(lastY, lastPicHeight, flag);
    		    		
    		lastY = picY;
    		lastPicHeight = bitmap.getHeight();
    		lastPicWidth = bitmap.getWidth() * 150/lastPicHeight;
    		imageView[i].setImagePath(imagecursor.getString(0));
    		SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    		int dateIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
	        Date date = new Date(Long.parseLong(imagecursor.getString(dateIndex)));
	        
	        imageView[i].setText(sfd.format(date).substring(10));
	        imageView[i].setRotateAngel((float)0.0);
	        imageView[i].initData(processedmap);    	
	        
	      //RectF dstRect = imageView1.getBackgroundRect();
	        layoutParams = new AbsoluteLayout.LayoutParams(imageView[i].getBmpWidthWithFrame(), imageView[i].getBmpHeightWithFrame(), picX, picY); 
	        imageView[i].setBackgroundColor(Color.GRAY);
	        //layoutParams = new AbsoluteLayout.LayoutParams((int)(dstRect.right - dstRect.left), (int)(dstRect.bottom - dstRect.top), 30, 30); 
	        mLayoutGroup.addView(imageView[i], layoutParams);
	        imageView[i].setOnTouchListener(touchListener);
    	} 
    }  
    
    private int GetRandomX(int lastX, int lastPicWidth, int picWidth, int screenWidth) {
    	Random ranX = new Random();
    	int picX;
    	if ((lastX + lastPicWidth/2 + picWidth + 50) > screenWidth) {
    		picX = ranX.nextInt(screenWidth - picWidth - 50);
    	} else {
    		picX = ranX.nextInt(screenWidth - lastX - lastPicWidth/2 - picWidth) + lastX + lastPicWidth/2;
    	}
    	
    	return picX;
    }
    
    private int GetRandomY(int lastY, int lastPicHeight, int flag) {
    	Random ranY = new Random();
    	
    	int picY = lastY + lastPicHeight/(ranY.nextInt(50)+1) + ranY.nextInt(50);
    	if (1 == flag) {
    		return picY;
    	}
    	
		while ((picY - lastY) * 2 <  lastPicHeight) {
			picY += ranY.nextInt(50);
		}
		
		return picY;
    }
    
    private Cursor RetrieveLocalImagesByDate(ContentResolver cr, String dateStr) {
    // get bitmap files from db by content provider
    	System.out.println("the date to get pic is: " + dateStr);
    	long dateminSec = Date.parse(dateStr+" 00:00:00");
		long datemaxSec = Date.parse(dateStr+" 23:59:59");
		String selection = "datetaken" + ">=? and datetaken<=?";
		String[] selectionArg = {"" + dateminSec, "" + datemaxSec};
		Cursor piccursor = cr.query(Media_ext_uri, mImageCols, selection, selectionArg, "datetaken asc");
		//Cursor piccursor = cr.query(Media_ext_uri, mImageCols, null, null, null);
				
    	return piccursor;
    // create thumbnail file from the bitmap files
    // save the thumbnail files? or just return the thumbnails
    }
    
    OnTouchListener touchListener = new OnTouchListener()  
    {  
        int temp[] = new int[]{0, 0};  
        public boolean onTouch(View arg0, MotionEvent arg1) {  
            // TODO Auto-generated method stub  
            int eventAction = arg1.getAction();  
            Log.e("testButtonMove", "OnTouchAction:"+eventAction);  
              
            int x = (int)arg1.getRawX();  
            int y = (int)arg1.getRawY();  
              
            System.out.println("1)" + x + "-" + y);
            
            switch (eventAction) {  
            case MotionEvent.ACTION_DOWN:  
            	
           	int evX = (int)arg1.getX();  
       	    int evY = (int)arg1.getY();  
       	  
	       	temp[0] = (int)arg1.getX();  
	        temp[1] = (int)(y-arg0.getTop());     
	        
	        System.out.println("2)" + temp[0] + "-" + temp[1]);
	        mLayoutGroup.bringChildToFront(arg0);  
	        arg0.postInvalidate();
	        
       	     /*
       	     if(imageView1.isOnPic(evX, evY))
       	     {
       	    	temp[0] = (int)arg1.getX();  
                temp[1] = (int)(y-arg0.getTop());     
                
                System.out.println("2)" + temp[0] + "-" + temp[1]);
                mLayoutGroup.bringChildToFront(arg0);  
                arg0.postInvalidate();  
                  
       	     }
       	     
       	  	 if(imageView2.isOnPic(evX, evY))
    	     {
    	    	temp[0] = (int)arg1.getX();  
             	temp[1] = (int)(y-arg0.getTop());     
             
             	System.out.println("4)" + temp[0] + "-" + temp[1]);
             	mLayoutGroup.bringChildToFront(arg0);  
             	arg0.postInvalidate();  
               
    	      }
    	      */
           	 
                break;  
            case MotionEvent.ACTION_MOVE:  

                int left = x - temp[0];  
                int top = y - temp[1];  
                int right = left + arg0.getWidth();  
                int bottom = top + arg0.getHeight();  
                  
                System.out.println("3)" + left + "-" + top + "-" + right + "-" + bottom);
                arg0.layout(left, top, right, bottom);  
                arg0.postInvalidate();  
                  
                break;  
  
            default:  
                break;  
            }  
              
            return false;  
        }  
    };  
} 