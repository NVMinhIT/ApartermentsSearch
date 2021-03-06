package vnjp.monstarlaplifetime.apartmentssearch.data.model


import com.google.firebase.firestore.PropertyName

data class Room(
    @PropertyName("address")
    var address: Address? = null,
    @PropertyName("amenities")
    var amenities: List<String>? = null,
    @PropertyName("cancellations")
    var cancellations: String? = null,
    @PropertyName("comments")
    var comments: HashMap<String, Comment>? = null,
    @PropertyName("description")
    var description: String? = null,
    @PropertyName("id")
    var id: Int? = null,
    @PropertyName("image")
    var image: String? = null,
    @PropertyName("name")
    var name: String? = null,
    @PropertyName("nearby_landmark")
    var nearby_landmark: List<NearbyLandmark>? = null,
    @PropertyName("price")
    var price: Float? = null,
    @PropertyName("rules")
    var rules: ArrayList<String>? = null
)
