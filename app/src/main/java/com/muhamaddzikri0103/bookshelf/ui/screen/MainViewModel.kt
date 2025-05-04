package com.muhamaddzikri0103.bookshelf.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhamaddzikri0103.bookshelf.database.BookshelfDao
import com.muhamaddzikri0103.bookshelf.model.BookAndReading
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(dao: BookshelfDao) : ViewModel() {

    val data: StateFlow<List<BookAndReading>> = dao.getBookAndReading().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )


//    val data = listOf(
//        BookAndReading(
//            id = 1,
//            Book(1,
//                "Harry Potter and the Philosopher's Stone",
//                "JK.Rowling",
//                "Fantasy",
//                450
//            ),
//            Reading(
//                1,
//                30,
//                false,
//                1
//            )
//        ),
//        BookAndReading(
//            id = 2,
//            Book(2,
//                "Harry Potter and the Deathly Hallows",
//                "JK.Rowling",
//                "Fantasy",
//                450
//            ),
//            Reading(
//                2,
//                110,
//                false,
//                2
//            )
//        ),
//        BookAndReading(
//            id = 3,
//            Book(3,
//                "HAtomic Habits",
//                "James Clear",
//                "Personal Development",
//                350
//            ),
//            Reading(
//                3,
//                100,
//                false,
//                3
//            )
//        ),
//        BookAndReading(
//            id = 4,
//            Book(4,
//                "The Midnight Library",
//                "Matt Haig",
//                "Philosophical",
//                304
//            ),
//            Reading(
//                4,
//                50,
//                false,
//                4
//            )
//        ),
//        BookAndReading(
//            id = 5,
//            Book(5,
//                "Educated",
//                "Tara Westover",
//                "Autobiography ",
//                334
//            ),
//            Reading(
//                5,
//                15,
//                false,
//                5
//            )
//        ),
//        BookAndReading(
//            id = 6,
//            Book(6,
//                "HHarry Potter and the Prisoner of Azkaban",
//                "JK.Rowling",
//                "Fantasy",
//                450
//            ),
//            Reading(
//                6,
//                15,
//                false,
//                6
//            )
//        ),
//        BookAndReading(
//            id = 7,
//            Book(7,
//                "Harry Potter and the Order of the Phoenix",
//                "JK.Rowling",
//                "Fantasy",
//                450
//            ),
//            Reading(
//                7,
//                90,
//                false,
//                7
//            )
//        ),
//        BookAndReading(
//            id = 8,
//            Book(8,
//                "Test",
//                "JK.Rowling",
//                "Fantasy",
//                10
//            ),
//            Reading(
//                8,
//                1,
//                false,
//                7
//            )
//        )
//    )

//    fun getBookAndReading(id: Long): BookAndReading? {
//        return data.find { it.id == id }
//    }
}