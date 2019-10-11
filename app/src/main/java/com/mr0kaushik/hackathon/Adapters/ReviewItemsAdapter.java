package com.mr0kaushik.hackathon.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.mr0kaushik.hackathon.Model.ReviewModel;
import com.mr0kaushik.hackathon.R;

import java.util.List;


public class ReviewItemsAdapter extends RecyclerView.Adapter<ReviewItemsAdapter.ReviewViewHolder> {
    private static final String TAG = "ReviewItemsAdapter";


    private Context context;
    private List<ReviewModel> reviewModels;
    private ItemClickListener itemClickListener;

    public ReviewItemsAdapter(Context context, List<ReviewModel> reviewModels, ItemClickListener itemClickListener) {
        this.context = context;
        this.reviewModels = reviewModels;
        this.itemClickListener = itemClickListener;

    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.review_model, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewModel reviewModel = reviewModels.get(position);

        holder.title.setText(reviewModel.getTitle());
        holder.desc.setText(reviewModel.getDesc());

    }


    @Override
    public int getItemCount() {
        return reviewModels.size();
    }


    public interface ItemClickListener {
        void onItemClick();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView title, desc;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvTitle);
            desc = itemView.findViewById(R.id.tvDesc);

        }
    }

}
