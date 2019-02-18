package com.desrumaux.androidtoolbox.keystore

import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import java.io.IOException
import java.security.*
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

class GlobalCryptor {
    private var encryptor: EnCryptor? = EnCryptor()
    private var decryptor: DeCryptor? = DeCryptor()

    fun decryptText(alias: String): String {
        try {
            Log.d("encryption", encryptor?.encryption?.toString())
            Log.d("iv", encryptor?.iv?.toString())
            return decryptor!!.decryptData(alias, encryptor?.encryption!!, encryptor?.iv!!)

        } catch (e: UnrecoverableEntryException) {
            Log.e("HomeActivity", "decryptData() called with: " + e.message, e)
        } catch (e: NoSuchAlgorithmException) {
            Log.e("HomeActivity", "decryptData() called with: " + e.message, e)
        } catch (e: KeyStoreException) {
            Log.e("HomeActivity", "decryptData() called with: " + e.message, e)
        } catch (e: NoSuchPaddingException) {
            Log.e("HomeActivity", "decryptData() called with: " + e.message, e)
        } catch (e: NoSuchProviderException) {
            Log.e("HomeActivity", "decryptData() called with: " + e.message, e)
        } catch (e: IOException) {
            Log.e("HomeActivity", "decryptData() called with: " + e.message, e)
        } catch (e: InvalidKeyException) {
            Log.e("HomeActivity", "decryptData() called with: " + e.message, e)
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        }
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun encryptText(alias: String, textToEncrypt: String) {
        try {
            encryptor?.encryptText(alias, textToEncrypt)
            Log.d("encryption", encryptor?.encryption.toString())
            Log.d("iv", encryptor?.iv.toString())

        } catch (e: UnrecoverableEntryException) {
            Log.e("HomeActivity", "onClick() called with: " + e.message, e)
        } catch (e: NoSuchAlgorithmException) {
            Log.e("HomeActivity", "onClick() called with: " + e.message, e)
        } catch (e: NoSuchProviderException) {
            Log.e("HomeActivity", "onClick() called with: " + e.message, e)
        } catch (e: KeyStoreException) {
            Log.e("HomeActivity", "onClick() called with: " + e.message, e)
        } catch (e: IOException) {
            Log.e("HomeActivity", "onClick() called with: " + e.message, e)
        } catch (e: NoSuchPaddingException) {
            Log.e("HomeActivity", "onClick() called with: " + e.message, e)
        } catch (e: InvalidKeyException) {
            Log.e("HomeActivity", "onClick() called with: " + e.message, e)
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: SignatureException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }
    }
}