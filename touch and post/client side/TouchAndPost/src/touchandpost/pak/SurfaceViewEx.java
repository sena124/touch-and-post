//Copyright (c) 2014 sena. All rights reserved.
package touchandpost.pak;
import android.app.Activity;
import android.os.Bundle;
import android.graphics.PixelFormat;
import android.view.Window;


public class SurfaceViewEx extends Activity {
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(new SurfaceViewView(this));
    }
}