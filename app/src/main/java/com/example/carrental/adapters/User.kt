package com.example.carrental.adapters

class User(val id: Int, val name: String, val email: String, val password: String, val isAdmin: Boolean) {

    constructor(name: String, email: String, password: String, isAdmin: Boolean) : this(-1, name, email, password, isAdmin)

    // Other methods and properties of the com.example.carrental.adapters.User class...
}
