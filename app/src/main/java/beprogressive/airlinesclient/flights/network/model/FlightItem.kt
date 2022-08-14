package beprogressive.airlinesclient.flights.network.model

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import beprogressive.airlinesclient.flights.network.FlightsLocalSource
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "Flights")
data class FlightItem(
    val name: String,
    val originDate: String?,
    val destinationDate: String?,
    val originCode: String?,
    val originName: String?,
    val destinationCode: String?,
    val destinationName: String?,
    val routeTime: String?,
    var fares: Fares,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    }

object MapConverter {
    @JvmStatic
    @TypeConverter
    fun faresFromString(value: String): Fares {
        val mapType = object : TypeToken<Fares>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    @JvmStatic
    fun fromFare(fares: Fares): String {
        val gson = Gson()
        return gson.toJson(fares)
    }
}

class Fares {
    var adult = Adult(0, 0F, "")
    var teen = Teen(0, 0F, "")
    var child = Child(0, 0F, "")

    fun getTotalPrice(): Float {
        return adult.amount * adult.count + teen.amount * teen.count + child.amount * child.count
    }

    class Adult(val count: Int, val amount: Float, val currency: String)
    class Teen(val count: Int, val amount: Float, val currency: String)
    class Child(val count: Int, val amount: Float, val currency: String)
}

@Dao
abstract class FlightItemDao: FlightsLocalSource {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun insertAll(list: List<FlightItem>)

    @Query("DELETE FROM Flights")
    abstract override fun deleteAll()

    @Query("SELECT * FROM Flights")
    abstract override fun observeAll(): Flow<List<FlightItem>>
}