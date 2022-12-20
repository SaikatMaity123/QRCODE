package com.mendine.qrcodegenscan;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    QRCodeWriter qrCodeWriter;
    BitMatrix bitMatrix;
    String title, name = "";
    int width, height;
    Bitmap bitmap;
    ImageView imageView;
    Button button, btn_submit, btn_generate, btn_gen_qr_code, btn_view;
    TextView name_tv, address_tv;
    DbHandler dataBaseHandler;
    EditText et_name, et_city;
    CardView card_table;
    TableLayout table_main;
    //qr code scanner object
    private IntentIntegrator qrScan;
    private LinearLayout ll_gen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image_view);
        name_tv = findViewById(R.id.name_tv);
        address_tv = findViewById(R.id.adress_txt);
        button = findViewById(R.id.scan);
        btn_submit = findViewById(R.id.btn_submit);*/

        setContentView(R.layout.main);
        table_main = findViewById(R.id.table_main);
        button = findViewById(R.id.btn_scan);
        name_tv = findViewById(R.id.name_tv);
        imageView = findViewById(R.id.image_view);
        btn_submit = findViewById(R.id.btn_submit);
        btn_generate = findViewById(R.id.btn_generate);
        btn_view = findViewById(R.id.btn_view);
        btn_gen_qr_code = findViewById(R.id.btn_gen_qr_code);
        ll_gen = findViewById(R.id.ll_gen);
        et_city = findViewById(R.id.et_city);
        et_name = findViewById(R.id.et_name);
        card_table = findViewById(R.id.card_table);

        dataBaseHandler = new DbHandler(this);


        /*imageView.setVisibility(View.GONE);
        address_tv.setVisibility(View.GONE);
        btn_submit.setAllCaps(false);*/

        qrCodeWriter = new QRCodeWriter();

        ll_gen.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        card_table.setVisibility(View.GONE);

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_gen.setVisibility(View.VISIBLE);

            }
        });

        btn_gen_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_gen.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Name", et_name.getText().toString());
                    jsonObject.put("Address", et_city.getText().toString());
                    /* jsonObject.put("Name", "Ranajeet");
                    jsonObject.put("Address", "OUAT");*/
                    title = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    bitMatrix = qrCodeWriter.encode(title, BarcodeFormat.QR_CODE, 512, 512);
                    width = bitMatrix.getWidth();
                    height = bitMatrix.getHeight();
                    bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            int color;
                            if (bitMatrix.get(i, j)) {
                                color = Color.BLACK;
                            } else {
                                color = Color.WHITE;
                            }
                            bitmap.setPixel(i, j, color);
                        }
                    }

                    imageView.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    e.printStackTrace();
                }


            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan = new IntentIntegrator(MainActivity.this);
                qrScan.setOrientationLocked(false);
                qrScan.initiateScan();
            }
        });

       /* if(name_tv.getText().toString().equals(""))
        {
            btn_submit.setVisibility(View.GONE);
            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        }
        else {
            btn_submit.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Hi", Toast.LENGTH_SHORT).show();
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }*/
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.equals("")) {
                    Toast.makeText(MainActivity.this, "Please Scan QR Code to submit", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean i = dataBaseHandler.insert_data(name);
                    if (i == true) {
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Not Success", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor t = dataBaseHandler.getinfo();
                if (t.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                } else {
                    card_table.setVisibility(View.VISIBLE);
                    while (t.moveToNext()) {
                        TableRow rows = new TableRow(MainActivity.this);
                        TextView col1 = new TextView(MainActivity.this);
                        col1.setText(t.getString(0));
                        col1.setPadding(5, 5, 5, 5);
                        col1.setBackgroundResource(R.drawable.table_border);
                        rows.addView(col1);

                        table_main.addView(rows, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    name_tv.setText(obj.getString("Name"));
                    address_tv.setText(obj.getString("Address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                    name_tv.setText(result.getContents());
                    name = name_tv.getText().toString();


                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyConstantData.cleanDB(MainActivity.this, dataBaseHandler);
    }
}
