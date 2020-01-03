package vnjp.monstarlaplifetime.apartmentssearch.data.repository

import vnjp.monstarlaplifetime.apartmentssearch.data.model.Comment
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Room

interface RoomRepository {

    fun getRooms(
        onDataLoaded: ((List<Room>) -> Unit),
        onException: ((String) -> Unit)
    )

    fun getDetailRoom(
        onDataLoaded: ((Room) -> Unit),
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