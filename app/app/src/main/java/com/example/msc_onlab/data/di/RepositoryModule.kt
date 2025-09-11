package com.example.msc_onlab.data.di

import android.app.Application
import com.example.msc_onlab.data.remote.HouseholdApi
import com.example.msc_onlab.data.remote.InvitationApi
import com.example.msc_onlab.data.remote.LoginApi
import com.example.msc_onlab.data.remote.ProfileApi
import com.example.msc_onlab.data.remote.RegisterApi
import com.example.msc_onlab.data.remote.TaskApi
import com.example.msc_onlab.data.remote.TeamApi
import com.example.msc_onlab.data.repository.household.HouseholdRepository
import com.example.msc_onlab.data.repository.household.HouseholdRepositoryImpl
import com.example.msc_onlab.data.repository.invitation.InvitationRepository
import com.example.msc_onlab.data.repository.invitation.InvitationRepositoryImpl
import com.example.msc_onlab.data.repository.login.LoginRepository
import com.example.msc_onlab.data.repository.login.LoginRepositoryImpl
import com.example.msc_onlab.data.repository.profile.ProfileRepository
import com.example.msc_onlab.data.repository.profile.ProfileRepositoryImpl
import com.example.msc_onlab.data.repository.register.RegisterRepository
import com.example.msc_onlab.data.repository.register.RegisterRepositoryImpl
import com.example.msc_onlab.data.repository.task.TaskRepository
import com.example.msc_onlab.data.repository.task.TaskRepositoryImpl
import com.example.msc_onlab.data.repository.team.TeamRepository
import com.example.msc_onlab.data.repository.team.TeamRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(
        loginApi: LoginApi,
        app: Application,
    ): LoginRepository {
        return LoginRepositoryImpl(
            api = loginApi,
            app = app
        )
    }

    @Provides
    @Singleton
    fun provideRegisterRepository(
        registerApi: RegisterApi,
        app: Application
    ): RegisterRepository {
        return RegisterRepositoryImpl(
            api = registerApi,
            app = app
        )
    }

    @Provides
    @Singleton
    fun provideHouseholdRepository(
        householdApi: HouseholdApi,
        app: Application
    ): HouseholdRepository {
        return HouseholdRepositoryImpl(
            api = householdApi,
            app = app
        )
    }

    @Provides
    @Singleton
    fun provideTeamRepository(
        teamApi: TeamApi,
        app: Application
    ): TeamRepository {
        return TeamRepositoryImpl(
            api = teamApi,
            app = app
        )
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        profileApi: ProfileApi,
        app: Application
    ): ProfileRepository {
        return ProfileRepositoryImpl(
            api = profileApi,
            app = app
        )
    }

    @Provides
    @Singleton
    fun provideInvitationRepository(
        invitationApi: InvitationApi,
        app: Application
    ): InvitationRepository {
        return InvitationRepositoryImpl(
            api = invitationApi,
            app = app
        )
    }

    @Provides
    @Singleton
    fun provideTaskRepository(
        taskApi: TaskApi,
        app: Application
    ): TaskRepository {
        return TaskRepositoryImpl(
            api = taskApi,
            app = app
        )
    }
}
