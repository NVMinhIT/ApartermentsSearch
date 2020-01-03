package vnjp.monstarlaplifetime.apartmentssearch.data.repository

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Comment
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Room
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Util
import kotlin.collections.HashMap
import kotlin.collections.set

class RoomRepositoryImpl(databaseReference: DatabaseReference) : RoomRepository {

    private val roomDatabaseReference: DatabaseReference = databaseReference

    override fun getRooms(
        onDataLoaded: ((List<Room>) -> Unit),
        onException: ((String) -> Unit)
    ) {
        roomDatabaseReference
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

    override fun getRoomInRange(
        position: LatLng,
        range: Double,
        onDataLoaded: (HashMap<String, Room>) -> Unit,
        onException: (String) -> Unit
    ) {
        roomDatabaseReference
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    onException.invoke(p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val rooms = HashMap<String, Room>()
                    for (snapshot in p0.children) {
                        val room = snapshot.getValue(Room::class.java)!!
                        if (Util.calculationByDistance(
                                position,
                                LatLng(room.address?.latitude!!, room.address?.longtitude!!)
                            ) <= range
                        ) rooms[snapshot.key!!] = room
                    }
                    onDataLoaded.invoke(rooms)
                }
            })
    }

    override fun getDetailRoom(
        id: String,
        onDataLoaded: (Room) -> Unit,
        onException: (String) -> Unit
    ) {
        roomDatabaseReference
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    onException.invoke(p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var room = Room()
                    for (snapshot in p0.children) {
                        if (snapshot.key == id) {
                            room = snapshot.getValue(Room::class.java)!!

                        }
                    }
                    onDataLoaded.invoke(room)
                }
            })
    }

    override fun getPriceRange(
        onDataLoaded: (min: Float, max: Float) -> Unit,
        onException: (String) -> Unit
    ) {
        roomDatabaseReference
            .orderByChild("price")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    onException.invoke(p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val start = p0.children.elementAt(0).getValue(Room::class.java)?.price!!
                    val end =
                        p0.children.elementAt((p0.childrenCount - 1).toInt())
                            .getValue(Room::class.java)?.price!!
                    if (start != end) {
                        onDataLoaded.invoke(start, end)
                    } else onException.invoke("cannot get price in range ")
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
            .push()
            .setValue(comment)
            .addOnSuccessListener {
                message = "Insert comment successfully"
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
            .child("comments")
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
            .child("comments")
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