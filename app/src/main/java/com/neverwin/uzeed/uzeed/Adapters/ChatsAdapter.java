package com.neverwin.uzeed.uzeed.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neverwin.uzeed.uzeed.Activities.MensajesActivity;
import com.neverwin.uzeed.uzeed.Model.Profesional;
import com.neverwin.uzeed.uzeed.Model.Usuario;
import com.neverwin.uzeed.uzeed.R;

import java.util.ArrayList;

public class ChatsAdapter extends RecyclerView.Adapter {

    private static final String USUARIO_RECEIVER = "USUARIO_RECEIVER";
    private static final String PROFESIONAL = "profesional";
    private final ArrayList<Profesional> mProfesionales;
    private Context mContext;

    public ChatsAdapter(ArrayList<Profesional>  profesionales)
    {
        mProfesionales = profesionales;


    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater li = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext =  viewGroup.getContext();
        View itemView = li.inflate(R.layout.chat_item,viewGroup,false);
        return new ChatsAdapter.ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final Profesional profesional = this.mProfesionales.get(i);
        final ChatViewHolder chatViewHolder = (ChatViewHolder) viewHolder;
        //chatViewHolder.tvInitialLetter.setText(usuario.getNombre().charAt(0));
        chatViewHolder.tvUserName.setText(profesional.getAlias());
        chatViewHolder.tvInitialLetter.setText(profesional.getAlias().substring(0,1).toUpperCase());

        chatViewHolder.llItemChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(getClass().getSimpleName(),"Entro en el item de chat");
                Intent intent = new Intent(chatViewHolder.llItemChat.getContext(), MensajesActivity.class);
                intent.putExtra(PROFESIONAL,profesional);
                chatViewHolder.llItemChat.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mProfesionales.size();
    }


    public class ChatViewHolder extends RecyclerView.ViewHolder
    {

        private final TextView tvInitialLetter;
        private final TextView tvUserName;
        private final LinearLayout llItemChat;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInitialLetter = (TextView) itemView.findViewById(R.id.tv_initial_letter);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_username);
            llItemChat = (LinearLayout) itemView.findViewById(R.id.ll_item_chat);
        }
    }
}
