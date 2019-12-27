package vnjp.monstarlaplifetime.apartmentssearch.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Comment
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Room

class RoomRepositoryImpl(databaseReference: DatabaseReference) : RoomRepository {

    private val roomDatabaseReference: DatabaseReference = databaseReference

    override fun getRooms(
        onDataLoaded: ((List<Room>) -> Unit),
        onException: ((String) -> Unit)
    ) {
        roomDatabaseReference.orderByKey()
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    onException.invoke(p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val rooms = mutableListOf<Room>()
                    for (snapshot in p0.children) {
                        rooms.add(snapshot.getValue(Room::class.java)!!)
                    }
                    onDataLoaded.invoke(rooms)
                }
            })
    }

    override fun getComments(
        onCommentsLoaded: (List<Comment>) -> Unit,
        onException: (String) -> Unit
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addComment(
        roomKey: String,
        comment: Comment,
        onAddCommentResult: ((isSuccess: Boolean, message: String) -> Unit)
    ) {
        var message: String?
        var result: Boolean
        roomDatabaseReference
            .child(roomKey)
            .child("comments")
            .setValue(comment)
            .addOnSuccessListener {
                message = "Insert successfully"
                result = true
                onAddCommentResult.invoke(result, message!!)
            }.addOnFailureListener {
                message = it.message
                result = false
                onAddCommentResult.invoke(result, message!!)
            }
    }

    override fun deleteComment(
        roomKey: String,
        commentKey: String,
        onDeleteResponse: ((isSuccess: Boolean, message: String) -> Unit)
    ) {
        var message: String?
        var result: Boolean

        roomDatabaseReference
            .child(roomKey)
            .child(commentKey)
            .removeValue()
            .addOnSuccessListener {
                message = "Remove successfully"
                result = true
                onDeleteResponse.invoke(result, message!!)
            }.addOnFailureListener {
                message = it.message
                result = false
                onDeleteResponse.invoke(result, message!!)
            }
    }

    override fun updateComment(
        roomKey: String,
        commentKey: String,
        comment: Comment,
        onUpdateResponse: ((isSuccess: Boolean, message: String) -> Unit)
    ) {
        var message: String?
        var result: Boolean
        roomDatabaseReference
            .child(roomKey)
            .child(commentKey)
            .setValue(comment)
            .addOnSuccessListener {
                message = "Update successfully"
                result = true
                onUpdateResponse.invoke(result, message!!)
            }.addOnFailureListener {
                message = it.message
                result = false
                onUpdateResponse.invoke(result, message!!)
            }
    }

}