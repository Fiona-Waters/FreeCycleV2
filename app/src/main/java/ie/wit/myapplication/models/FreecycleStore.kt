package ie.wit.myapplication.models

interface FreecycleStore {
    fun findAll(): List<FreecycleModel>
    fun create(listing: FreecycleModel)
    fun update(listing: FreecycleModel)
    fun delete(listing: FreecycleModel)
}