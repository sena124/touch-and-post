//Copyright (c) 2014 sena. All rights reserved.
package touchandpost.pak;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class SurfaceViewView extends SurfaceView 
implements SurfaceHolder.Callback,Runnable {
	private SurfaceHolder holder;
	private Thread        thread;


	private int touchX=0;
	private int touchY=0;
	private int touchAction=-999;
	private int ballX=0;
	private int ballY=0;
	private int ballAction=-999;
	
	
	ArrayList<Figure> figs = new ArrayList<Figure>();
    Figure temp_fig = new Figure();
    String result = "";
    JSONObject jsonObject;
    private MediaPlayer mp;
    
	public SurfaceViewView(Context context) {
		super(context);
		holder=getHolder();
		holder.addCallback(this);
		holder.setFixedSize(getWidth(),getHeight());
	}

	public void surfaceCreated(SurfaceHolder holder) {
		thread=new Thread(this);
		thread.start();
	}

	public void surfaceChanged(SurfaceHolder holder,
			int format,int w,int h) {
	}   

	public void surfaceDestroyed(SurfaceHolder holder) {
		thread=null;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		touchX=(int)event.getX();
		touchY=(int)event.getY();
		touchAction=event.getAction();
		return true;
	}

	@Override    
	public boolean onTrackballEvent(MotionEvent event) {
		ballX=(int)(event.getX()*100);
		ballY=(int)(event.getY()*100);
		ballAction=event.getAction();
		return true;
	}
	
	@Override 
	public void run() {
		Canvas canvas;
		while(thread!=null) {
			String str; 
			canvas=holder.lockCanvas();
			canvas.drawColor(Color.WHITE);
			Paint paint=new Paint();
			paint.setAntiAlias(true);
			paint.setStyle(Paint.Style.FILL);
			paint.setTextSize(32);
			str="NONE";
			
			//一時的なタッチポイントを得る
			if (touchAction==MotionEvent.ACTION_DOWN) {
				str="ACTION_DOWN";
				temp_fig.setX(touchX);
				temp_fig.setY(touchY);

				if (touchX<100 && touchY<100&& figs.size()>0){	

					//jsonに変換
					String postStr = "";
					postStr = postStr + "{\"fig\": [";
					if (figs.size()>0){
						for (int jj=0; jj<figs.size()-1; jj++) {
							postStr = postStr + "{";
							postStr = postStr +"\"r\":" + figs.get(jj).getR() + ",";
							postStr = postStr +"\"g\":" + figs.get(jj).getG() + ",";
							postStr = postStr +"\"b\":" + figs.get(jj).getB() + ",";
							postStr = postStr +"\"a\":" + figs.get(jj).getA() + ",";
							postStr = postStr + "\"varXY\": [";
							for (int ii=1; ii<figs.get(jj).getX().size()-1; ii++){
								postStr = postStr + "{";
								postStr = postStr + "\"varX\":" + figs.get(jj).getX().get(ii) + ",";
								postStr = postStr + "\"varY\":" + figs.get(jj).getY().get(ii) ;
								if (ii != figs.get(jj).getX().size()-2) {
									postStr = postStr + "},";
								} else if (ii == figs.get(jj).getX().size()-2) {
									postStr = postStr + "}";
								}
							}
							postStr = postStr + "]";
							if (jj !=figs.size()-2) {
								postStr = postStr + "},";
							} else if (jj ==figs.size()-2) {
								postStr = postStr + "}";
							}
						}
					}
					postStr = postStr + "]}";
					

					try {
						//サーバーに送信
						URL url = new URL("http://hogehoge/touch_and_post.php");
						HttpURLConnection http = (HttpURLConnection)url.openConnection();
						http.setRequestMethod("POST");
						http.setRequestProperty("Content-Type","application/octet-stream");
						http.setDoInput(true);
						http.setDoOutput(true);
						http.setUseCaches(false);
						http.connect();
						
						OutputStream out = new BufferedOutputStream(http.getOutputStream());
						jsonObject = new JSONObject(postStr);
						
						byte [] bytes = jsonObject.toString().getBytes("UTF-8");
						out.write(bytes);
						out.flush();
						out.close();

						InputStream in = new BufferedInputStream(http.getInputStream());
						BufferedReader reader = new BufferedReader(new InputStreamReader(in));
						result = reader.readLine();

						http.disconnect();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					mp = MediaPlayer.create(this.getContext(), R.raw.sound);
			        mp.start();

					//動画を送信
					paint.setColor(Color.GREEN);
					int mov_y = 1;
					int ff = 0;
					while(ff<50) {		
						if (figs.size()>0){
							for (int jj=0; jj<figs.size()-1; jj++) {
								for (int ii=1; ii<figs.get(jj).getX().size()-1; ii++){
									canvas.drawLine(figs.get(jj).getX().get(ii),figs.get(jj).getY().get(ii)-(int)(mov_y*(Math.pow(ff, 2))),figs.get(jj).getX().get(ii+1),figs.get(jj).getY().get(ii+1)-(int)(mov_y*(Math.pow(ff, 2))), paint);	
								}
							}
						}
					
						if (temp_fig.getX().size() > 1){
							for (int ii=1; ii<temp_fig.getX().size()-1; ii++){
								canvas.drawLine(temp_fig.getX().get(ii),temp_fig.getY().get(ii)-(int)(mov_y*(Math.pow(ff, 2))),temp_fig.getX().get(ii+1),temp_fig.getY().get(ii+1)-(int)(mov_y*(Math.pow(ff, 2))), paint);	
							}        
						}
						holder.unlockCanvasAndPost(canvas);
						try {
							Thread.sleep(50);
						} catch (Exception e) {
							e.printStackTrace();
						}
						canvas=holder.lockCanvas();
						ff++;
					}

					figs = new ArrayList<Figure>();
					temp_fig = new Figure();
				}
			}
			if (touchAction==MotionEvent.ACTION_MOVE) {
				str="ACTION_MOVE";

				temp_fig.setX(touchX);
				temp_fig.setY(touchY);

				if (figs.size()>0){
					for (int jj=0; jj<figs.size()-1; jj++) {
						for (int ii=1; ii<figs.get(jj).getX().size()-1; ii++){
							canvas.drawLine(figs.get(jj).getX().get(ii),figs.get(jj).getY().get(ii),figs.get(jj).getX().get(ii+1),figs.get(jj).getY().get(ii+1), paint);	
						}
					}
				}

				if (temp_fig.getX().size() > 1){
					for (int ii=1; ii<temp_fig.getX().size()-1; ii++){
						canvas.drawLine(temp_fig.getX().get(ii),temp_fig.getY().get(ii),temp_fig.getX().get(ii+1),temp_fig.getY().get(ii+1), paint);	
					}        
				}
			}
			if (touchAction==MotionEvent.ACTION_UP) {
				str="ACTION_UP";
				if (figs.size()>0){
					for (int jj=0; jj<figs.size()-1; jj++) {
						for (int ii=1; ii<figs.get(jj).getX().size()-1; ii++){
							canvas.drawLine(figs.get(jj).getX().get(ii),figs.get(jj).getY().get(ii),figs.get(jj).getX().get(ii+1),figs.get(jj).getY().get(ii+1), paint);	
						}
					}
				}
				
				
				if (temp_fig.getX().size() > 1){
					for (int ii=1; ii<temp_fig.getX().size()-1; ii++){
						canvas.drawLine(temp_fig.getX().get(ii),temp_fig.getY().get(ii),temp_fig.getX().get(ii+1),temp_fig.getY().get(ii+1), paint);	
					}        
				}
				paint.setARGB(50,0,255,0);
				canvas.drawRect(0, 0,100,100,paint);
				figs.add(temp_fig);
				temp_fig = new Figure(); 
			}
			if (touchAction==MotionEvent.ACTION_CANCEL) str="ACTION_CANCEL";

			str="NONE";
			if (ballAction==MotionEvent.ACTION_DOWN)   str="ACTION_DOWN";
			if (ballAction==MotionEvent.ACTION_MOVE) {
				str="ACTION_MOVE";
			}
			if (ballAction==MotionEvent.ACTION_UP) {
				str="ACTION_UP";
			}
			if (ballAction==MotionEvent.ACTION_CANCEL) str="ACTION_CANCEL";
			holder.unlockCanvasAndPost(canvas);
		}
	}
}