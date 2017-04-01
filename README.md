###一个笑脸View,效果图如下
![这里写图片描述](http://img.blog.csdn.net/20170401110558394?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvZ2l2ZW1lYWNvbmRvbQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)


由于技术有限,没有去画下面的字母,字母好画,就是里面的纹理,不太好搞,算了如果大神看见了,可以实现下,我会去学习的.


###实现的效果如下
![这里写图片描述](http://img.blog.csdn.net/20170401110759475?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvZ2l2ZW1lYWNvbmRvbQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

 ###步骤

 1. 分析自定义的属性
 2. 获取各个绘制层的坐标属性
 3. 绘制
 4. 动起来
 

###自定义属性暴露

```
<declare-styleable name="SmailFaceView">
        <attr name="smail_bg_color" format="color" />
        <attr name="smail_bg_radio" format="dimension" />
        <attr name="smail_face_color" format="color" />
        <attr name="smail_face_container_color" format="color" />
        <attr name="smail_face_eys_color" format="color" />
    </declare-styleable>
```

###计算各个绘制的范围

```
  @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = h;
        viewWith = w;
        bgRect = new RectF(0, 0, viewWith, viewHeight);
        centerPoint = new Point(viewWith / 2, viewHeight / 2);
        float top = (viewHeight - viewHeight * FACE_HEIGHT_SCAL) / 2;
        float left = (viewWith - FACE_HEIGHT_SCAL * viewHeight) / 2;
        float right = viewWith - left;
        float bottom = viewHeight - top;
        ovalReactF = new RectF(left, top, right, bottom);
        int leftEyeX = (int) (centerPoint.x - ovalReactF.width() / 8);
        int leftEyeY = centerPoint.y;
        int rightEyeX = (int) (centerPoint.x + ovalReactF.width() / 8);
        int rightEyeY = centerPoint.y;
        leftEyePoint = new Point(leftEyeX, leftEyeY);
        rightEyePoint = new Point(rightEyeX, rightEyeY);
        int mL = leftEyeX + 40;
        int mT = centerPoint.y + 50;
        int mR = rightEyeX - 40;
        int mB = centerPoint.y + 50 + 50;
        mouthReactF = new RectF(mL, mT, mR, mB);
        rotateFaceContainer();
    }
```

###绘制的过程
唯一一个比较有意思的就是黄色的脸盘子画法;
他是由两组扇形组成的 一个黄色比如40度扇形 + 10 度 透明扇形,然后循环360度绘制完毕

```
//核心代码就两行
 private void drawFaceContainer(Canvas canvas) {
        for (int i = 0; i < 8; i++) {
            canvas.drawArc(ovalReactF, 45 * i, 10, true, transPaint);
            canvas.drawArc(ovalReactF, 45 * i + 10, 35, true, faceContainerPaint);
        }
    }
```

###动起来
属性动画暴露增量改变值,刷新页面即可

```
  private void rotateFaceContainer() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1.F);
        valueAnimator.setDuration(3000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentF = (float) animation.getAnimatedValue();
                currentDegree = 360 * currentF;
                invalidate();
            }
        });
        valueAnimator.start();
    }
```

#源代码地址:[https://github.com/GuoFeilong/SmailFaceDemo](https://github.com/GuoFeilong/SmailFaceDemo) star谢谢,

#下面有彩蛋
![这里写图片描述](http://img.blog.csdn.net/20170401111503220?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvZ2l2ZW1lYWNvbmRvbQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
