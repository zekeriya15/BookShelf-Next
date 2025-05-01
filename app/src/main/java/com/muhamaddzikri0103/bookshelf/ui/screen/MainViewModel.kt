package com.muhamaddzikri0103.bookshelf.ui.screen

import androidx.lifecycle.ViewModel
import com.muhamaddzikri0103.bookshelf.model.Book
import com.muhamaddzikri0103.bookshelf.model.Reading

class MainViewModel : ViewModel() {

    val book1 = Book(1,
        "Harry Potter Harry Potter and the Philosopher's Stone",
        "JK.Rowling",
        "Fantasy",
        450
    )

    val book2 = Book(2,
        "Harry Potter and the Deathly Hallows",
        "JK.Rowling",
        "Fantasy",
        450
    )

    val book3 = Book(3,
        "HAtomic Habits",
        "James Clear",
        "Personal Development",
        350
    )

    val book4 = Book(4,
        "The Midnight Library",
        "Matt Haig",
        "Philosophical",
        304
    )

    val book5 = Book(5,
        "Educated",
        "Tara Westover",
        "Autobiography ",
        334
    )

    val book6 = Book(6,
        "HHarry Potter and the Prisoner of Azkaban",
        "JK.Rowling",
        "Fantasy",
        450
    )

    val book7 = Book(7,
        "Harry Potter and the Order of the Phoenix",
        "JK.Rowling",
        "Fantasy",
        450
    )

    val data = listOf(
        Reading(
            1,
            30,
            false,
            1
        ),
        Reading(
            2,
            50,
            false,
            5
        ),
        Reading(
            3,
            15,
            false,
            6
        ),
        Reading(
            4,
            100,
            false,
            3
        ),
        Reading(
            5,
            90,
            false,
            7
        ),
        Reading(
            6,
            230,
            false,
            6
        ),
        Reading(
            7,
            110,
            false,
            2
        )
    )
}