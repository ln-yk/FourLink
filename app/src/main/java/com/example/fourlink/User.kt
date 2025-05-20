package com.example.fourlink

data class User(val username: String, val password: String)

object UserManager {
    private val users = mutableListOf<User>()
    var currentUser: User? = null

    fun register(username: String, password: String): Boolean {
        if (users.any { it.username == username }) return false
        users.add(User(username, password))
        return true
    }

    fun login(username: String, password: String): Boolean {
        val user = users.find { it.username == username && it.password == password }
        currentUser = user
        return user != null
    }

    fun updatePassword(username: String, newPassword: String): Boolean {
        val index = users.indexOfFirst { it.username == username }
        if (index == -1) return false
        users[index] = users[index].copy(password = newPassword)
        return true
    }


    fun logout() {
        currentUser = null
    }
}