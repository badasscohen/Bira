package olivegumball.bira;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ariel on 1/29/16.
 */
public class SettingsActivity extends Activity {

    private Typeface alef;
    private String beers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        beers = "";
        alef = Typeface.createFromAsset(this.getAssets(), "Alef-Regular.ttf");

        final EditText add_beer = (EditText) findViewById(R.id.add_beer);
        add_beer.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        add_beer.setTypeface(alef);

        ((TextView) findViewById(R.id.settings_title)).setTypeface(alef);
        ((TextView) findViewById(R.id.settings_description)).setTypeface(alef);

        FloatingActionButton more = (FloatingActionButton) findViewById(R.id.add_button);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beers = beers.concat(add_beer.getText().toString() + ",");
                add_beer.setText("");
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(beers.length() > 0) {
                    editor.putInt("first", 0);
                    editor.putString("beers", beers.substring(0, beers.length() - 1));
                    editor.commit();
                    Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(i);
                    SettingsActivity.this.finish();
                }
            }
        });
    }
}
