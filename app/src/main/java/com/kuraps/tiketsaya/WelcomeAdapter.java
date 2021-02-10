package com.kuraps.tiketsaya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

public class WelcomeAdapter extends PagerAdapter {
        Context context;
        LayoutInflater inflater;
        public WelcomeAdapter(Context context) {
            this.context=context;
        }
        public int [] list_images= {
                R.drawable.intro_slide_img1,
                R.drawable.intro_slide_img2,
                R.drawable.intro_slide_img3,
                R.drawable.intro_slide_img4
        };

        public int[] list_title= {
                R.string.welcome_adapter_title,
                R.string.welcome_adapter_title2,
                R.string.welcome_adapter_title3,
                R.string.welcome_adapter_title4,
        };

        public int[] list_description= {
                R.string.welcome_adapter_sub,
                R.string.welcome_adapter_sub2,
                R.string.welcome_adapter_sub3,
                R.string.welcome_adapter_sub4
        };

        public int getCount() {
            return list_title.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view==(RelativeLayout)obj;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.activity_welcome_adapter,container,false);
            ImageView img = (ImageView)view.findViewById(R.id.intro_slideimg);
            TextView txt1 = (TextView)view.findViewById(R.id.intro_slidetitle);
            TextView txt2 = (TextView)view.findViewById(R.id.intro_slidedescription);
            img.setImageResource(list_images[position]);
            txt1.setText(list_title[position]);
            txt2.setText(list_description[position]);

            img.setTag(ParallaxViewTransformer.IMAGE_TAG);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout)object);
        }

}
