package com.example.lab1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab1.databinding.ActivityMainBinding;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class MainActivity extends AppCompatActivity implements TransactionEvents {

    // Used to load the 'lab1' library on application startup.
    static {
        System.loadLibrary("lab1");
        System.loadLibrary("mbedcrypto");
    }

    private ActivityMainBinding binding;
    ActivityResultLauncher activityResultLauncher;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRng();
        activityResultLauncher  = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            //String pin = data.getStringExtra("pin");
                            //Toast.makeText(MainActivity.this, pin, Toast.LENGTH_SHORT).show();
                            pin = data.getStringExtra("pin");
                            synchronized (MainActivity.this) {
                                MainActivity.this.notifyAll();
                            }
                        }
                    }
                });
//        String keyStr = "1234567890123456";
//        byte[] key = keyStr.getBytes(StandardCharsets.US_ASCII);
//        String dataStr = "HelloSt";
//        byte[] data = dataStr.getBytes(StandardCharsets.UTF_8);
//        byte[] encrypted = encrypt(key, data);
//        byte[] decrypted = decrypt(key, encrypted);
//        // Example of a call to a native method
//        TextView tv = findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI() + "\n" + initRng() + "\n" + Arrays.toString(data) +
//                "\n" + Arrays.toString(randomBytes(5)) + "\n" + Arrays.toString(encrypted) +
//                "\n" + Arrays.toString(decrypted));
    }

//    public void onButtonClick(View v)
//    {
//        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
//    }

//    public void onButtonClick(View v)
//    {
//        byte[] key = stringToHex("0123456789ABCDEF0123456789ABCDE0");
//        byte[] enc = encrypt(key, stringToHex("000000000000000102"));
//        byte[] dec = decrypt(key, enc);
//        String s = new String(Hex.encodeHex(dec)).toUpperCase();
//        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
//    }

    public void onButtonClick(View v)
    {
//        new Thread(()-> {
//            try {
//                byte[] trd = stringToHex("9F0206000000000100");
//                boolean ok = transaction(trd);
//                runOnUiThread(()-> {
//                    Toast.makeText(MainActivity.this, ok ? "ok" : "failed", Toast.LENGTH_SHORT).show();
//                });
//
//            } catch (Exception ex) {
//                // todo: log error
//            }
//        }).start();
        byte[] trd = stringToHex("9F0206000000000100");
        transaction(trd);
//        Intent it = new Intent(this, PinpadActivity.class);
//        startActivity(it);
//        activityResultLauncher.launch(it);
    }

    @Override
    public void transactionResult(boolean result) {
        runOnUiThread(()-> {
            Toast.makeText(MainActivity.this, result ? "ok" : "failed", Toast.LENGTH_SHORT).show();
        });
    }


    public static byte[] stringToHex(String s) {
        byte[] hex;
        try {
            hex = Hex.decodeHex(s.toCharArray());
        } catch (DecoderException ex) {
            hex = null;
        }
        return hex;
    }


    /**
     * A native method that is implemented by the 'lab1' native library,
     * which is packaged with this application.
     */
    private String pin;

    @Override
    public String enterPin(int ptc, String amount) {
        pin = new String();
        Intent it = new Intent(MainActivity.this, PinpadActivity.class);
        it.putExtra("ptc", ptc);
        it.putExtra("amount", amount);
        synchronized (MainActivity.this) {
            activityResultLauncher.launch(it);
            try {
                MainActivity.this.wait();
            } catch (Exception ex) {
                //todo: log error
            }
        }
        return pin;
    }
    public native String stringFromJNI();
    public native boolean transaction(byte[] trd);
    public static native int initRng();

    public static native byte[] randomBytes(int no);

    public static native byte[] encrypt(byte[] key, byte[] data);

    public static native byte[] decrypt(byte[] key, byte[] data);
}