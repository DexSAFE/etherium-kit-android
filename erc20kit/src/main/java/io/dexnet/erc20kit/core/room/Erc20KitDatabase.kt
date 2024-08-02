package io.dexnet.erc20kit.core.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.dexnet.erc20kit.models.TokenBalance
import io.dexnet.ethereumkit.api.storage.RoomTypeConverters

@Database(entities = [TokenBalance::class], version = 5, exportSchema = true)
@TypeConverters(RoomTypeConverters::class)
abstract class Erc20KitDatabase : RoomDatabase() {

    abstract val tokenBalanceDao: TokenBalanceDao

    companion object {

        fun getInstance(context: Context, databaseName: String): Erc20KitDatabase {
            return Room.databaseBuilder(context, Erc20KitDatabase::class.java, databaseName)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
        }
    }

}
