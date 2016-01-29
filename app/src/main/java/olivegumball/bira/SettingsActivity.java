package olivegumball.bira;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by ariel on 1/29/16.
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ((EditText) findViewById(R.id.add_beer)).getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);

    }
}
