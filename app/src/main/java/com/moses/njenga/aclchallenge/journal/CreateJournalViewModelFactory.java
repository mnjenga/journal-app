package com.moses.njenga.aclchallenge.journal;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.moses.njenga.aclchallenge.journal.database.AppDatabase;

public class CreateJournalViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final int mJournalId;

    public CreateJournalViewModelFactory(AppDatabase database, int journalId) {
        mDb = database;
        mJournalId = journalId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new CreateJournalViewModel(mDb, mJournalId);
    }
}

