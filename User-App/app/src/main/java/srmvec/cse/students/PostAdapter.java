package srmvec.cse.students;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;


public class PostAdapter extends FirebaseRecyclerAdapter<Post, PostAdapter.PastViewHolder> {

    FirebaseDatabase database;
    Context mContext;



    public PostAdapter(@NonNull FirebaseRecyclerOptions<Post> options, Context context) {
        super(options);

        mContext = context;


    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull PastViewHolder holder, final int i, @NonNull final Post post) {
        String filename=post.getfilename();
        long time=post.gettime();
        Date datest = new Date(time*1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfst = new SimpleDateFormat("dd-MM-YYYY hh.mm aa");
        sdfst.setTimeZone(java.util.TimeZone.getTimeZone("GMT+5:30"));
        String formattedDatest = sdfst.format(datest);

        holder.st.setText("UPLOADED TIME   :  "+formattedDatest);
        holder.dur.setText("QUESTION PAPER  :  "+filename.substring(0, filename.length() - 5));




        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Userdata.setsfilename(filename);
                Intent intent = new Intent(mContext, QuestionActivity.class);
                mContext.startActivity(intent);
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

        TextView st,dur;
        ImageView start;


        
        public PastViewHolder(@NonNull View itemView) {
            super(itemView);

            database=  FirebaseDatabase.getInstance();
            dur = itemView.findViewById(R.id.filename);
            st = itemView.findViewById(R.id.uploadtime);
            start = itemView.findViewById(R.id.start);
            
        }
    }
}
