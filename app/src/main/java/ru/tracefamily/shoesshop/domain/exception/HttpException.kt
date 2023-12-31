package ru.tracefamily.shoesshop.domain.exception

class HttpException(
    message: String,
    private val errorCode: Int = -1
): Throwable(message) {
    override fun toString(): String {
        return if (errorCode == -1) {
            message ?: cause.toString()
        } else {
            "Code $errorCode: $message"
        }
    }
}