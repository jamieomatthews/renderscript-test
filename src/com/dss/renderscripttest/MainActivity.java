package com.dss.renderscripttest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.widget.ImageView;

public class MainActivity extends Activity {

ImageView imageView;
	
	Bitmap originalBitmap;
    Bitmap filteredBitmap;
    RenderScript mRS;
    Allocation mInAllocation;
    Allocation mOutAllocation;
    String mCurrentEffect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imageView = (ImageView)findViewById(R.id.image);
		
		BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 8;
        originalBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.colors,opts);
        filteredBitmap = Bitmap.createBitmap(originalBitmap.getWidth(),originalBitmap.getHeight(), originalBitmap.getConfig());
        imageView.setImageBitmap(originalBitmap);
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        
        //RENDERSCRIPT ALLOCATION
        mRS = RenderScript.create(this);
        mInAllocation = Allocation.createFromBitmap(mRS, originalBitmap,Allocation.MipmapControl.MIPMAP_NONE,Allocation.USAGE_SCRIPT);
        mOutAllocation = Allocation.createTyped(mRS, mInAllocation.getType());
        mOutAllocation.copyFrom(originalBitmap);
        mOutAllocation.copyTo(filteredBitmap);
        
        //ScriptC_hello_world mScript = new ScriptC_hello_world(mRS);
     // script created using the helloworld bitcode in res/raw/helloworld.bc
        ScriptC_brightnessfilter helloworldScript = new ScriptC_brightnessfilter(mRS);
        
        helloworldScript.set_brightnessValue(4.0f);
        helloworldScript.bind_gPixels(mInAllocation);

        helloworldScript.set_gIn(mInAllocation);
        helloworldScript.set_gOut(mOutAllocation);
        helloworldScript.set_gScript(helloworldScript);
        helloworldScript.invoke_filter();
        mOutAllocation.copyTo(filteredBitmap);
        // now we can call the script’s hello_world function using the reflected method
        //helloworldScript.invoke_filter(helloworldScript, mInAllocation, mOutAllocation);
        //mScript.invoke_filter(mScript,mInAllocation, mOutAllocation);
        //mOutAllocation.copyTo(filteredBitmap);
        //imageView.setImageBitmap(filteredBitmap);
        
	}

	
}
