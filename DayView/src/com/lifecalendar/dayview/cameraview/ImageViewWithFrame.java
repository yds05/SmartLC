package com.lifecalendar.dayview.cameraview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Create ImageView with border
 * @author Felix
 *
 */
public class ImageViewWithFrame extends View { 
 
   private Context context ; 
   private String imagePath;
   private Bitmap mainBmp;  
   private int mainBmpWidth , mainBmpHeight , controlBmpWidth , controlBmpHeight ;  
   private Matrix matrix ;   
   private float [] srcPs , dstPs ;  
   private RectF srcRect , dstRect ;  
   private Paint paint, paintRect , paintFrame;  

   private float angel;
   private String text;
   private float deltaX = 5, deltaY = 5; //位移值 
   private RectF txtBackgroundRect;
     
   /* 图片控制点 
    * 0---1---2 
    * |       | 
    * 7   8   3 
    * |       | 
    * 6---5---4  
    */  
   public static final int CTR_NONE = -1;  
   public static final int CTR_LEFT_TOP = 0;  
   public static final int CTR_MID_TOP = 1;  
   public static final int CTR_RIGHT_TOP = 2;  
   public static final int CTR_RIGHT_MID = 3;  
   public static final int CTR_RIGHT_BOTTOM = 4;  
   public static final int CTR_MID_BOTTOM = 5;  
   public static final int CTR_LEFT_BOTTOM = 6;  
   public static final int CTR_LEFT_MID = 7;  
   public static final int CTR_MID_MID = 8;  
   public int current_ctr = CTR_NONE;  
     
   public ImageViewWithFrame(Context context){  
       super(context);  
       this.context = context ;  
   }  
     
   public ImageViewWithFrame(Context context, AttributeSet attrs) {  
       super(context, attrs);  
       this.context = context ;  
   }  
     
   public void initData(Bitmap bitmap){ 
	   if (null == bitmap) {
	       System.out.println("---------InitData"  + imagePath);
	       BitmapFactory.Options options = new BitmapFactory.Options();
	       mainBmp = BitmapFactory.decodeFile(imagePath, options); //此时返回bm为空
	   } else {
		   mainBmp = bitmap;
	   }
	   
       mainBmpWidth = mainBmp.getWidth();  
       mainBmpHeight = mainBmp.getHeight();  
         
       srcPs = new float[]{  
                               0,0,   
                               mainBmpWidth/2,0,   
                               mainBmpWidth,0,   
                               mainBmpWidth,mainBmpHeight/2,  
                               mainBmpWidth,mainBmpHeight,   
                               mainBmpWidth/2,mainBmpHeight,   
                               0,mainBmpHeight,   
                               0,mainBmpHeight/2,   
                               mainBmpWidth/2,mainBmpHeight/2  
                           };  
       /*
       srcPs = new float[]{  
				               0 - 5,0 - 5,   
				               mainBmpWidth/2,0 - 5,   
				               mainBmpWidth + 5,0 + 5,   
				               mainBmpWidth + 5,mainBmpHeight/2,  
				               mainBmpWidth + 5,mainBmpHeight + 10,   
				               mainBmpWidth/2,mainBmpHeight + 10,   
				               0 -5,mainBmpHeight + 10,   
				               0 -5,mainBmpHeight/2,   
				               mainBmpWidth/2,mainBmpHeight/2  
				           };*/
       
       dstPs = srcPs.clone();  
       srcRect = new RectF(0, 0, mainBmpWidth, mainBmpHeight);  
       dstRect = new RectF();  
       txtBackgroundRect = new RectF(0 - 5, mainBmpHeight - 5, mainBmpWidth + 5, mainBmpHeight + 10);
         
       matrix = new Matrix();  
         
       paint = new Paint();
       
       paintRect = new Paint();  
       paintRect.setColor(Color.RED);  
       paintRect.setAlpha(100);  
       paintRect.setAntiAlias(true);  
          
       paintFrame = new Paint();  
       paintFrame.setColor(Color.WHITE);  
       paintFrame.setAntiAlias(true);  
       paintFrame.setStyle(Paint.Style.STROKE);
       paintFrame.setStrokeWidth(10); 
          
       setMatrix();  
   }  
   
   public int getBmpWidthWithFrame()
   {
	   return (int)(mainBmpWidth + 10);
   }
   
   public int getBmpHeightWithFrame()
   {
	   return (int)(mainBmpHeight + 15);
   }
   
   public void setImagePath(String imagePath)
   {
	   this.imagePath = imagePath;
   }
   
