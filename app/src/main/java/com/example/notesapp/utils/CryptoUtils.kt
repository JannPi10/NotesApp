package com.example.notesapp.utils

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptoUtils {
    private const val secretKey = "1234567890abcdef" // 16 caracteres = 128 bits
    private const val algorithm = "AES/CBC/PKCS5Padding"

    fun encrypt(str: String): String {
        val iv = ByteArray(16).apply { SecureRandom().nextBytes(this) }
        val keySpec = SecretKeySpec(secretKey.toByteArray(), "AES")
        val ivSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

        val encrypted = cipher.doFinal(str.toByteArray())
        val combined = iv + encrypted
        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    fun decrypt(encStr: String): String {
        val decoded = Base64.decode(encStr, Base64.NO_WRAP)
        val iv = decoded.copyOfRange(0, 16)
        val encrypted = decoded.copyOfRange(16, decoded.size)

        val keySpec = SecretKeySpec(secretKey.toByteArray(), "AES")
        val ivSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

        return String(cipher.doFinal(encrypted))
    }
}
