package com.modi.geometry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by J_modi on 2017/5/11.
 */

public class Geometry extends View {
	//定义画笔
	private Paint mPaint = null;
	//记录画布宽高
	private int width = 0;
	private int height = 0;
	//记录坐标箭头的长度
	private int arrowLenght = 0;
	//记录X,Y的刻度大小
	private int xAxisScale = 20;
	private int yAxisScale = 20;
	//记录坐标轴标识与轴的距离
	private int offsetX = 10;
	private int offsetY = 10;
	// 记录刻度线的长度
	private int xScaleLenght = 15;
	private int yScaleLenght = 15;
	// 记录五边形轴线长度
	private int axisLenght = 200;
	//记录五边形轴线与坐标轴的夹角:30度
	private double angle = (Math.PI/6);
	// private int angle = 30;
	//记录五边形角标识与对应顶角的距离
	private int offset = 80;
	private List<Map<String,Object>> datas = null;

	private String []texts = {
			"关系","身份","资产",
			"偏好","履约"
	};
	private int []icons = {
			R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,
			R.mipmap.ic_launcher,R.mipmap.ic_launcher
	};
	//模拟用户数据所占比率
	private int []rates = {
			50,90,30,
			60,20
	};
	// 角标的大小
	private int bSize = 50;

	// 模拟真实用户数据
	private List<Map<String,Object>> userDatas = null;
	public Geometry(Context context) {
		this(context,null);
	}

	public Geometry(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		initPaint();

		arrowLenght = 30;

		datas = new ArrayList<>();
		userDatas = new ArrayList<>();
		initDatas();
	}

