package srmvec.cse.exam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class RecyclerListActivity extends AppCompatActivity {

    private Dialog loadingDialog,loadingDialog1;
    private RecyclerAdapter adapter;
    private String  TAG="print";
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore db ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide top
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recycler_list);

        db= FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String lang = intent.getExtras().getString("send");
        Log.i("print", lang);
        setTitle("User management : "+ lang);

        loadingDialog = new Dialog(RecyclerListActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        loadingDialog1 = new Dialog(RecyclerListActivity.this);
        loadingDialog1.setContentView(R.layout.nofile_progressbar);
        loadingDialog1.setCancelable(false);
        loadingDialog1.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog1.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        FirebaseRecyclerOptions<Recycler> options =
                new FirebaseRecyclerOptions.Builder<Recycler>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("student").child(lang), Recycler.class)
                        .build();

        adapter = new RecyclerAdapter(options,this);
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


}