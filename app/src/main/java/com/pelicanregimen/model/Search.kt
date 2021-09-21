/*
 * Copyright 2020 Pelican Regimen
 *
 * This source file is a part of 
 * Pelican Regimen <https://github.com/PelicanRegimen/pelican-regimen>
 *
 * Pelican Regimen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Pelican Regimen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pelican Regimen.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.pelicanregimen.model

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * A fake repo for searching.
 */
object SearchRepo {
    fun getCategories(): List<SearchCategoryCollection> = searchCategoryCollections
    fun getSuggestions(): List<SearchSuggestionGroup> = searchSuggestions

    suspend fun search(query: String): List<Snack> = withContext(Dispatchers.Default) {
        delay(200L) // simulate an I/O delay
        snacks.filter { it.name.contains(query, ignoreCase = true) }
    }
}

@Immutable
data class SearchCategoryCollection(
    val id: Long,
    val name: String,
    val categories: List<SearchCategory>
)

@Immutable
data class SearchCategory(
    val name: String,
    val imageUrl: String
)

@Immutable
data class SearchSuggestionGroup(
    val id: Long,
    val name: String,
    val suggestions: List<String>
)

/**
 * Static data
 */

private val searchCategoryCollections = listOf(
    SearchCategoryCollection(
        id = 0L,
        name = "Categories",
        categories = listOf(
            SearchCategory(
                name = "Chips & crackers",
                imageUrl = "https://source.unsplash.com/UsSdMZ78Q3E"
            ),
            SearchCategory(
                name = "Fruit snacks",
                imageUrl = "https://source.unsplash.com/SfP1PtM9Qa8"
            ),
            SearchCategory(
                name = "Desserts",
                imageUrl = "https://source.unsplash.com/_jk8KIyN_uA"
            ),
            SearchCategory(
                name = "Nuts ",
                imageUrl = "https://source.unsplash.com/UsSdMZ78Q3E"
            )
        )
    ),
    SearchCategoryCollection(
        id = 1L,
        name = "Lifestyles",
        categories = listOf(
            SearchCategory(
                name = "Organic",
                imageUrl = "https://source.unsplash.com/7meCnGCJ5Ms"
            ),
            SearchCategory(
                name = "Gluten Free",
                imageUrl = "https://source.unsplash.com/m741tj4Cz7M"
            ),
            SearchCategory(
                name = "Paleo",
                imageUrl = "https://source.unsplash.com/dt5-8tThZKg"
            ),
            SearchCategory(
                name = "Vegan",
                imageUrl = "https://source.unsplash.com/ReXxkS1m1H0"
            ),
            SearchCategory(
                name = "Vegitarian",
                imageUrl = "https://source.unsplash.com/IGfIGP5ONV0"
            ),
            SearchCategory(
                name = "Whole30",
                imageUrl = "https://source.unsplash.com/9MzCd76xLGk"
            )
        )
    )
)

private val searchSuggestions = listOf(
    SearchSuggestionGroup(
        id = 0L,
        name = "Recent searches",
        suggestions = listOf(
            "Cheese",
            "Apple Sauce"
        )
    ),
    SearchSuggestionGroup(
        id = 1L,
        name = "Popular searches",
        suggestions = listOf(
            "Organic",
            "Gluten Free",
            "Paleo",
            "Vegan",
            "Vegitarian",
            "Whole30"
        )
    )
)
