package olivegumball.bira;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ariel on 1/29/16.
 */
public class SettingsActivity extends Activity {

    private Typeface alef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        alef = Typeface.createFromAsset(this.getAssets(), "Alef-Regular.ttf");

        ((EditText) findViewById(R.id.add_beer)).getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        ((EditText) findViewById(R.id.add_beer)).setTypeface(alef);

        ((TextView) findViewById(R.id.settings_title)).setTypeface(alef);
        ((TextView) findViewById(R.id.settings_description)).setTypeface(alef);
    }
}
