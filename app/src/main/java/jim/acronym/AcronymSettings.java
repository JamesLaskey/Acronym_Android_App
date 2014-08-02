package jim.acronym;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import jim.acronym.R;

public class AcronymSettings extends Activity {
    protected static final String PREFS_NAME = "AcronymSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acronym_settings);

        final EditText apiEditText = (EditText) findViewById(R.id.acrSettingsApiKey);
        Button apiBtn = (Button) findViewById(R.id.acrSettingsApiKeyBtn);

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
        apiEditText.setText(preferences.getString("apiKey", ""));

        apiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("apiKey", apiEditText.getText().toString());
                editor.commit();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.acronym_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
}
