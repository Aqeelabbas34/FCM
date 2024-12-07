package com.aqeel.adminapp.notifications

import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors

class AccessToken {
    companion object {
        private const val FIREBASE_MESSAGING_SCOPE =
            "https://www.googleapis.com/auth/firebase.messaging"

        fun getAccessTokenAsync(callback: AccessTokenCallback) {
            val executor = Executors.newSingleThreadExecutor()
            executor.submit {
                val token = getAccessToken()
                callback.onAccessTokenReceived(token)
            }
            // executor.shutdown() // Uncomment if needed
        }

        private fun getAccessToken(): String? {
            return try {
                val jsonString = """
               {
  "type": "service_account",
  "project_id": "notifications-c1775",
  "private_key_id": "34a0f4ae2132dfdee0724290db94734f569f7661",
  "private_key": "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDPhOSK2x1/wxvq\ndpz8lIBSYgc8SakMbpAnF8os9+Q3MZbeTHnweUItQ1LKBCg8azcBG2FJznQVAa0J\nO1UMeuSgxLFST7MJtk+ykTmErQQui0hGtaa8l8GTjx2doNNU8WCRaIE9oMEbRPF/\nmar9N4qb7JgyMt4MdDt83T9RaGlIExHIo5PCawUC9TZyts1yI1xIfHYKabwWNERG\nFxBRUqY0ubgTf95qVhsg7rpe+j53x7gdChiw6Ndt0A4emOMOdSOg/FofDWnYQiAs\nnwi4yMZe/QLFYcqj2QN8P3vQwxeYsFGHRsAPPJQ26KrFQUVx/8WsgaHppAfOiaIP\nzvFGbxl7AgMBAAECggEAVco9R1BlhhuNj2Ug6mSkFirTje9v1SViL98sZGHI9a5p\nDha0tJ8Upiy2bbDBR5UVZL5CfqY8pDCQrgfa1bF6kiIuonYzcJjvosVZ74kteOZ4\n506Sk1Xzn4KjfjZuF6hAQWzgPS+XlaPvaM6I+2z6PxLVKq+Pz7s/mpPG6OtO1w3X\njNdLir1ilr6v3f775hCflrLfSAFyKNG1tyPk+rB6F4K+suAtqr4IuvNCRTz0Rnst\ndnyYt6hoUphoJo1cGIqOr8jjK2vR95ixN9OKagnqnSGFawZdFS0y93F91isM7c4E\nOzANac26XzzD32vlR0eenu7gUy90+8ys02DqzuuGrQKBgQD/l2gKGyVSZZcg/TyX\ncCawWy/2brT164zhIPknozM0j3STtDvvNP+heMzHC7n1wO+XN71yBDPw8wGa688y\nXeTMjC7cFMMdVwTobZ43lQpyCb8a17dtCDeVXlbvBd/YFupulWS64d6mE1OWqHgj\ntQg+sy2CmaAsbLIjr0KLkZr9RwKBgQDP2dBopNIsjydqvd7XXSEpR9ZT5kAAATQQ\nmOkNKTn2xPBsKaUSbepCQkLK0+1Lom90peD3YjAUSsCqqbqPs0YwWPmNZqWxaxvP\nBnsKBAQhddzFsfEix5HDQ1NtTsi0oWJkBb8JBP2zz+cfAU5fITu/jEL3NOsHW+lC\nyIZnrsTMLQKBgQCpg/eizEJCldJYNNyc6+sdCD2VSfOHLwSuw7m8CkeJ0qNU2zLt\nAc+RfpcCZMqfLak6N+PT3Om+Is2rvD4df5yh9U+W88Y8jVxZ82n+H0FgRQqUeQHU\ntKB41qr9uH2vyfMOCNNYBzouF59WysFznTusqhXDCrTrAeRcOn7+JkIPPQKBgByD\n1lq02fyw+whi2hKTvTvrlhTWjssxXxIz2C0tDb20EQPVxsa7e5rxejZXUsPAmifX\npgBqYQw3oS0IsgaK4sM5QatLfMnVVvBVBEZAfMyWXd5lZh6OYIn4T5a8az6UsWnl\ne+rh22hyylNvm2/joE3MEVfW07OjwWGfHwlUab1VAoGAPFa5phhUcGEtdqAPnW/I\nQsXe0DobtSnr/og8otI324W34U6asGc0XpYTVs8FV3p/Ysba2a5JJiJdemPpP1qw\nnHPOkVNdddIO1Vp3IHsLqMFqlY0gFjzHrqyuelnn2Bt0ZBVwDg4VXPwOm38Ke+Ta\ntefio3g4UPwHYk/L9telZI4=\n-----END PRIVATE KEY-----\n",
  "client_email": "firebase-adminsdk-jt3gc@notifications-c1775.iam.gserviceaccount.com",
  "client_id": "106769664987834724552",
  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
  "token_uri": "https://oauth2.googleapis.com/token",
  "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
  "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-jt3gc%40notifications-c1775.iam.gserviceaccount.com",
  "universe_domain": "googleapis.com"
}

            """.trimIndent()
                val stream = ByteArrayInputStream(jsonString.toByteArray(StandardCharsets.UTF_8))
                val googleCredentials = GoogleCredentials
                    .fromStream(stream)
                    .createScoped(listOf(FIREBASE_MESSAGING_SCOPE))
                googleCredentials.refreshIfExpired()
                googleCredentials.accessToken.tokenValue
            } catch (e: Exception) {
                Log.e("AccessToken", "Error retrieving access token", e)
                null
            }
        }
    }

    interface AccessTokenCallback {
        fun onAccessTokenReceived(token: String?)
    }
}