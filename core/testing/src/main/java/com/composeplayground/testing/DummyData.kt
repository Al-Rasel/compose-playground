package com.composeplayground.testing

import com.composeplayground.model.User
import com.composeplayground.model.UserSearchDetails

val userSearchDetails = UserSearchDetails(
    5, false, listOf(
        User(
            login = "johndoe123",
            name = "John Doe",
            imageUrl = "https://example.com/johndoe.jpg",
            followers = 150,
            following = 75
        ),
        User(
            login = "janesmith456",
            name = "Jane Smith",
            imageUrl = "https://example.com/janesmith.jpg",
            followers = 240,
            following = 120
        ),
        User(
            login = "user789",
            name = null,
            imageUrl = "https://example.com/user789.jpg",
            followers = 50,
            following = 10
        ),
        User(
            login = "charlie_b",
            name = "Charlie Brown",
            imageUrl = "https://example.com/charliebrown.jpg",
            followers = 500,
            following = 300
        ),
        User(
            login = "alicewonder",
            name = "Alice Wonderland",
            imageUrl = "https://example.com/alice.jpg",
            followers = 1200,
            following = 600
        )
    )
)