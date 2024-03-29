package com.sgztech.checklist.di

import com.sgztech.checklist.database.AppDatabase
import com.sgztech.checklist.repository.CheckItemRepository
import com.sgztech.checklist.repository.CheckListRepository
import com.sgztech.checklist.viewModel.CheckItemViewModel
import com.sgztech.checklist.viewModel.CheckListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dbModule = module {
    single { AppDatabase.getInstance(
        context = get()
    )}
    factory { get<AppDatabase>().checkListDao() }
    factory { get<AppDatabase>().checkItemDao() }
}

val repositoryModule = module {
    single { CheckListRepository(get()) }
    single { CheckItemRepository(get()) }
}

val uiModule = module {
    viewModel { CheckListViewModel(get()) }
    viewModel { CheckItemViewModel(get()) }
}