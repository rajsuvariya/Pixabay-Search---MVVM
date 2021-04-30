package com.rajsuvariya.payudemo.injection

/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context

import com.rajsuvariya.payudemo.data.source.TasksDataSource
import com.rajsuvariya.payudemo.data.source.TasksRepository
import com.rajsuvariya.payudemo.data.source.local.TasksLocalDataSource
import com.rajsuvariya.payudemo.data.source.local.ToDoDatabase
import com.rajsuvariya.payudemo.data.source.remote.TasksRemoteDataSource
import com.rajsuvariya.payudemo.util.AppExecutors

/**
 * Enables injection of production implementations for
 * [TasksDataSource] at compile time.
 */
object Injection {

    fun provideTasksRepository(context: Context): TasksRepository {
        val database = ToDoDatabase.getInstance(context)
        return TasksRepository.getInstance(TasksRemoteDataSource,
                TasksLocalDataSource.getInstance(AppExecutors(), database.taskDao()))
    }
}