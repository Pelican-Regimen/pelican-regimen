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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pelicanregimen.ui.theme.PelicanRegimenTheme

@Composable
fun PelicanRegimenGradientTintedIconButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    colors: List<Color> = PelicanRegimenTheme.colors.interactiveSecondary
) {
    val interactionSource = remember { MutableInteractionSource() }

    // This should use a layer + srcIn but needs investigation
    val border = Modifier.fadeInDiagonalGradientBorder(
        showBorder = true,
        colors = PelicanRegimenTheme.colors.interactiveSecondary,
        shape = CircleShape
    )
    val pressed by interactionSource.collectIsPressedAsState()
    val background = if (pressed) {
        Modifier.offsetGradientBackground(colors, 200f, 0f)
    } else {
        Modifier.background(PelicanRegimenTheme.colors.uiBackground)
    }
    val blendMode = if (PelicanRegimenTheme.colors.isDark) BlendMode.Darken else BlendMode.Plus
    val modifierColor = if (pressed) {
        Modifier.diagonalGradientTint(
            colors = listOf(
                PelicanRegimenTheme.colors.textSecondary,
                PelicanRegimenTheme.colors.textSecondary
            ),
            blendMode = blendMode
        )
    } else {
        Modifier.diagonalGradientTint(
            colors = colors,
            blendMode = blendMode
        )
    }
    Surface(
        modifier = modifier
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null
            )
            .clip(CircleShape)
            .then(border)
            .then(background),
        color = Color.Transparent
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = modifierColor
        )
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GradientTintedIconButtonPreview() {
    PelicanRegimenTheme {
        PelicanRegimenGradientTintedIconButton(
            imageVector = Icons.Default.Add,
            onClick = {},
            contentDescription = "Demo",
            modifier = Modifier.padding(4.dp)
        )
    }
}
