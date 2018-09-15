package frc.team4069.robot

import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import kotlin.reflect.KProperty

//TODO: I should be in SaturnShell
object NTConnection {
    val inst = NetworkTableInstance.getDefault()!!

    operator fun get(key: String): NetworkTableEntry = inst.getEntry(key)!!

    fun getTable(tableName: String) = inst.getTable(tableName)!!
}

operator fun NetworkTable.get(key: String) = getEntry(key)!!

@Suppress("UNCHECKED_CAST")
fun <T> NetworkTableEntry.delegate(defaultValue: T) = delegate { this.value.value as T }

fun <T> NetworkTableEntry.delegate(get: () -> T) = NTEntryDelegate(this, get)

class NTEntryDelegate<T>(private val entry: NetworkTableEntry, private val get: () -> T) {
    operator fun setValue(ref: Any, prop: KProperty<*>, value: T) {
        entry.setValue(value)
    }

    operator fun getValue(ref: Any, prop: KProperty<*>): T = get()
}