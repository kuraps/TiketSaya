package com.kuraps.tiketsaya;


import android.animation.ObjectAnimator;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.view.View;

public class ParallaxViewTransformer implements ViewPager.PageTransformer{
		
	int mImageId = -1;
	public static final String IMAGE_TAG = "parallaxViewTag";
	String imgTag = null;
	
	@Override
	public void transformPage(View page, float position) {
		// TODO Auto-generated method stub
		
		int pageWidth = page.getWidth();
		
		if(mImageId > 0) {
			if (page.findViewById(mImageId)!=null) {
				page = page.findViewById(mImageId);
			}
			if(position <= 0) {
				if(page.getId()== mImageId) {
					page.setTranslationX(pageWidth * -position /1.4f); 
				} else {
					page.setTranslationX(0);
				}
			} else {
				page.setTranslationX(pageWidth * - position /1.4f);
			}
		}
		
		if(!TextUtils.isEmpty(imgTag)) {
			if(page.findViewWithTag(imgTag) != null) {
				page = page.findViewWithTag(imgTag);
			}
			if(position <= 0 ) {
				if (page.getTag() == imgTag) {
					page.setTranslationX(pageWidth * -position / 1.4f);
					ObjectAnimator.ofFloat(page, "translationX", pageWidth * -position/1.4f).start();
				}else {
					page.setTranslationX(0);
					ObjectAnimator.ofFloat(page, "translationX",0).start();
				}
			}else {
				ObjectAnimator.ofFloat(page, "translationX", pageWidth * -position/1.4f).start();
			}
		}
		
	}
	
	public ParallaxViewTransformer(int mImageId) {
		this.mImageId = mImageId;
	}
	public ParallaxViewTransformer(String imgTag) {
		this.imgTag = imgTag;
	}
	
	

}