package com.github.johntinashe.hooked;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.johntinashe.hooked.model.Message;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<Message> mMessages;

    RecyclerViewAdapter(Context context, ArrayList<Message> messages) {
        mMessages = messages;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_message_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Message message = mMessages.get(i);
        viewHolder.lastMsg.setText(message.getLastMessage());
        viewHolder.username.setText(message.getUserName());

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                viewHolder.archiveBtn.setVisibility(View.VISIBLE);
                viewHolder.blockBtn.setVisibility(View.VISIBLE);
                viewHolder.bgView.setVisibility(View.VISIBLE);

                YoYo.with(Techniques.FadeInUp)
                        .duration(600)
                        .playOn(viewHolder.bgView);

                YoYo.with(Techniques.SlideInLeft)
                        .duration(400)
                        .playOn(viewHolder.archiveBtn);
                YoYo.with(Techniques.SlideInRight)
                        .duration(400)
                        .playOn(viewHolder.blockBtn);

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMessages != null ? mMessages.size() : 0;
    }

     class ViewHolder extends RecyclerView.ViewHolder {

         final TextView username;
         final TextView lastMsg;
         final Button blockBtn;
         final Button archiveBtn;
         final View bgView;

         ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            lastMsg = itemView.findViewById(R.id.last_message);
            blockBtn = itemView.findViewById(R.id.block_btn);
            archiveBtn = itemView.findViewById(R.id.archive_btn);
            bgView = itemView.findViewById(R.id.bg_view);
        }
    }
}
