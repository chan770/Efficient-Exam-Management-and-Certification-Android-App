package srmvec.cse.students;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class PostListActivity extends AppCompatActivity {

    private PostAdapter adapter;
    private Dialog loadingDialog,loadingDialog1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide top
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_post_list);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setTitle("Programing Language : "+ Userdata.lang);

        loadingDialog = new Dialog(PostListActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        loadingDialog1 = new Dialog(PostListActivity.this);
        loadingDialog1.setContentView(R.layout.nofile_progressbar);
        loadingDialog1.setCancelable(false);
        loadingDialog1.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog1.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        FirebaseRecyclerOptions<Post> options =
                new FirebaseRecyclerOptions.Builder<Post>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Exam").child(Userdata.lang), Post.class)
                        .build();

        adapter = new PostAdapter(options,this);
        recyclerView.setAdapter(adapter);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override public void run() {
                if (adapter.getItemCount() != 0) {
                    loadingDialog.cancel();
                } else {
                    loadingDialog.cancel();
                    loadingDialog1.show();
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override public void run() {
                            loadingDialog1.cancel();
                            onBackPressed();
                        }
                    }, 3000);
                }
            }
        }, 5000);


        }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
