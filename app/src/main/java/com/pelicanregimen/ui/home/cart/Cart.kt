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
package com.pelicanregimen.ui.home.cart

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.statusBarsHeight
import com.pelicanregimen.R
import com.pelicanregimen.model.OrderLine
import com.pelicanregimen.model.SnackCollection
import com.pelicanregimen.model.SnackRepo
import com.pelicanregimen.ui.components.PelicanRegimenButton
import com.pelicanregimen.ui.components.PelicanRegimenDivider
import com.pelicanregimen.ui.components.PelicanRegimenSurface
import com.pelicanregimen.ui.components.QuantitySelector
import com.pelicanregimen.ui.components.SnackCollection
import com.pelicanregimen.ui.components.SnackImage
import com.pelicanregimen.ui.home.DestinationBar
import com.pelicanregimen.ui.theme.AlphaNearOpaque
import com.pelicanregimen.ui.theme.PelicanRegimenTheme
import com.pelicanregimen.ui.utils.formatPrice

@Composable
fun Cart(
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = viewModel(factory = CartViewModel.provideFactory())
) {
    val orderLines by viewModel.orderLines.collectAsState()
    val inspiredByCart = remember { SnackRepo.getInspiredByCart() }
    Cart(
        orderLines = orderLines,
        removeSnack = viewModel::removeSnack,
        increaseItemCount = viewModel::increaseSnackCount,
        decreaseItemCount = viewModel::decreaseSnackCount,
        inspiredByCart = inspiredByCart,
        onSnackClick = onSnackClick,
        modifier = modifier
    )
}

