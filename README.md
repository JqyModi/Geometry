#Author：modi
#Time：2017年5月13日09:19:30

### Android自定义View之几何图形绘制
***
### 效果预览
***
![效果预览](http://...)
***
***
### 如何使用
	1.不会导入自定义View的看我这篇JqyModi/Gobang的README.md文件
	2.由于时间关系各种属性设置都写在View中需要的朋友可以自己提出来作为自定义属性
	3.关于如何自定义属性可以参考我的另外一个项目：ArcMenu及Gobang
### 实现思路及代码实现
>	- 1.绘制坐标轴：
>		- 1.绘制X轴
>		- 2.绘制Y轴
>		- 3.绘制坐标轴表示（o,x,y）
>		- 4.绘制刻度线
>		- 5.代码实现：
```
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
···
>	- 2.绘制底层五边形
>		- 1.计算五边形定各个点坐标：通过传入五边形的轴线（从坐标原点到各个顶点的距离）长来计算：
>		- 2.构造画笔绘制路径
>		- 3.绘制五边形
>		- 4.代码实现：
```
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
```
>	- 3.绘制次层五边形：（跟2中一样操作不同的是将轴线比率调至原来一半）
>	- 4.绘制用户真实数据五边形
>		- 1.根据用户数据传入的百分比计算出需要绘制轴线的长度
>		- 2.重复2中操作
>	- 5.计算用户数据得分(待完善)
***
