/*
 * Copyright 2021 Pelican Regimen
 *
 * This source file is a part of Pelican Regimen <https://github.com/PelicanRegimen/pelican-regimen>
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
package com.pelicanregimen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.pelicanregimen.ui.PelicanRegimenApp
import com.pelicanregimen.ui.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AppTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            PelicanRegimenApp()
        }
    }

    @Test
    fun app_launches() {
        // Check app launches at the correct destination
        composeTestRule.onNodeWithText("HOME").assertIsDisplayed()
        composeTestRule.onNodeWithText("Android's picks").assertIsDisplayed()
    }

    @Test
    fun app_canNavigateToAllScreens() {
        // Check app launches at HOME
        composeTestRule.onNodeWithText("HOME").assertIsDisplayed()
        composeTestRule.onNodeWithText("Android's picks").assertIsDisplayed()

        // Navigate to Search
        composeTestRule.onNodeWithText("SEARCH").performClick().assertIsDisplayed()
        composeTestRule.onNodeWithText("Categories").assertIsDisplayed()

        // Navigate to Cart
        composeTestRule.onNodeWithText("MY CART").performClick().assertIsDisplayed()
        composeTestRule.onNodeWithText("Order (3 items)").assertIsDisplayed()

        // Navigate to Profile
        composeTestRule.onNodeWithText("PROFILE").performClick().assertIsDisplayed()
        composeTestRule.onNodeWithText("This is currently work in progress").assertIsDisplayed()
    }

    @Test
    fun app_canNavigateToDetailPage() {
        composeTestRule.onNodeWithText("Chips").performClick()
        composeTestRule.onNodeWithText("Lorem ipsum", substring = true).assertIsDisplayed()
    }
}
