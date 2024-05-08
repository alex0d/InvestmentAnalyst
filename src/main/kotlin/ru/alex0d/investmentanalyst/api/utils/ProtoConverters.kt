package ru.alex0d.investmentanalyst.api.utils

import com.google.protobuf.ByteString
import com.google.protobuf.CodedInputStream

fun ByteString.splitIntoStrings(): List<String> {
    val input = CodedInputStream.newInstance(this.toByteArray())
    val result = mutableListOf<String>()
    while (!input.isAtEnd) {
        val tag = input.readTag()
        val value = when (val wireType = tag and 7) {
            2 -> input.readString()
            else -> throw IllegalArgumentException("Unexpected wire type: $wireType")
        }
        result.add(value)
    }
    return result
}