package com.izv.android.proyectopaintfinal;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class Vista extends View implements ColorPickerDialog.OnColorChangedListener {

	private Path trazo;
	private Paint pincel, miCanvas;
	private int color = 0xFF660000;
	private Canvas lienzo;
	private Bitmap mapaDeBits;
	private float tamPincel, lasttamPincel;
	private boolean borrar=false;
    private Button btColor;

	public Vista(Context context, AttributeSet attrs){
		super(context, attrs);
		ajustesPincel();
	}

	private void ajustesPincel(){

		tamPincel = getResources().getInteger(R.integer.medium_size);
		lasttamPincel = tamPincel;
		trazo = new Path();
		pincel = new Paint();
		pincel.setColor(color);
		pincel.setAntiAlias(true);
		pincel.setStrokeWidth(tamPincel);
		pincel.setStyle(Paint.Style.STROKE);
		pincel.setStrokeJoin(Paint.Join.ROUND);
		pincel.setStrokeCap(Paint.Cap.ROUND);
		miCanvas = new Paint(Paint.DITHER_FLAG);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mapaDeBits = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		lienzo = new Canvas(mapaDeBits);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mapaDeBits, 0, 0, miCanvas);
		canvas.drawPath(trazo, pincel);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x0 = event.getX();
		float y0 = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			trazo.moveTo(x0, y0);
			break;
		case MotionEvent.ACTION_MOVE:
			trazo.lineTo(x0, y0);
			break;
		case MotionEvent.ACTION_UP:
			trazo.lineTo(x0, y0);
			lienzo.drawPath(trazo, pincel);
			trazo.reset();
			break;
		default:
			return false;
		}
		invalidate();
		return true;
	}

	public void setColor(String newColor){
		invalidate();
		color = Color.parseColor(newColor);
		pincel.setColor(color);
	}

	public void settamPincel(float newSize){
		float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				newSize, getResources().getDisplayMetrics());
		tamPincel=pixelAmount;
		pincel.setStrokeWidth(tamPincel);
	}


	public void setborrar(boolean isborrar){
		borrar=isborrar;
		if(borrar){
            this.setColor("#FFFFFFFF");
            //pincel.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
		else{
            pincel.setXfermode(null);
        }
	}

	public void nuevo(){
		lienzo.drawColor(0, PorterDuff.Mode.CLEAR);
		invalidate();
	}

    @Override
    public void colorChanged(int color) {
        pincel.setColor(color);
        //btColor.setBackgroundColor(color);
    }

    public void cambiarColor(){
        ColorPickerDialog color = new ColorPickerDialog(getContext(),this,Color.BLACK);
        color.show();
    }
}
