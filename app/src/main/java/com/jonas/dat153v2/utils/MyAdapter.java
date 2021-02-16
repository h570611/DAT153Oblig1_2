package com.jonas.dat153v2.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.jonas.dat153v2.R;
import com.jonas.dat153v2.database.GameObject;


public class MyAdapter extends ListAdapter<GameObject, MyAdapter.MyViewHolder> {

    private OnItemClickListener mListener;

    public interface OnItemClickListener {

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView myText1;
        ImageView mDeleteImage, mCardImage;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            mCardImage = itemView.findViewById(R.id.CardImageView);
            myText1 = itemView.findViewById(R.id.CardName);
            mDeleteImage = itemView.findViewById(R.id.deleteCard);
            mDeleteImage.setOnClickListener(v -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onDeleteClick(position);
                    }
                }
            });


        }
    }

    public MyAdapter(){
        super(DIFF_CALLBACK);
    }


    private static final DiffUtil.ItemCallback<GameObject>  DIFF_CALLBACK = new DiffUtil.ItemCallback<GameObject>() {
        @Override
        public boolean areItemsTheSame(@NonNull GameObject oldItem, @NonNull GameObject newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull GameObject oldItem, @NonNull GameObject newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getImage() == newItem.getImage();
        }
    };



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_row, parent, false);
        MyViewHolder mVh = new MyViewHolder(view, mListener);

        return mVh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.myText1.setText(getItem(position).getName());
        byte[] imageAsByte = getItem(position).getImage();

        Bitmap bmp = BitmapFactory.decodeByteArray(imageAsByte, 0, imageAsByte.length);
        holder.mCardImage.setImageBitmap(bmp);
    }

    public GameObject getGameObjectAt(int position) {
        return getItem(position);
    }

}