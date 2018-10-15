package com.example.pageturning;

import java.io.IOException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Toast;
import com.example.pageturning.util.BookPageFactory;
import com.example.pageturning.widget.PageTurningWidgetCompleteTest;

public class PageTurningCompleteTest extends Activity {

	private PageTurningWidgetCompleteTest mPageWidget;
	Bitmap mCurPageBitmap, mNextPageBitmap;
	Canvas mCurPageCanvas, mNextPageCanvas;
	BookPageFactory pagefactory;

	@SuppressLint("SdCardPath")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		mPageWidget = new PageTurningWidgetCompleteTest(this);
		setContentView(mPageWidget);

		/* ������ҳ */
		mCurPageBitmap = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
		mNextPageBitmap = Bitmap
				.createBitmap(480, 800, Bitmap.Config.ARGB_8888);

		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);
		/* ���������� */
		/* ������ҳ�ı��� */
		pagefactory = new BookPageFactory(480, 800);
		pagefactory.setBgBitmap(BitmapFactory.decodeResource(
				this.getResources(), R.drawable.bg));
		try {
			pagefactory.openbook("/sdcard/test.txt");
			pagefactory.Draw(mCurPageCanvas);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Toast.makeText(this, "�����鲻���ڣ��뽫��test.txt������SD����Ŀ¼�¡�",
					Toast.LENGTH_SHORT).show();
		}
		/* ���ò����ĵ�ǰҳ����һҳ����ʼ��ʱ����ǰҳ����һҳ��ͬ */
		/* ���ò����Ĵ������¼� */
		mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
		mPageWidget.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent e) {
				boolean ret = false;
				if (v == mPageWidget) {
					/* �����´���ʱ */
					if (e.getAction() == MotionEvent.ACTION_DOWN) {
						mPageWidget.abortAnimation();
						mPageWidget.calcCornerXY(e.getX(), e.getY());
						pagefactory.Draw(mCurPageCanvas);
						/* �����´���ʱ���ж������ң��������� */
						/* �����ң�����ǰ��ҳ���������ҳ */
						if (mPageWidget.DragToRight()) { // ���
							try {
								pagefactory.prePage();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							if (pagefactory.isfirstPage())
								return false;
							pagefactory.Draw(mNextPageCanvas);
						} else {
							try {
								pagefactory.nextPage();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (pagefactory.islastPage())
								return false;
							pagefactory.Draw(mNextPageCanvas);
						}
						/* ���ò����ĵ�ǰҳ����һҳ */
						mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
					}

					ret = mPageWidget.onTouchEvent(e);
					return ret;
				}
				return false;
			}
		});
	}
}