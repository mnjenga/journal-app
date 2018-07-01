package com.moses.njenga.aclchallenge.journal;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moses.njenga.aclchallenge.journal.database.JournalEntry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy HH:mm";

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    private List<JournalEntry> mJournalEntries;
    private Context mContext;
    // Date formatter

    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    public JournalAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }


    @Override
    public JournalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext).inflate(R.layout.journal_layout, parent, false);

        return new JournalViewHolder(view);
    }


    @Override
    public void onBindViewHolder(JournalViewHolder holder, int position) {
        // Determine the values of the wanted data
        JournalEntry journalEntry = mJournalEntries.get(position);
        String title = journalEntry.getTitle();
        String content = journalEntry.getContent();
        int mood = journalEntry.getMood();
        String createdAt = "Created at "+dateFormat.format(journalEntry.getCreatedAt());
        String updatedAt = "Updated at "+dateFormat.format(journalEntry.getUpdatedAt());

        //Set values
        holder.journalTitle.setText(title);
        holder.journalContent.setText(content);
        holder.createdAtView.setText(createdAt);
        holder.updatedAtView.setText(updatedAt);




        GradientDrawable moodCircle  = (GradientDrawable) holder.moodView.getBackground();
        // Get the appropriate background color based on the mood
        int moodColor = getMoodColor(mood);
        holder.journalTitle.setTextColor(moodColor);
        moodCircle.setColor(moodColor);

    }

    private int getMoodColor(int mood) {
        int moodColor =1;

        switch (mood) {
            case 1:
                moodColor = ContextCompat.getColor(mContext, R.color.joy);
                break;
            case 2:
                moodColor = ContextCompat.getColor(mContext, R.color.sad);
                break;
            case 3:
                moodColor = ContextCompat.getColor(mContext, R.color.anger);
                break;
            case 4:
                moodColor = ContextCompat.getColor(mContext, R.color.fear);
                break;
            case 5:
                moodColor = ContextCompat.getColor(mContext, R.color.disgust);
                break;
            default:
                break;
        }
        return moodColor;
    }

    @Override
    public int getItemCount() {
        if (mJournalEntries == null) {
            return 0;
        }
        return mJournalEntries.size();
    }

    public List<JournalEntry> getJournals() {
        return mJournalEntries;
    }


    public void setJournals(List<JournalEntry> journalEntries) {
        mJournalEntries = journalEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    // Inner class for creating ViewHolders
    class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView journalTitle;
        TextView updatedAtView;
        TextView moodView;
        TextView createdAtView;
        TextView journalContent;


        public JournalViewHolder(View itemView) {
            super(itemView);

            journalTitle = itemView.findViewById(R.id.journalTitle);
            updatedAtView = itemView.findViewById(R.id.journalUpdatedAt);
            moodView = itemView.findViewById(R.id.moodTextView);
            createdAtView = itemView.findViewById(R.id.journalCreatedAt);
            journalContent = itemView.findViewById(R.id.journalContent);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mJournalEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}