   public void setText(String text)
   {
   		this.text = text;
   }
   
   public void setRotateAngel(float angel)
   {
   		this.angel = angel;	
   }
      
   private void setMatrix(){  
    	matrix.postTranslate(deltaX , deltaY); 
        matrix.postRotate(angel,dstPs[CTR_MID_MID * 2], dstPs[CTR_MID_MID * 2 + 1]);
        matrix.mapPoints(dstPs, srcPs);  
        matrix.mapRect(dstRect, srcRect);  
        
        System.out.println("srcRect:" + srcRect.toString());
        System.out.println("dstRect:" + dstRect.toString());
   }  
      
   private boolean isOnPic(int x , int y){  
        if(dstRect.contains(x, y)){  
            return true;  
        }else   
            return false;  
   }  
       
   private int isOnCP(int evx, int evy) {  
        Rect rect = new Rect(evx-controlBmpWidth/2,evy-controlBmpHeight/2,evx+controlBmpWidth/2,evy+controlBmpHeight/2);  
        int res = 0 ;  
        for (int i = 0; i < dstPs.length; i+=2) {  
            if(rect.contains((int)dstPs[i], (int)dstPs[i+1])){  
                return res ;  
            }  
            ++res ;   
        }  
        return CTR_NONE;  
   }  
           
    @Override  
    public void onDraw(Canvas canvas){  
        drawBackground(canvas);//Draw background, for testing   
        drawImage(canvas);//Draw picture   
        drawFrame(canvas);//Draw picture border   
       // drawControlPoints(canvas);//Dras control points   
    }  
      
    private void drawBackground(Canvas canvas){  
        canvas.drawRect(dstRect, paintRect);  
    }  
    
    private void drawImage(Canvas canvas){
    	paint.setAntiAlias(true);
    	canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
    	//Bitmap bitmap_tmp = Bitmap.createBitmap(bitmap, 0, 0, width, height);
    	canvas.drawBitmap(mainBmp, matrix, paint);
    }
      
    private void drawFrame(Canvas canvas){  
    	/*
        canvas.drawLine(dstPs[0] , dstPs[1], dstPs[4], dstPs[5], paintFrame);  
        canvas.drawLine(dstPs[4], dstPs[5], dstPs[8], dstPs[9], paintFrame);  
        canvas.drawLine(dstPs[8], dstPs[9], dstPs[12], dstPs[13], paintFrame);  
        canvas.drawLine(dstPs[0], dstPs[1], dstPs[12] , dstPs[13], paintFrame);  
        canvas.drawPoint(dstPs[16], dstPs[17], paintFrame);  */
        
    	//Lock canvas
        canvas.save();  
        canvas.translate(deltaX, deltaY);
        canvas.rotate(angel, dstPs[CTR_MID_MID * 2], dstPs[CTR_MID_MID * 2 + 1]);
  
        //Draw the picture border
        canvas.drawRect(srcRect, paintFrame);
        
        //Draw text background
        Paint textBackgroundPaint = new Paint();
        textBackgroundPaint.setStyle(Paint.Style.FILL);
        textBackgroundPaint.setColor(Color.WHITE);
        canvas.drawRect(txtBackgroundRect, textBackgroundPaint);  
        
        //Draw text with picture time
        int textsize = 12;
        Paint textPaint = new Paint( Paint.ANTI_ALIAS_FLAG);  
        textPaint.setTextSize( textsize );  
        textPaint.setColor( Color.BLACK);  
        textPaint.setTextAlign(Paint.Align.CENTER);
        FontMetrics fontMetrics = textPaint.getFontMetrics();  
          
        //Y坐标是：(行高-字体高度)/2+字体高度
        int fontHeight = getFontHeight(textsize);
        float centerBaseY = (mainBmpHeight-5) + ((15-fontHeight)/2 - fontMetrics.top);
        float centerBaseX = mainBmpWidth/2;
        
        canvas.drawText( text, centerBaseX, centerBaseY, textPaint); 
  
        //Unlock canvas
        canvas.restore();
    }  
    
    public int getFontHeight(float fontSize)  
    {  
        Paint paint = new Paint();  
        paint.setTextSize(fontSize);  
        FontMetrics fm = paint.getFontMetrics();  
        return (int) Math.ceil(fm.descent - fm.top) + 2;  
    }
    
    // Touch event  
    public boolean onTouchEvent(MotionEvent event) {  
    	System.out.println("Touch the iamge");
        return true;  
    }
            
}