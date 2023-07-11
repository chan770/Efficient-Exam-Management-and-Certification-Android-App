package srmvec.cse.students;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView question, qCount, timer;
    private Button option1, option2, option3, option4;
    private List<Question> questionList;
    private int quesNum;
    private CountDownTimer countDown;
    private int score;
    private FirebaseFirestore firestore;
    private Dialog loadingDialog;
    private boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        //hide top
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_question);

        question = findViewById(R.id.question);
        qCount = findViewById(R.id.quest_num);
        timer = findViewById(R.id.countdown);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);

        loadingDialog = new Dialog(QuestionActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        check=true;
        firestore = FirebaseFirestore.getInstance();
        getQuestionsList();
        score = 0;
    }

    private void getQuestionsList()
    {
        questionList = new ArrayList<>();
        firestore.collection(Userdata.lang).document(Userdata.filename)
                .collection("question").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<QuerySnapshot> task) {
                          if (task.isSuccessful()) {
                              QuerySnapshot questions = task.getResult();
                              for (QueryDocumentSnapshot doc : questions) {
                                  questionList.add(new Question(
                                          doc.getString("QUESTION"),
                                          doc.getString("A"),
                                          doc.getString("B"),
                                          doc.getString("C"),
                                          doc.getString("D"),
                                          Integer.valueOf(doc.getString("ANSWER"))
                                          ));
                              }
                              setQuestion();
                          }
                          loadingDialog.dismiss();
                      }
                });
    }

    private void setQuestion()
    {
        timer.setText(String.valueOf(10));
        question.setText(questionList.get(0).getQuestion());
        option1.setText(questionList.get(0).getOptionA());
        option2.setText(questionList.get(0).getOptionB());
        option3.setText(questionList.get(0).getOptionC());
        option4.setText(questionList.get(0).getOptionD());

        qCount.setText(1 + "/" + questionList.size());

        startTimer();

        quesNum = 0;

    }

    private void startTimer()
    {
        countDown = new CountDownTimer(12000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished < 10000)
                    timer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFinish() {
                changeQuestion();
            }
        };
        countDown.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onClick(View view) {
        if(check){
            int selectedOption = 0;
            switch (view.getId())
            {
                case R.id.option1 :
                    selectedOption = 1;
                    break;

                case R.id.option2:
                    selectedOption = 2;
                    break;

                case R.id.option3:
                    selectedOption = 3;
                    break;

                case R.id.option4:
                    selectedOption = 4;
                    break;

                default:
            }

            check=false;
            countDown.cancel();
            checkAnswer(selectedOption, view);

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnswer(int selectedOption, View view)
    {

        if(selectedOption == questionList.get(quesNum).getCorrectAns())
        {
            //Right Answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#41c300")));
            score++;
        }
        else
        {
            //Wrong Answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#fa4659")));
            switch (questionList.get(quesNum).getCorrectAns())
            {
                case 1:
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#41c300")));
                    break;
                case 2:
                    option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#41c300")));
                    break;
                case 3:
                    option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#41c300")));
                    break;
                case 4:
                    option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#41c300")));
                    break;

            }
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                changeQuestion();
            }
        }, 3000);

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void changeQuestion()
    {
        if( quesNum < questionList.size() - 1)
        {
            quesNum++;

            playAnim(question,0,0);
            playAnim(option1,0,1);
            playAnim(option2,0,2);
            playAnim(option3,0,3);
            playAnim(option4,0,4);

            qCount.setText(String.valueOf(quesNum+1) + "/" + String.valueOf(questionList.size()));

            timer.setText(String.valueOf(10));
            startTimer();
            check=true;

        }
        else
        {
            String mydata= score + "/" + questionList.size();

            uploadtoDB(Userdata.regnum,Userdata.name,Userdata.dob,Userdata.lang,Userdata.filename,mydata);

            // Go to Score Activity
            Intent intent = new Intent(QuestionActivity.this,ScoreActivity.class);
            intent.putExtra("SCORE", mydata);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //QuestionActivity.this.finish();
        }
    }

    private void uploadtoDB(String regnumS,String nameS,String dobS,String lang,String filename,String mydata) {
        long unixTime = System.currentTimeMillis() / 1000L;
        Userdata.setet(unixTime);
        DatabaseReference mRef =  FirebaseDatabase.getInstance().getReference().child("student").child(lang).child(String.valueOf(unixTime));
        mRef.child("regnum").setValue(regnumS);
        mRef.child("name").setValue(nameS);
        mRef.child("dob").setValue(dobS);
        mRef.child("marks").setValue(mydata);
        mRef.child("filename").setValue(filename);
        mRef.child("time").setValue(unixTime);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void playAnim(final View view, final int value, final int viewNum) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (value == 0) {
                            switch (viewNum) {
                                case 0:
                                    ((TextView) view).setText(questionList.get(quesNum).getQuestion());
                                    break;
                                case 1:
                                    ((Button) view).setText(questionList.get(quesNum).getOptionA());
                                    break;
                                case 2:
                                    ((Button) view).setText(questionList.get(quesNum).getOptionB());
                                    break;
                                case 3:
                                    ((Button) view).setText(questionList.get(quesNum).getOptionC());
                                    break;
                                case 4:
                                    ((Button) view).setText(questionList.get(quesNum).getOptionD());
                                    break;

                            }
                            if (viewNum != 0)
                                ((Button) view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#606060")));


                            playAnim(view, 1, viewNum);

                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }


    @Override
    public void onBackPressed() { }
    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }
}
