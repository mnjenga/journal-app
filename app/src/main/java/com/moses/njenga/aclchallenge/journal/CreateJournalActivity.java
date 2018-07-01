package com.moses.njenga.aclchallenge.journal;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.moses.njenga.aclchallenge.journal.database.AppDatabase;
import com.moses.njenga.aclchallenge.journal.database.JournalEntry;

import java.util.Date;


public class CreateJournalActivity extends AppCompatActivity {


    public static final String EXTRA_JOURNAL_ID = "extraJournalId";

    public static final String INSTANCE_JOURNAL_ID = "instanceJournalId";
    // Constants for mood
    public static final int MOOD_HAPPY = 1;
    public static final int MOOD_SAD = 2;
    public static final int MOOD_ANGRY = 3;
    public static final int MOOD_FEARFUL = 4;
    public static final int MOOD_DISGUSTED = 5;
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_JOURNAL_ID = -1;
    // Constant for logging
    private static final String TAG = CreateJournalActivity.class.getSimpleName();
    // Fields for views
    EditText mEditTitle;
    EditText mEditContent;
    Date mCreatedAt;
    RadioGroup mRadioGroup;
    Button mButton;

    private int mJournalId = DEFAULT_JOURNAL_ID;

    // Member variable for the Database
    private AppDatabase mDb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_journal);

        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_JOURNAL_ID)) {
            mJournalId = savedInstanceState.getInt(INSTANCE_JOURNAL_ID, DEFAULT_JOURNAL_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)) {
            mButton.setText(R.string.update_button);
            if (mJournalId == DEFAULT_JOURNAL_ID) {
                // populate the UI
                mJournalId = intent.getIntExtra(EXTRA_JOURNAL_ID, DEFAULT_JOURNAL_ID);
                CreateJournalViewModelFactory factory = new CreateJournalViewModelFactory(mDb, mJournalId);
                final CreateJournalViewModel viewModel =
                        ViewModelProviders.of(this, factory).get(CreateJournalViewModel.class);
                viewModel.getJournal().observe(this, new Observer<JournalEntry>() {
                    @Override
                    public void onChanged(@Nullable JournalEntry journalEntry) {
                        viewModel.getJournal().removeObserver(this);
                        populateUI(journalEntry);
                    }
                });
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_JOURNAL_ID, mJournalId);
        super.onSaveInstanceState(outState);
    }


    private void initViews() {
        mEditTitle = findViewById(R.id.editTitle);
        mEditContent = findViewById(R.id.editContent);
        mRadioGroup = findViewById(R.id.radioGroup);
        mButton = findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param journal the journalEntry to populate the UI
     */
    private void populateUI(JournalEntry journal) {
        if (journal == null) {
            return;
        }

        mEditTitle.setText(journal.getTitle());
        mEditContent.setText(journal.getContent());
        mEditContent.setTag(journal.getCreatedAt());
        setMoodInViews(journal.getMood());
    }

    public void onSaveButtonClicked() {
        String title = mEditTitle.getText().toString();
        String content = mEditContent.getText().toString();
        int mood = getMoodFromViews();
        Date dateUpdated = new Date();
        Date dateCreated = new Date();

        final JournalEntry journal = new JournalEntry(title,content, dateCreated, mood, dateUpdated);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mJournalId == DEFAULT_JOURNAL_ID) {
                    // insert new journal
                    mDb.journalDao().insertJournal(journal);
                } else {
                    //update journal
                    mCreatedAt = (Date) mEditContent.getTag();
                    journal.setCreatedAt(mCreatedAt);
                    journal.setId(mJournalId);
                    mDb.journalDao().updateJournal(journal);
                }
                finish();
            }
        });
    }


    public int getMoodFromViews() {
        int mood = 1;
        int checkedId = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButton1:
                mood = MOOD_HAPPY;
                break;
            case R.id.radButton2:
                mood = MOOD_SAD;
                break;
            case R.id.radButton3:
                mood = MOOD_ANGRY;
                break;
            case R.id.radButton4:
                mood = MOOD_FEARFUL;
                break;
            case R.id.radButton5:
                mood = MOOD_DISGUSTED;
        }
        return mood;
    }

    /**
     * setMood is called when we receive a journal from MainActivity
     *
     * @param mood the priority value
     */
    public void setMoodInViews(int mood) {
        switch (mood) {
            case MOOD_HAPPY:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton1);
                break;
            case MOOD_SAD:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton2);
                break;
            case MOOD_ANGRY:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton3);
                break;
            case MOOD_FEARFUL:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton4);
                break;
            case MOOD_DISGUSTED:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton5);
        }
    }
}

