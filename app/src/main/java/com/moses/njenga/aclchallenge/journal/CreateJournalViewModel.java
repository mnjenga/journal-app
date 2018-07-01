package com.moses.njenga.aclchallenge.journal;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.moses.njenga.aclchallenge.journal.database.AppDatabase;
import com.moses.njenga.aclchallenge.journal.database.JournalEntry;


public class CreateJournalViewModel extends ViewModel {
    private LiveData<JournalEntry> journal;

    public CreateJournalViewModel(AppDatabase database, int journalId) {
        journal = database.journalDao().loadJournalById(journalId);
    }
    public LiveData<JournalEntry> getJournal() {
        return journal;
    }
}

