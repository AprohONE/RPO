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
        String keyStr = "1234567890123456";
        byte[] key = keyStr.getBytes(StandardCharsets.US_ASCII);
        String dataStr = "Hello, world!";
        byte[] data = dataStr.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = encrypt(key, data);
        byte[] decrypted = decrypt(key, encrypted);
        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI() + "\n" + initRng() +
                "\n" + Arrays.toString(randomBytes(5)) + "\n" + Arrays.toString(encrypted) +
                "\n" + Arrays.toString(decrypted));
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
