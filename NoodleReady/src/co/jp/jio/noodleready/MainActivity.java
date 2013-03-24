package co.jp.jio.noodleready;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class MainActivity extends Activity {

	private static final long THREE_MINUTES = 3;
	private static final double LDPI = 0.75;
	private static final double MDPI = 1.0;	
	private static final double TVDPI = 1.3312500715255737;
	private static final double HDPI = 1.5;
	private static final double XHDPI = 2.0;
	private long minutes;
	private long seconds;
	protected AdView adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		adView = (AdView)findViewById(R.id.ad);  
		adView.loadAd(new AdRequest());
		
		final Chronometer timer = (Chronometer) findViewById(R.id.timer);
		
		final RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout);
		final ImageView closingNoodle = new ImageView(getApplicationContext());
		closingNoodle.setImageResource(R.drawable.cup_noodle_bk);
		
		int width = 0, height = 0; 
		final double screenScale = getResources().getDisplayMetrics().density;
		Log.d("!!! SCREEN DENSITY: ", "!!! SCREEN DENSITY: " + String.valueOf(screenScale));
		if (screenScale <= LDPI ) {
			width = 320;
			height = 260;
			timer.setPadding(0,60,0,0);
			timer.setTextSize(80);
		} else if (screenScale > LDPI && screenScale <= MDPI ) {
			width = 480;
			height = 320;
			timer.setPadding(0,75,0,0);
			timer.setTextSize(70);
		} else if (screenScale > MDPI && screenScale <= TVDPI ) {
			width = 2000;
			height = 800;
			timer.setPadding(0,190,0,0);
			timer.setTextSize(130);
		} else if (screenScale > TVDPI && screenScale <= HDPI ) {
			width = 800;
			height = 480;
			timer.setPadding(0,150,0,0);
			timer.setTextSize(75);
		} else if (screenScale > HDPI && screenScale <= XHDPI) {
			width = 1280;
			height = 720;
			timer.setPadding(0,200,0,0);
			timer.setTextSize(90);
		}
		final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
	    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
	    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
	    closingNoodle.setLayoutParams(layoutParams);
	    closingNoodle.setAdjustViewBounds(true);
		layout.addView(closingNoodle);
		
	    Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
		closingNoodle.startAnimation(myFadeInAnimation);
	    
		timer.bringToFront();
		timer.setBase(SystemClock.elapsedRealtime());
	    timer.start();
	    timer.setOnChronometerTickListener(new OnChronometerTickListener() { 
	    	public void onChronometerTick(Chronometer arg0) { 
				minutes=((SystemClock.elapsedRealtime()-timer.getBase())/1000)/60; 
				seconds=((SystemClock.elapsedRealtime()-timer.getBase())/1000)%60;
				
				if (minutes == THREE_MINUTES) {
					timer.stop();
					ImageView openingNoodle = new ImageView(getApplicationContext());
				    openingNoodle.setImageResource(R.drawable.openingnoodle);
				    openingNoodle.setLayoutParams(layoutParams);
				    openingNoodle.setAdjustViewBounds(true); 
				    layout.removeView(closingNoodle);
					layout.addView(openingNoodle);
					
					AnimationDrawable openingNoodleAnimation = (AnimationDrawable) openingNoodle.getDrawable(); 
					openingNoodleAnimation.start();
					
					MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.cup_noodle_ok1); 	
					mPlayer.start();
				}
				
				if (minutes == 0 && seconds < 10) { arg0.setText("00:0"+seconds);   
				} else if (minutes == 0 && seconds >= 10) { arg0.setText("00:"+seconds);
				} else if (minutes > 0 && seconds < 10) { arg0.setText("0"+minutes+":0"+seconds);
				} else if (minutes > 0 && seconds >= 10) { arg0.setText("0"+minutes+":"+seconds);}  
			}
	    }); 
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		super.onResume();
		AdView adView = (AdView) findViewById(R.id.ad);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { 
			adView.setVisibility(View.INVISIBLE);
		} else {
			adView.setVisibility(View.VISIBLE);
		}
	 }
	
	@Override
	protected void onPause(){
		super.onPause();
		finish();
	}

}
