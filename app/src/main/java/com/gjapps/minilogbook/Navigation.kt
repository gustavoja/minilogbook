package com.gjapps.minilogbook

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gjapps.minilogbook.ui.blodglucroserecords.BloodGlucoseRecordsScreen


@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "bloodGlucoseRecordsScreen") {
        composable("bloodGlucoseRecordsScreen") { BloodGlucoseRecordsScreen() }
    }
}