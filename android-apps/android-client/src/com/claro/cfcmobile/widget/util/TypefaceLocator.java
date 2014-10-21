
package com.claro.cfcmobile.widget.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * 
 * @author Christopher Herrera
 *
 */
public class TypefaceLocator {

	private static final int TYPE_ROBOTO_BOLD		 = 1;
	private static final int TYPE_ROBOTO_LIGHT 		 = 2;
	private static final int TYPE_ROBOTO_MEDIUM 	 = 3;
	private static final int TYPE_ROBOTO_REGULAR 	 = 4;
	private static final int TYPE_ROBOTO_THIN 	 	 = 5;
    private static final int TYPE_ENTYPO = 6;
	
	public enum Type{

		ROBOTO_BOLD{
			@Override
			protected Typeface get( Context cxt ) {
				return Typeface.createFromAsset(cxt.getAssets(), "fonts/Roboto-Bold.ttf");
			}
			
			@Override
			protected boolean counterPart(int identifier) {
					if( identifier == TYPE_ROBOTO_BOLD )
						return true;
					return false;
				}
		},
		ROBOTO_LIGHT{
			@Override
			protected Typeface get( Context cxt ) {
				return Typeface.createFromAsset(cxt.getAssets(), "fonts/Roboto-Light.ttf");
			}
			
			@Override
			protected boolean counterPart(int identifier) {
					if( identifier == TYPE_ROBOTO_LIGHT )
						return true;
					return false;
				}
		},
		ROBOTO_MEDIUM{
			@Override
			protected Typeface get( Context cxt ) {
				return Typeface.createFromAsset(cxt.getAssets(), "fonts/Roboto-Medium.ttf");
			}
			
			@Override
			protected boolean counterPart(int identifier) {
					if( identifier == TYPE_ROBOTO_MEDIUM )
						return true;
					return false;
				}
		},
		ROBOTO_REGULAR{
			@Override
			protected Typeface get( Context cxt ) {
				return Typeface.createFromAsset(cxt.getAssets(), "fonts/Roboto-Regular.ttf");
			}
			
			@Override
			protected boolean counterPart(int identifier) {
				if( identifier == TYPE_ROBOTO_REGULAR )
					return true;
				return false;
			}
		},		
		ROBOTO_THIN{
			@Override
			protected Typeface get( Context cxt ) {
				return Typeface.createFromAsset(cxt.getAssets(), "fonts/Roboto-Thin.ttf");
			}
			
			@Override
			protected boolean counterPart(int identifier) {
					if( identifier == TYPE_ROBOTO_THIN )
						return true;
					return false;
				}
		},
        ENTYPO{
            @Override
            protected Typeface get( Context cxt ) {
                return Typeface.createFromAsset(cxt.getAssets(), "fonts/Entypo.otf");
            }

            @Override
            protected boolean counterPart(int identifier) {
                if( identifier == TYPE_ENTYPO )
                    return true;
                return false;
            }
        };
		
		protected abstract Typeface get( Context cxt );
		protected abstract boolean counterPart( int identifier );
	
	};
		

	public static Typeface getTypeface( Context cxt ,Type type ){
		return type.get( cxt );		
	}
	
	
	public static Typeface getTypeface( Context cxt,int id ){
		for( Type t : Type.values() )
			if( t.counterPart( id ) )			
				return  t.get( cxt );
		return Typeface.SANS_SERIF;
	}
}
