package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.lab1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'lab1' library on application startup.
    static {
        System.loadLibrary("lab1");
        System.loadLibrary("mbedcrypto");
    }
    
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI() + "\n" + initRng() +
                "\n" + Arrays.toString(randomBytes(5)) + "\n" + Arrays.toString(encrypt(new byte[]{7, 9}, new byte[]{7, 8, 47})) +
                "\n" + Arrays.toString(decrypt(new byte[]{7, 9, 3}, new byte[]{7, 8})));
    }


    /**
     * A native method that is implemented by the 'lab1' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public static native int initRng();

    public static native byte[] randomBytes(int no);
    public static native byte[] encrypt(byte[] key, byte[] data);
    public static native byte[] decrypt(byte[] key, byte[] data);
}
