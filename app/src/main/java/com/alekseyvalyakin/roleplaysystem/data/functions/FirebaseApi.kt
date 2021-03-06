package com.alekseyvalyakin.roleplaysystem.data.functions

import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProvider
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.utils.throwError
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import io.reactivex.Single
import timber.log.Timber

class FirebaseApiImpl(
        private val authProvider: AuthProvider
) : FirebaseApi {

    private val functions = FirebaseFunctions.getInstance()

    override fun copyGame(gameId: String): Single<Game> {
        return Single.create { emitter ->
            val data = mutableMapOf<String, Any>()
            data[GAME_ID] = gameId
            data[USER_NAME] = authProvider.getCurrentUser()!!.name
            functions.getHttpsCallable(COPY_GAME)
                    .call(data)
                    .continueWith { task ->
                        task.result!!.data.toString()
                    }.addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            task.exception?.run {
                                if (this is FirebaseFunctionsException) {
                                    Timber.e("errorCode ${this.code} details ${this.details}")
                                }
                            }
                            emitter.throwError(task.exception!!)
                        } else {
                            Timber.d("Success copy game ${task.result}")
                            emitter.onSuccess(Game())
                        }
                    }
        }
    }

    companion object {
        private const val COPY_GAME = "copyGame"
        private const val GAME_ID = "game_id"
        private const val USER_NAME = "user_name"
    }
}

interface FirebaseApi {
    fun copyGame(gameId: String): Single<Game>
}