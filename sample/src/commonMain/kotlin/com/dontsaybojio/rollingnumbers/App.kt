package com.dontsaybojio.rollingnumbers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AirplanemodeActive
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.CurrencyBitcoin
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        Scaffold(bottomBar = { NavigationBar(navController) }) { innerPadding ->
            NavHost(navController = navController, startDestination = BottomNavItem.Integer.route) {
                composable(BottomNavItem.Integer.route) { IntegerScreen(innerPadding) }
                composable(BottomNavItem.Decimal.route) { DecimalScreen(innerPadding) }
                composable(BottomNavItem.Currency.route) { CurrencyScreen(innerPadding) }
                composable(BottomNavItem.Alphanumeric.route) { AlphanumericScreen(innerPadding) }
            }
        }
    }
}

@Composable
private fun NavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        BottomNavItem.entries.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

enum class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector,
) {
    Integer(
        "Integer",
        "integer",
        Icons.Rounded.Numbers
    ),
    Decimal(
        "Decimal",
        "decimal",
        Icons.Rounded.AttachMoney
    ),
    Currency(
        "Currency",
        "currency",
        Icons.Rounded.CurrencyBitcoin
    ),
    Alphanumeric(
        "Alphanumeric",
        "alphanumeric",
        Icons.Rounded.AirplanemodeActive
    )
}