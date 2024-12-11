package me.ztiany.wifi.kit.arch

/**
 * @author Ztiany
 */
interface DataManager<T> {

    fun add(element: T)

    fun addAt(location: Int, element: T)

    fun addItems(elements: List<T>)

    /**
     * Before adding elements, the equals method will be used for comparison.
     * If the element already exists in the collection, it will not be added.
     */
    fun addItemsDistinguished(elements: List<T>)

    fun addItemsAt(location: Int, elements: List<T>)

    fun replace(oldElement: T, newElement: T)

    fun replaceAt(index: Int, element: T)

    /**
     * Clear the previous data in the collection and add the elements to the previous collection, not using elements as the data source.
     */
    fun replaceAll(elements: List<T>)

    /**
     * This method will directly replace the previous data source with [newDataSource].
     */
    fun setDataSource(newDataSource: MutableList<T>)

    fun swipePosition(fromPosition: Int, toPosition: Int)

    fun remove(element: T): Boolean

    fun removeIf(filter: (T) -> Boolean)

    fun removeAt(index: Int)

    fun removeItems(elements: List<T>)

    fun removeItems(elements: List<T>, isSuccessive: Boolean)

    fun getItem(position: Int): T?

    operator fun contains(element: T): Boolean

    fun clear()

    /**
     * @return -1 if not contains this element.
     */
    fun indexItem(element: T): Int

    fun notifyElementChanged(element: T)

    fun isEmpty(): Boolean

    fun getList(): List<T>

    fun getDataSize(): Int

}

fun <T> DataManager<T>.requireItem(position: Int): T {
    return getItem(position) ?: throw NullPointerException("There is no item for position ${position}.")
}