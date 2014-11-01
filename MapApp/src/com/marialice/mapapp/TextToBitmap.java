package com.marialice.mapapp;
/* 
 * This class creates the text (numbers) on the icons
 */
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class TextToBitmap extends Activity {
	
	private double displacement;
	private Typeface tf;

	// draw text over the icons - pois numbers
	public Bitmap drawTextToBitmap(Context context, int gResId, String gText, String gFont) {

		Resources resources = context.getResources();
		// the scale of the image is used to place the text relatively
		float scale = resources.getDisplayMetrics().density;
		Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);

		android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
		if (bitmapConfig == null) {
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		bitmap = bitmap.copy(bitmapConfig, true);
		
		// the typeface is created from assets, the correct font is chosen based on the input value
		if (gFont.equals("DINNextRounded")) {
			tf = Typeface.createFromAsset(context.getAssets(),"fonts/DINNextRounded.otf");
		} else {
			tf = Typeface.createFromAsset(context.getAssets(),	"fonts/Roboto-Light.ttf");
			;}
		
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.WHITE);
		paint.setTypeface(tf);
		paint.setTextSize((int) (13*scale));

		// here the values are set for the position of the text
		Rect bounds = new Rect();
		paint.getTextBounds(gText, 0, gText.length(), bounds);
		
		// defining displacement of the text when the text is only single number
		if (gText.length() > 1){ displacement = 0;}
		else {displacement = 3.5;}
		
		if (gFont.equals("DINNextRounded")) {
			int x = (int) ((9f+displacement) * context.getResources().getDisplayMetrics().density + 0.5f);
			int y = (int) ((15f) * context.getResources().getDisplayMetrics().density + 0.5f);
			canvas.drawText(gText, x , y , paint);
		} else {
			int x = (int) ((10f+displacement) * context.getResources().getDisplayMetrics().density + 0.5f);
			int y = (int) ((14f) * context.getResources().getDisplayMetrics().density + 0.5f);
			canvas.drawText(gText, x , y , paint);
		}

		// the complete bitmap (image + text) is returned
		return bitmap;
	}

}