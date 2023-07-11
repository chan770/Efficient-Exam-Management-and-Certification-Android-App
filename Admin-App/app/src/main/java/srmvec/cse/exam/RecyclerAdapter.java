package srmvec.cse.exam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecyclerAdapter extends FirebaseRecyclerAdapter<Recycler, RecyclerAdapter.PastViewHolder> {
    FirebaseFirestore db ;
    FirebaseStorage storage;
    StorageReference storageReference;

    public RecyclerAdapter(@NonNull FirebaseRecyclerOptions<Recycler> options, Context context) {
        super(options);
        db= FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }


    @Override
    protected void onBindViewHolder(@NonNull PastViewHolder holder, final int i, @NonNull final Recycler post) {
        String regnum123=post.getregnum();
        String name123=post.getname();
        long time=post.gettime();
        String marks123=post.getmarks();
        Date datest = new Date(time*1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfst = new SimpleDateFormat("dd-MM-yy hh.mm aa");
        sdfst.setTimeZone(java.util.TimeZone.getTimeZone("GMT+5:30"));
        String formattedDatest = sdfst.format(datest);



        holder.regnum.setText(regnum123);
        holder.name.setText(name123);
        holder.time.setText(formattedDatest);
        holder.marks.setText(marks123);



    }

    @NonNull
    @Override
    public PastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler, parent, false);
        return new PastViewHolder(view);
    }

    class PastViewHolder extends RecyclerView.ViewHolder{

        TextView regnum,name,time,marks;

        public PastViewHolder(@NonNull View itemView) {
            super(itemView);
            regnum = itemView.findViewById(R.id.regnum);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            marks = itemView.findViewById(R.id.marks);
        }
    }
}
