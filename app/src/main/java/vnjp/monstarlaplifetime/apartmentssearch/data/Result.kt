package vnjp.monstarlaplifetime.apartmentssearch.data


sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: String) : Result<Nothing>()

    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success get $data"
            is Error -> exception
            Loading -> "Loading"
        }
    }
}