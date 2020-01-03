package vnjp.monstarlaplifetime.apartmentssearch.data.repository

import com.google.android.gms.maps.model.LatLng
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Comment
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Room

interface RoomRepository {

    fun getRooms(
        onDataLoaded: ((List<Room>) -> Unit),
        onException: ((String) -> Unit)
    )
    fun getDetailRoom(
        id: String,
        onDataLoaded: ((Room) -> Unit),
        onException: ((String) -> Unit)
    )
    fun getPriceRange(
        onDataLoaded: (min : Float,max : Float) -> Unit,
        onException: (String) -> Unit
    )

    fun getRoomInRange(
        position: LatLng,
        range: Double = 10.toDouble(),
        onDataLoaded: ((HashMap<String, Room>) -> Unit),
        onException: ((String) -> Unit)
    )

    fun getComments(
        onCommentsLoaded: ((List<Comment>) -> Unit),
        onException: ((String) -> Unit)
    )

    fun addComment(
        roomKey: String,
        comment: Comment,
        onAddCommentResult: ((isSuccess: Boolean, message: String) -> Unit)
    )

    fun deleteComment(
        roomKey: String,
        commentKey: String,
        onDeleteResponse: ((isSuccess: Boolean, message: String) -> Unit)
    )

    fun updateComment(
        roomKey: String,
        commentKey: String,
        comment: Comment,
        onUpdateResponse: ((isSuccess: Boolean, message: String) -> Unit)
    )

}