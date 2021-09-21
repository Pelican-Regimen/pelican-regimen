/*
 * Copyright 2020 Pelican Regimen
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
package com.pelicanregimen.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pelicanregimen.ui.theme.PelicanRegimenTheme

@Composable
fun PelicanRegimenDivider(
    modifier: Modifier = Modifier,
    color: Color = PelicanRegimenTheme.colors.uiBorder.copy(alpha = DividerAlpha),
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp
) {
    Divider(
        modifier = modifier,
        color = color,
        thickness = thickness,
        startIndent = startIndent
    )
}

private const val DividerAlpha = 0.12f

@Preview("default", showBackground = true)
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DividerPreview() {
    PelicanRegimenTheme {
        Box(Modifier.size(height = 10.dp, width = 100.dp)) {
            PelicanRegimenDivider(Modifier.align(Alignment.Center))
        }
    }
}
