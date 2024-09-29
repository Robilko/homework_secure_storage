package com.otus.securehomework.data.source.secure

import java.security.KeyStore
import javax.crypto.SecretKey

interface KeyProvider {

    val secretKey: SecretKey

    companion object {
        val keyStore: KeyStore by lazy {
            KeyStore.getInstance(KEY_PROVIDER).apply {
                load(null)
            }
        }

        internal const val KEY_PROVIDER = "AndroidKeyStore"
    }
}