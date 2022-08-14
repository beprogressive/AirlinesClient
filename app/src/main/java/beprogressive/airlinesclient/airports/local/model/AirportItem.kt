package beprogressive.airlinesclient.airports.local.model

import androidx.room.*
import beprogressive.airlinesclient.airports.local.AirportsLocalSource
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "Airports")
data class AirportItem(
    @PrimaryKey val code: String,
    val name: String?,
    val countryName: String?,
    val countryCode: String?,
) {
    override fun equals(other: Any?): Boolean {
        if (other !is AirportItem) return false
        return code.lowercase() == other.code.lowercase()
    }

    override fun hashCode(): Int {
        var result = code.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (countryName?.hashCode() ?: 0)
        result = 31 * result + (countryCode?.hashCode() ?: 0)
        return result
    }
}

@Dao
abstract class AirportItemDao : AirportsLocalSource {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun insertAll(list: List<AirportItem>)

    @Query("DELETE FROM Airports")
    abstract override fun deleteAll()

    @Query("SELECT * FROM Airports ORDER BY countryName, name")
    abstract override fun observeAll(): Flow<List<AirportItem>>

    @Query("SELECT * FROM Airports WHERE code = :airportCode")
    abstract override suspend fun getAirport(airportCode: String): AirportItem?

    @Query("SELECT * FROM Airports WHERE code in (:codeList) ORDER BY countryName, name")
    abstract override suspend fun getAirportList(codeList: List<String>): List<AirportItem>

    @Query("SELECT * FROM Airports WHERE code in (:codeList) ORDER BY countryName, name")
    abstract override fun observeAirportList(codeList: List<String>): Flow<List<AirportItem>>

    @Query("DELETE FROM Airports WHERE code NOT IN (:validCodeList)")
    abstract override suspend fun deleteExpires(validCodeList: List<String>)
}