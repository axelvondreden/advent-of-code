package utils

import java.math.BigInteger
import java.security.MessageDigest

val md: MessageDigest by lazy { MessageDigest.getInstance("MD5") }

fun String.md5() = BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
