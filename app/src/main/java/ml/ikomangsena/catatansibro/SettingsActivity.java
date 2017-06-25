package ml.ikomangsena.catatansibro;

import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean apaSound;
    public static final int FAST = 0;
    public static final int SLOW = 1;
    public static final int NONE = 2;
    private int apaAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        CheckBox cbSound = (CheckBox) findViewById(R.id.cbSound);
        RadioGroup rGroup = (RadioGroup) findViewById(R.id.rGroup);

        sharedPreferences = getSharedPreferences("Catatan Si Bro", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        apaSound = sharedPreferences.getBoolean("sound", true);
        if(apaSound){
            cbSound.setChecked(true);
        } else{
            cbSound.setChecked(false);
        }

        apaAnimation = sharedPreferences.getInt("anim option", SLOW);
        rGroup.clearCheck();
        switch (apaAnimation){
            case FAST: rGroup.check(R.id.rbFast); break;
            case SLOW: rGroup.check(R.id.rbSlow); break;
            case NONE: rGroup.check(R.id.rbNone); break;
        }

        cbSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("sound = ", ""+apaSound);
                Log.i("isChecked = ", ""+isChecked);

                apaSound = !apaSound;
                editor.putBoolean("sound", apaSound);
            }
        });

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton rbTerpilih = (RadioButton) group.findViewById(checkedId);
                if (null != rbTerpilih && checkedId > -1){
                    switch (rbTerpilih.getId()){
                        case R.id.rbFast: apaAnimation = FAST; break;
                        case R.id.rbSlow: apaAnimation = SLOW; break;
                        case R.id.rbNone: apaAnimation = NONE; break;
                    }
                    editor.putInt("anim option", apaAnimation);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor.commit();
    }
}
