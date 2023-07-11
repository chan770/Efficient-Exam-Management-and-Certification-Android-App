package srmvec.cse.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.List;

public class ButtonAct extends AppCompatActivity {
    PowerSpinnerView spinnerView, spinnerView2;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide top
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_post_list);
        setContentView(R.layout.activity_button);
        button=findViewById(R.id.but);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://firebasestorage.googleapis.com/v0/b/onemarkexam.appspot.com/o/template%2FQuestion_formate.xlsx?alt=media&token=a2e469dc-89a9-46d2-bed3-4d5a100702b8"));
                startActivity(browserIntent);
            }
        });

        spinnerView = findViewById(R.id.spinnerView);
        spinnerView2=findViewById(R.id.spinnerView2);

        List<IconSpinnerItem> iconSpinnerItems = new ArrayList<>();
        iconSpinnerItems.add(new IconSpinnerItem(getResources().getDrawable(R.drawable.c), "C"));
        iconSpinnerItems.add(new IconSpinnerItem(getResources().getDrawable(R.drawable.cplus), "C++"));
        iconSpinnerItems.add(new IconSpinnerItem(getResources().getDrawable(R.drawable.python), "Python"));
        iconSpinnerItems.add(new IconSpinnerItem(getResources().getDrawable(R.drawable.java), "Java"));
        IconSpinnerAdapter iconSpinnerAdapter = new IconSpinnerAdapter(spinnerView);
        spinnerView.setSpinnerAdapter(iconSpinnerAdapter);
        spinnerView.setItems(iconSpinnerItems);
        spinnerView.setLifecycleOwner(this);
        IconSpinnerAdapter iconSpinnerAdapter2 = new IconSpinnerAdapter(spinnerView2);
        spinnerView2.setSpinnerAdapter(iconSpinnerAdapter2);
        spinnerView2.setItems(iconSpinnerItems);
        spinnerView2.setLifecycleOwner(this);

        spinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(int i, Object o) {
                switch (i)
                {
                    case 0:
                        Intent i1 = new Intent(getApplicationContext(), PostListActivity.class).putExtra("send", "c");
                        startActivity(i1);
                        break;

                    case 1:
                        Intent i2 = new Intent(getApplicationContext(), PostListActivity.class).putExtra("send", "c++");
                        startActivity(i2);
                        break;

                    case 2:
                        Intent i3 = new Intent(getApplicationContext(), PostListActivity.class).putExtra("send", "python");
                        startActivity(i3);
                        break;

                    case 3:
                        Intent i4 = new Intent(getApplicationContext(), PostListActivity.class).putExtra("send", "java");
                        startActivity(i4);
                        break;
                }
            }
        });

        spinnerView2.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(int i, Object o) {
                switch (i)
                {
                    case 0:
                        Intent i1 = new Intent(getApplicationContext(), RecyclerListActivity.class).putExtra("send", "c");
                        startActivity(i1);
                        break;

                    case 1:
                        Intent i2 = new Intent(getApplicationContext(), RecyclerListActivity.class).putExtra("send", "c++");
                        startActivity(i2);
                        break;

                    case 2:
                        Intent i3 = new Intent(getApplicationContext(), RecyclerListActivity.class).putExtra("send", "python");
                        startActivity(i3);
                        break;

                    case 3:
                        Intent i4 = new Intent(getApplicationContext(), RecyclerListActivity.class).putExtra("send", "java");
                        startActivity(i4);
                        break;
                }
            }
        });


    }

}
