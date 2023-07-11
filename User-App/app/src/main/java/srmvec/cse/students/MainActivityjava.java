package srmvec.cse.students;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivityjava extends AppCompatActivity {

    EditText regnum,name, year,month,day;
    PowerSpinnerView spinnerView;
    private String mylang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide top
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mainjava);

        setTitle("Start Exam");


        regnum = findViewById(R.id.regnum);
        name = findViewById(R.id.name);
        year =findViewById(R.id.year);
        month=findViewById(R.id.month);
        day=findViewById(R.id.day);


        spinnerView = findViewById(R.id.spinnerView);
        year=findViewById(R.id.year);

        List<IconSpinnerItem> iconSpinnerItems = new ArrayList<>();
        iconSpinnerItems.add(new IconSpinnerItem(getResources().getDrawable(R.drawable.c), "C"));
        iconSpinnerItems.add(new IconSpinnerItem(getResources().getDrawable(R.drawable.cplus), "C++"));
        iconSpinnerItems.add(new IconSpinnerItem(getResources().getDrawable(R.drawable.python), "Python"));
        iconSpinnerItems.add(new IconSpinnerItem(getResources().getDrawable(R.drawable.java), "Java"));
        IconSpinnerAdapter iconSpinnerAdapter = new IconSpinnerAdapter(spinnerView);
        spinnerView.setSpinnerAdapter(iconSpinnerAdapter);
        spinnerView.setItems(iconSpinnerItems);
        spinnerView.setLifecycleOwner(this);

        spinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(int i, Object o) {
                switch (i)
                {
                    case 0:
                        mylang="c";
                        break;

                    case 1:
                        mylang="c++";
                        break;

                    case 2:
                        mylang="python";
                        break;

                    case 3:
                        mylang="java";
                        break;
                }
                checkDataEntered();
            }
        });


    }


    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    void checkDataEntered() {
        if (isEmpty(regnum)) {
            Toast.makeText(this, "You must enter the register number ", Toast.LENGTH_SHORT).show();
        }

        if (isEmpty(name)) {
            Toast.makeText(this, "You must enter the Name ", Toast.LENGTH_SHORT).show();
        }
        if (isEmpty(year)||isEmpty(month)||isEmpty(day)) {
            Toast.makeText(this, "You must enter the DOB ", Toast.LENGTH_SHORT).show();
        }
        if(!isEmpty(regnum)&&!isEmpty(name)&&!isEmpty(year)&&!isEmpty(month)&&!isEmpty(day)){
            senddata();
        }
    }

    private void senddata() {
        String y1=year.getText().toString();
        String m1=month.getText().toString();
        String d1=day.getText().toString();
        String regnumS = regnum.getText().toString();
        String nameS = name.getText().toString();
        String dobS = y1+"."+m1+"."+d1;
        if(Integer.parseInt(y1)<=2020&&Integer.parseInt(m1)<=12&&Integer.parseInt(d1)<=31&&Integer.parseInt(y1)>=1900){
            Userdata.setregnum(regnumS);
            Userdata.setname(nameS);
            Userdata.setdob(dobS);
            Userdata.setlang(mylang);
            startActivity(new Intent(getApplicationContext(), PostListActivity.class));
        }else{
            Toast.makeText(this, "Enter valid DOB ", Toast.LENGTH_SHORT).show();
        }
    }
}
