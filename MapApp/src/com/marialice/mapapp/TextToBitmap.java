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

	// draw text over the icons - pois numbers
	public Bitmap drawTextToBitmap(Context context, int gResId, String gText) {

		Resources resources = context.getResources();
		// the scale of the image is used to place the text relatively
		float scale = resources.getDisplayMetrics().density;
		Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);

		android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
		if (bitmapConfig == null) {
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		bitmap = bitmap.copy(bitmapConfig, true);

		// the typeface is created from assets
		Typeface tf = Typeface.createFromAsset(context.getAssets(),
				"fonts/DINNextRounded.otf");

		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.WHITE);
		paint.setTypeface(tf);
		paint.setTextSize((int) (13 * scale));
		// paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

		// here the values are set for the position of the text
		Rect bounds = new Rect();
		paint.getTextBounds(gText, 0, gText.length(), bounds);
		int x = (bitmap.getWidth() - bounds.width()) / 4;
		int y = (bitmap.getHeight() + bounds.height()) / 6;

		canvas.drawText(gText, x * scale, y * scale, paint);

		// the complete bitmap (image + text) is returned
		return bitmap;
	}

}
