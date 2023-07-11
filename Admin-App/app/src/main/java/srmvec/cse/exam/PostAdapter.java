package srmvec.cse.exam;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class PostAdapter extends FirebaseRecyclerAdapter<Post, PostAdapter.PastViewHolder> {
    private Context context;
    FirebaseFirestore db ;
    FirebaseStorage storage;
    StorageReference storageReference;

    public PostAdapter(@NonNull FirebaseRecyclerOptions<Post> options,Context context) {
        super(options);
        this.context = context;
        db= FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }


    @Override
    protected void onBindViewHolder(@NonNull PastViewHolder holder, final int i, @NonNull final Post post) {
        String filename=post.getfilename();
        String lang=post.getlang();
        long time=post.gettime();
        String fileurl=post.getfileurl();
        Date datest = new java.util.Date(time*1000L);
        SimpleDateFormat sdfst = new java.text.SimpleDateFormat("dd-MM-YYYY hh.mm aa");
        sdfst.setTimeZone(java.util.TimeZone.getTimeZone("GMT+5:30"));
        String formattedDatest = sdfst.format(datest);

        holder.st.setText(formattedDatest);
        holder.dur.setText(filename);
        holder.et.setText(fileurl);

        holder.et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileurl));
                context.startActivity(browserIntent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection(lang).document(filename).delete();
                db.collection(lang).document(filename).collection("question").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String temp= document.getId();
                                db.collection(lang).document(filename).collection("question").document(temp).delete();
                                Log.i("TAG",temp);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
                storage.getReferenceFromUrl(fileurl).delete();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.child("Exam").child(lang).orderByChild("filename").equalTo(filename);
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("TAG", "onCancelled", databaseError.toException());
                    }
                });
            }
        });


    }

    @NonNull
    @Override
    public PastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post, parent, false);
        return new PastViewHolder(view);
    }

    class PastViewHolder extends RecyclerView.ViewHolder{

        TextView st,et,dur;
        ImageView delete;


        
        public PastViewHolder(@NonNull View itemView) {
            super(itemView);
            dur = itemView.findViewById(R.id.filename);
            st = itemView.findViewById(R.id.uploadtime);
            et = itemView.findViewById(R.id.url123);
            delete = itemView.findViewById(R.id.delete);


            
        }
    }
}
