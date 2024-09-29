package com.otus.securehomework.data.source.secure

import android.util.Base64
import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class TextCipher @Inject constructor(private val keyProvider: KeyProvider) {

    fun encryptAes(plainText: String): String {
        val cipher = Cipher.getInstance(AES_CIPHER_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, keyProvider.secretKey, getInitializationVector())
        val encodedBytes = cipher.doFinal(plainText.toByteArray())
        return Base64.encodeToString(encodedBytes, Base64.NO_WRAP)
    }

    fun decryptAes(encryptedText: String): String {
        val cipher = Cipher.getInstance(AES_CIPHER_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, keyProvider.secretKey, getInitializationVector())
        val encodedBytes = Base64.decode(encryptedText, Base64.NO_WRAP)
        val decoded = cipher.doFinal(encodedBytes)
        return String(decoded, Charsets.UTF_8)
    }

    private fun getInitializationVector(): AlgorithmParameterSpec {
        val iv = ByteArray(GCM_IV_LENGTH)
        SecureRandom().nextBytes(iv) // Генерация случайного IV
        return GCMParameterSpec(128, iv)
    }

    private companion object {
        const val AES_CIPHER_TRANSFORMATION = "AES/GCM/NoPadding"
        const val GCM_IV_LENGTH = 12
    }
}