@Composable
fun Cart(
    orderLines: List<OrderLine>,
    removeSnack: (Long) -> Unit,
    increaseItemCount: (Long) -> Unit,
    decreaseItemCount: (Long) -> Unit,
    inspiredByCart: SnackCollection,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    PelicanRegimenSurface(modifier = modifier.fillMaxSize()) {
        Box {
            CartContent(
                orderLines = orderLines,
                removeSnack = removeSnack,
                increaseItemCount = increaseItemCount,
                decreaseItemCount = decreaseItemCount,
                inspiredByCart = inspiredByCart,
                onSnackClick = onSnackClick,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            DestinationBar(modifier = Modifier.align(Alignment.TopCenter))
            CheckoutBar(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
private fun CartContent(
    orderLines: List<OrderLine>,
    removeSnack: (Long) -> Unit,
    increaseItemCount: (Long) -> Unit,
    decreaseItemCount: (Long) -> Unit,
    inspiredByCart: SnackCollection,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val resources = LocalContext.current.resources
    val snackCountFormattedString = remember(orderLines.size, resources) {
        resources.getQuantityString(
            R.plurals.cart_order_count,
            orderLines.size, orderLines.size
        )
    }
    LazyColumn(modifier) {
        item {
            Spacer(Modifier.statusBarsHeight(additional = 56.dp))
            Text(
                text = stringResource(R.string.cart_order_header, snackCountFormattedString),
                style = MaterialTheme.typography.h6,
                color = PelicanRegimenTheme.colors.brand,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .heightIn(min = 56.dp)
                    .padding(horizontal = 24.dp, vertical = 4.dp)
                    .wrapContentHeight()
            )
        }
        items(orderLines) { orderLine ->
            SwipeDismissItem(
                background = { offsetX ->
                    /*Background color changes from light gray to red when the
                    swipe to delete with exceeds 160.dp*/
                    val backgroundColor = if (offsetX < -160.dp) {
                        PelicanRegimenTheme.colors.error
                    } else {
                        PelicanRegimenTheme.colors.uiFloated
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(backgroundColor),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Set 4.dp padding only if offset is bigger than 160.dp
                        val padding: Dp by animateDpAsState(
                            if (offsetX > -160.dp) 4.dp else 0.dp
                        )
                        Box(
                            Modifier
                                .width(offsetX * -1)
                                .padding(padding)
                        ) {
                            // Height equals to width removing padding
                            val height = (offsetX + 8.dp) * -1
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(height)
                                    .align(Alignment.Center),
                                shape = CircleShape,
                                color = PelicanRegimenTheme.colors.error
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Icon must be visible while in this width range
                                    if (offsetX < -40.dp && offsetX > -152.dp) {
                                        // Icon alpha decreases as it is about to disappear
                                        val iconAlpha: Float by animateFloatAsState(
                                            if (offsetX < -120.dp) 0.5f else 1f
                                        )

                                        Icon(
                                            imageVector = Icons.Filled.DeleteForever,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .graphicsLayer(alpha = iconAlpha),
                                            tint = PelicanRegimenTheme.colors.uiBackground,
                                            contentDescription = null,
                                        )
                                    }
                                    /*Text opacity increases as the text is supposed to appear in
                                    the screen*/
                                    val textAlpha by animateFloatAsState(
                                        if (offsetX > -144.dp) 0.5f else 1f
                                    )
                                    if (offsetX < -120.dp) {
                                        Text(
                                            text = stringResource(id = R.string.remove_item),
                                            style = MaterialTheme.typography.subtitle1,
                                            color = PelicanRegimenTheme.colors.uiBackground,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .graphicsLayer(
                                                    alpha = textAlpha
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
            ) {
                CartItem(
                    orderLine = orderLine,
                    removeSnack = removeSnack,
                    increaseItemCount = increaseItemCount,
                    decreaseItemCount = decreaseItemCount,
                    onSnackClick = onSnackClick
                )
            }
        }
        item {
            SummaryItem(
                subtotal = orderLines.map { it.snack.price * it.count }.sum(),
                shippingCosts = 369
            )
        }
        item {
            SnackCollection(
                snackCollection = inspiredByCart,
                onSnackClick = onSnackClick,
                highlight = false
            )
            Spacer(Modifier.height(56.dp))
        }
    }
}

@Composable
fun CartItem(
    orderLine: OrderLine,
    removeSnack: (Long) -> Unit,
    increaseItemCount: (Long) -> Unit,
    decreaseItemCount: (Long) -> Unit,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val snack = orderLine.snack
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSnackClick(snack.id) }
            .background(PelicanRegimenTheme.colors.uiBackground)
            .padding(horizontal = 24.dp)

    ) {
        val (divider, image, name, tag, priceSpacer, price, remove, quantity) = createRefs()
        createVerticalChain(name, tag, priceSpacer, price, chainStyle = ChainStyle.Packed)
        SnackImage(
            imageUrl = snack.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                }
        )
        Text(
            text = snack.name,
            style = MaterialTheme.typography.subtitle1,
            color = PelicanRegimenTheme.colors.textSecondary,
            modifier = Modifier.constrainAs(name) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = remove.start,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        IconButton(
            onClick = { removeSnack(snack.id) },
            modifier = Modifier
                .constrainAs(remove) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .padding(top = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                tint = PelicanRegimenTheme.colors.iconSecondary,
                contentDescription = stringResource(R.string.label_remove)
            )
        }
        Text(
            text = snack.tagline,
            style = MaterialTheme.typography.body1,
            color = PelicanRegimenTheme.colors.textHelp,
            modifier = Modifier.constrainAs(tag) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = parent.end,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        Spacer(
            Modifier
                .height(8.dp)
                .constrainAs(priceSpacer) {
                    linkTo(top = tag.bottom, bottom = price.top)
                }
        )
        Text(
            text = formatPrice(snack.price),
            style = MaterialTheme.typography.subtitle1,
            color = PelicanRegimenTheme.colors.textPrimary,
            modifier = Modifier.constrainAs(price) {
                linkTo(
                    start = image.end,
                    end = quantity.start,
                    startMargin = 16.dp,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        QuantitySelector(
            count = orderLine.count,
            decreaseItemCount = { decreaseItemCount(snack.id) },
            increaseItemCount = { increaseItemCount(snack.id) },
            modifier = Modifier.constrainAs(quantity) {
                baseline.linkTo(price.baseline)
                end.linkTo(parent.end)
            }
        )
        PelicanRegimenDivider(
            Modifier.constrainAs(divider) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(parent.bottom)
            }
        )
    }
}

@Composable
fun SummaryItem(
    subtotal: Long,
    shippingCosts: Long,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.cart_summary_header),
            style = MaterialTheme.typography.h6,
            color = PelicanRegimenTheme.colors.brand,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .heightIn(min = 56.dp)
                .wrapContentHeight()
        )
        Row(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = stringResource(R.string.cart_subtotal_label),
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
                    .alignBy(LastBaseline)
            )
            Text(
                text = formatPrice(subtotal),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }
        Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
            Text(
                text = stringResource(R.string.cart_shipping_label),
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
                    .alignBy(LastBaseline)
            )
            Text(
                text = formatPrice(shippingCosts),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        PelicanRegimenDivider()
        Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
            Text(
                text = stringResource(R.string.cart_total_label),
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
                    .wrapContentWidth(Alignment.End)
                    .alignBy(LastBaseline)
            )
            Text(
                text = formatPrice(subtotal + shippingCosts),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }
        PelicanRegimenDivider()
    }
}

@Composable
private fun CheckoutBar(modifier: Modifier = Modifier) {
    Column(
        modifier.background(
            PelicanRegimenTheme.colors.uiBackground.copy(alpha = AlphaNearOpaque)
        )
    ) {

        PelicanRegimenDivider()
        Row {
            Spacer(Modifier.weight(1f))
            PelicanRegimenButton(
                onClick = { /* todo */ },
                shape = RectangleShape,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.cart_checkout),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview("default")
@Preview("dark theme", uiMode = UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun CartPreview() {
    PelicanRegimenTheme {
        Cart(
            orderLines = SnackRepo.getCart(),
            removeSnack = {},
            increaseItemCount = {},
            decreaseItemCount = {},
            inspiredByCart = SnackRepo.getInspiredByCart(),
            onSnackClick = {}
        )
    }
}
