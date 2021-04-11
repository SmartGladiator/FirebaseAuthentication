package com.example.firebaseauthentication;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private LayoutInflater inflater;

    ArrayList<Integer> colorList;

    Context context;

    MyViewHolder myViewHolder;

    int matrixSize;

    boolean isScrumbled = false;

    int firstPos = -1, secondPos = -1;


    ImageView imgList[];

    public CustomAdapter(Activity context, ArrayList<Integer> colorList, int matrixSize) {
        this.matrixSize = matrixSize;
        //   super(context, 0, objects);
        inflater = LayoutInflater.from(context);
        this.colorList = colorList;
        this.context = context;

        imgList = new ImageView[matrixSize * matrixSize];
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View view = null;
            view = inflater.inflate(R.layout.gridview_items, parent, false);


            MyViewHolder holder = new MyViewHolder(view);


            return holder;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {
        try {
            myViewHolder = holder;

            imgList[pos] = holder.imgColor;


            if (!isScrumbled) {

                switch (pos % matrixSize) {
                    case 0:
                        myViewHolder.imgColor.setBackgroundResource(colorList.get(0));
                        break;
                    case 1:
                        myViewHolder.imgColor.setBackgroundResource(colorList.get(1));

                        break;
                    case 2:
                        myViewHolder.imgColor.setBackgroundResource(colorList.get(2));
                        break;
                    case 3:
                        myViewHolder.imgColor.setBackgroundResource(colorList.get(3));
                        break;

                    default:
                        break;
                }
            } else {


                for (int i = 0; i < matrixSize * matrixSize; i++) {

                    imgList[i].setBackgroundResource(colorList.get(i));


                }

            }


            holder.imgColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (firstPos == -1) {
                        imgList[pos].setImageResource(R.drawable.ic_round_check_24);
                        firstPos = pos;
                    } else if (firstPos >= 0) {
                        isScrumbled = true;
                        secondPos = colorList.get(pos);

                        colorList.set(pos, colorList.get(firstPos));
                        colorList.set(firstPos, secondPos);

                        imgList[firstPos].setImageResource(0);
                        firstPos = -1;


                        notifyDataSetChanged();
                    }

                }
            });


        } catch (Exception e) {
        }

    }

    @Override
    public int getItemCount() {

        return matrixSize * matrixSize;
    }

    public void scrumbleFn() {

        isScrumbled = true;

        Collections.shuffle(colorList);


    }


    class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView imgColor;


        RelativeLayout rlParent;

        public MyViewHolder(View itemView) {
            super(itemView);

            imgColor = itemView.findViewById(R.id.imgColor);
            rlParent = itemView.findViewById(R.id.rlParent);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 150);
            rlParent.setLayoutParams(params);


        }


    }


}