	private void initPaint() {
		mPaint = new Paint();
		// mPaint.setColor(Color.CYAN);
		mPaint.setColor(Color.LTGRAY);
		mPaint.setStrokeWidth(2);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);
	}

	private void initDatas() {
		if (datas.size() == 0){
			for (int i = 0; i < texts.length; i++) {
				Map<String,Object> map = new HashMap<>();
				map.put("text",texts[i]);
				map.put("icon",icons[i]);
				datas.add(map);
				//用户所占对应类型比率数据
				map.put("rate",rates[i]);
				userDatas.add(map);
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		width = canvas.getWidth();
		height = canvas.getHeight();
		//绘制坐标轴
		drawCoordinate(canvas);

		//绘制一个三角形
		// drawTriangle(canvas);

		//绘制多边形
		drawPolygon(canvas);
	}

	/**
	 * 绘制多边形 ： 以五边形为例
	 * @param canvas
	 */
	private void drawPolygon(Canvas canvas) {
		//空心
		mPaint.setStyle(Paint.Style.FILL);
		// 1.绘制五边形的轴线
		CalcAxis calcAxis = new CalcAxis().invoke(axisLenght);
		int rtDotX = calcAxis.getRtDotX();
		int rtDotY = calcAxis.getRtDotY();
		int rbDotX = calcAxis.getRbDotX();
		int rbDotY = calcAxis.getRbDotY();
		int lbDotX = calcAxis.getLbDotX();
		int lbDotY = calcAxis.getLbDotY();
		int ltDotX = calcAxis.getLtDotX();
		int ltDotY = calcAxis.getLtDotY();
		Path path = calcAxis.getPath();

		canvas.drawLine(width/2,height/2,width/2,height/2-axisLenght,mPaint);
		canvas.drawLine(width/2,height/2,rtDotX,rtDotY,mPaint);
		canvas.drawLine(width/2,height/2,rbDotX,rbDotY,mPaint);
		canvas.drawLine(width/2,height/2,lbDotX,lbDotY,mPaint);
		canvas.drawLine(width/2,height/2,ltDotX,ltDotY,mPaint);

		// 2.绘制底层五边形
		//设置画笔颜色
		mPaint.setColor(0x2288faff);
		canvas.drawPath(path,mPaint);
		// 5.绘制五边形角标识
		for (int i = 0; i < datas.size(); i++) {
			Map<String, Object> map = datas.get(i);
			String text = (String) map.get("text");
			Log.e("text = ",text);
			int icon = (int) map.get("icon");
			//计算图标及文字的宽高
			// float tWidth = mPaint.measureText(text);
			mPaint.setTextSize(25);
			mPaint.setColor(0xFFDE3226);
			Rect rect = new Rect();
			mPaint.getTextBounds(text,0,text.length(),rect);
			int tWidth = rect.width();
			int tHeight = rect.height();
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), icon);
			Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, tWidth, tHeight*2, true);
			int bWidth = scaledBitmap.getWidth();
			int bHeight = scaledBitmap.getHeight();

			//记录绘制图片及文字的坐标
			int xB = 0;
			int yB = 0;
			int xt = 0;
			int yt = 0;
			if (text.equals("关系")){
				xB = ltDotX-offset;
				yB = ltDotY-bHeight;
				xt = xB;
				yt = ltDotY+tHeight;
				Log.e("关系 ：","xB = "+xB+" : yB = "+yB);
			}else if (text.equals("身份")){
				xB = width/2-bWidth/2;
				yB = (height/2-axisLenght)-offset/2-bHeight-tHeight;
				xt = xB;
				yt = yB + tHeight + bHeight;
				Log.e("身份 ：","xB = "+xB+" : yB = "+yB);
				Toast.makeText(getContext(),"身份 ："+yB+" : "+yt,Toast.LENGTH_SHORT).show();
			}else if (text.equals("资产")){
				xB = rtDotX+offset/2;
				yB = rtDotY-bHeight;
				xt = rtDotX+offset/2;
				yt = rtDotY+tHeight;
				Log.e("资产 ：","xB = "+xB+" : yB = "+yB);
			}else if (text.equals("偏好")){
				xB = rbDotX+offset/2;
				yB = rbDotY-bHeight;
				xt = rbDotX+offset/2;
				yt = rbDotY+tHeight;
				Log.e("偏好 ：","xB = "+xB+" : yB = "+yB);
			}else if (text.equals("履约")){
				xB = lbDotX-offset;
				yB = lbDotY-bHeight;
				xt = lbDotX-offset;
				yt = lbDotY+tHeight;
				Log.e("履约 ：","xB = "+xB+" : yB = "+yB);
			}
			//绘制左上角图标及文字
			canvas.drawBitmap(scaledBitmap,xB,yB,mPaint);
			canvas.drawText(text,xt,yt,mPaint);
		}


		// 3.绘制次层五边形
		CalcAxis calcAxis1 = new CalcAxis().invoke(axisLenght/2);
		rtDotX = calcAxis1.getRtDotX();
		rtDotY = calcAxis1.getRtDotY();
		rbDotX = calcAxis1.getRbDotX();
		rbDotY = calcAxis1.getRbDotY();
		lbDotX = calcAxis1.getLbDotX();
		lbDotY = calcAxis1.getLbDotY();
		ltDotX = calcAxis1.getLtDotX();
		ltDotY = calcAxis1.getLtDotY();
		path = calcAxis1.getPath();
		mPaint.setColor(0x5588faff);
		canvas.drawPath(path,mPaint);
		// 4.绘制我们的真实数据五边形:参数规定为每种数据占中评分的百分比

		drawUserDataPolygon(canvas,userDatas);

		//恢复画笔样式
		initPaint();
	}

	private void drawUserDataPolygon(Canvas canvas, List<Map<String, Object>> userDatas) {
		CalcAxis calcAxis = null;
		int rtDotX = 0;
		int rtDotY = 0;
		int rbDotX = 0;
		int rbDotY = 0;
		int lbDotX = 0;
		int lbDotY = 0;
		int ltDotX = 0;
		int ltDotY = 0;

		int sX = 0;
		int sY = 0;
		Path path = null;

		if (userDatas!=null&&userDatas.size()>0){
			calcAxis = new CalcAxis();
			for (int i = 0; i < userDatas.size(); i++) {
				Map<String, Object> map = userDatas.get(i);
				String text = (String) map.get("text");
				Log.e("text = ",text);
				int rate = (int) map.get("rate");
				calcAxis.invoke(axisLenght * rate/100);
				// Log.e("按比例计算后的长度 : ", String.valueOf(axisLenght * (rate/100)));
				path = new Path();
				Log.e("rate = ", String.valueOf(rate));
				if (text.equals("关系")){
					ltDotX = calcAxis.getLtDotX();
					ltDotY = calcAxis.getLtDotY();
					calcAxis.invoke(axisLenght/2);
					int ltDotX1 = calcAxis.getLtDotX();
					int ltDotY1 = calcAxis.getLtDotY();
				}else if (text.equals("身份")){
					sX = canvas.getWidth()/2;
					sY = canvas.getHeight()/2 - axisLenght * rate/100;
				}else if (text.equals("资产")){
					rtDotX = calcAxis.getRtDotX();
					rtDotY = calcAxis.getRtDotY();
				}else if (text.equals("偏好")){
					rbDotX = calcAxis.getRbDotX();
					rbDotY = calcAxis.getRbDotY();
				}else if (text.equals("履约")){
					lbDotX = calcAxis.getLbDotX();
					lbDotY = calcAxis.getLbDotY();
				}
			}
			path.moveTo(sX,sY);
			path.lineTo(rtDotX,rtDotY);
			path.lineTo(rbDotX,rbDotY);
			path.lineTo(lbDotX,lbDotY);
			path.lineTo(ltDotX,ltDotY);
			path.lineTo(sX,sY);
			// 2.绘制底层五边形
			//设置画笔颜色
			mPaint.setColor(0xaafaffbd);
			canvas.drawPath(path,mPaint);
			initPaint();
		}
	}

	private void drawTriangle(Canvas canvas) {
		int dotX1 = (int) (width/2 + 100 * Math.sin(30));
		int dotY1 = (int) (height/2 - 100 * Math.cos(30));
		int dotX2 = (int) (width/2 + 150 * Math.sin(62));
		int dotY2 = (int) (height/2 - 150 * Math.cos(62));
		int dotX3 = (int) (width/2 + 200 * Math.sin(88));
		int dotY3 = (int) (height/2 - 200 * Math.cos(88));
		Path path = new Path();
		path.moveTo(dotX1,dotY1);
		path.lineTo(dotX2,dotY2);
		path.lineTo(dotX3,dotY3);
		path.lineTo(dotX1,dotY1);
		mPaint.setStyle(Paint.Style.STROKE);
		canvas.drawPath(path,mPaint);
	}

	/**
	 * 绘制坐标轴
	 * @param canvas
	 */
	private void drawCoordinate(Canvas canvas) {
		//绘制X轴
		canvas.drawLine(0,height/2,width,height/2,mPaint);
		//绘制小箭头样式
		//计算箭头终点坐标
		int arrowX = (int) (arrowLenght * Math.cos(Math.PI/4));
		int arrowY = (int) (arrowLenght * Math.sin(Math.PI/4));
		int aX = width - arrowX;
		int aY = height/2 - arrowY;
		Toast.makeText(getContext(),"ax : ay = "+aX+" : "+aY,Toast.LENGTH_SHORT).show();
		//绘制X轴上下箭头线：注意不需要带正负
		canvas.drawLine(width,height/2,(aX),(aY),mPaint);
		canvas.drawLine(width,height/2,(aX),(height/2+arrowY),mPaint);
		//绘制圆点及坐标点标识
		// 1.绘制坐标圆点0或者o
		canvas.drawText("o",(width/2-arrowX),(height/2+arrowY),mPaint);
		// 2.绘制x标识
		canvas.drawText("x",(width-arrowX),(height/2+arrowY+offsetY),mPaint);
		//绘制Y轴
		canvas.drawLine(width/2,0,width/2,height,mPaint);
		//绘制Y轴左右箭头线
		arrowX = (int) (arrowLenght * Math.sin(Math.PI/4));
		arrowY = (int) (arrowLenght * Math.cos(Math.PI/4));
		aX = width/2 - arrowX;
		aY = arrowY;
		canvas.drawLine(width/2,0,(aX),(aY),mPaint);
		canvas.drawLine(width/2,0,(width/2+arrowX),(aY),mPaint);
		//绘制坐标原点
		canvas.drawCircle(width/2,height/2,4,mPaint);
		// 3.绘制y标识
		canvas.drawText("y",(width/2-arrowX-offsetX),(aY),mPaint);
		// 绘制刻度线:
		//计算刻度线数量：坐标轴箭头处不绘制
		int xCount = width/2 / xAxisScale - 1;
		int yCount = height/2 / yAxisScale -1;
		//计算刻度线所占的宽度
		// int xPaintTotalWidth = (int) ((xCount+1)*mPaint.getStrokeWidth());
		// int yPaintTotalWidth = (int) ((yCount+1)*mPaint.getStrokeWidth());
		// int xC = (width/2-xPaintTotalWidth) / xAxisScale -1;
		// int yC = (width/2-yPaintTotalWidth) / xAxisScale -1;
		int xStart = 0;
		int xStop = 0;
		int yStart = 0;
		int yStop = 0;

		// 绘制X轴
		for (int i = 1; i <= xCount; i++) {
			//绘制正半轴
			xStart = width/2 +(i)*xAxisScale;
			xStop = xStart;
			yStart = height/2;
			yStop = yStart - xScaleLenght;
			if (xStart == width/2){
				continue;
			}
			canvas.drawLine(xStart,yStart,xStop,yStop,mPaint);
			//绘制负半轴
			xStart = width/2 -(i)*xAxisScale;
			xStop = xStart;
			// yStart = height/2;
			// yStop = yStart - xScaleLenght;
			canvas.drawLine(xStart,yStart,xStop,yStop,mPaint);
		}
		// 绘制Y轴
		for (int i = 1; i <= yCount; i++) {
			//绘制正半轴
			xStart = width/2;
			xStop = xStart + yScaleLenght;
			yStart = height/2 - i*yAxisScale;
			yStop = yStart;
			if (yStart == height/2){
				continue;
			}
			canvas.drawLine(xStart,yStart,xStop,yStop,mPaint);
			//绘制负半轴
			// xStart = width/2 -(i)*xAxisScale;
			// xStop = xStart;
			yStart = height/2 + i*yAxisScale;
			yStop = yStart;
			canvas.drawLine(xStart,yStart,xStop,yStop,mPaint);
		}
	}

	private class CalcAxis {
		private int ltDotX;
		private int ltDotY;
		private int rtDotX;
		private int rtDotY;
		private int lbDotX;
		private int lbDotY;
		private int rbDotX;
		private int rbDotY;
		private Path path;

		public int getLtDotX() {
			return ltDotX;
		}

		public int getLtDotY() {
			return ltDotY;
		}

		public int getRtDotX() {
			return rtDotX;
		}

		public int getRtDotY() {
			return rtDotY;
		}

		public int getLbDotX() {
			return lbDotX;
		}

		public int getLbDotY() {
			return lbDotY;
		}

		public int getRbDotX() {
			return rbDotX;
		}

		public int getRbDotY() {
			return rbDotY;
		}

		public Path getPath() {
			return path;
		}

		public CalcAxis invoke(int axisLenght) {
			// 计算左、右上 左、右下角点坐标
			ltDotX = 0;
			ltDotY = 0;
			rtDotX = 0;
			rtDotY = 0;
			lbDotX = 0;
			lbDotY = 0;
			rbDotX = 0;
			rbDotY = 0;
			ltDotX = (int) (width/2 - (axisLenght * Math.cos(angle)));
			ltDotY = (int) (height/2 - (axisLenght * Math.sin(angle)));

			rtDotX = (int) (width/2 + (axisLenght * Math.cos(angle)));
			rtDotY = ltDotY;

			lbDotX = (int) (width/2 - (axisLenght * Math.sin(angle)));
			lbDotY = (int) (height/2 + (axisLenght * Math.cos(angle)));

			rbDotX = (int) (width/2 + (axisLenght * Math.sin(angle)));
			rbDotY = lbDotY;

			path = new Path();
			path.moveTo(width/2,height/2-axisLenght);
			path.lineTo(rtDotX,rtDotY);
			path.lineTo(rbDotX,rbDotY);
			path.lineTo(lbDotX,lbDotY);
			path.lineTo(ltDotX,ltDotY);
			//封闭
			path.lineTo(width/2,height/2-axisLenght);
			return this;
		}
	}
}


