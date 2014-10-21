package com.claro.cfcmobile.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.widget.util.TypefaceLocator;


/**
 * 
 * @author Christopher Herrera
 *
 */
public class TextView extends android.widget.TextView{
	
	public TextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		prepareTypeface(attrs);
		
	}

	public TextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		prepareTypeface(attrs);
	}
	
	private void prepareTypeface( AttributeSet attrs ){
		TypedArray values = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTypeface );
		int type = values.getInt( R.styleable.CustomTypeface_typeface, 0 );
			
		Typeface tf = TypefaceLocator.getTypeface(getContext(), type);
		setTypeface(tf);
		values.recycle();
	}
	
}
