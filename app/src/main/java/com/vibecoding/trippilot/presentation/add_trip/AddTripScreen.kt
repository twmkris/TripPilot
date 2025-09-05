
package com.vibecoding.trippilot.presentation.add_trip

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.vibecoding.trippilot.domain.model.Trip
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddTripScreen(
    navController: NavController,
    viewModel: AddTripViewModel = koinViewModel()
) {
    var destination by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("0") }
    var interests by remember { mutableStateOf("") }

    // 觀察行程規劃狀態 - 使用 collectAsStateWithLifecycle 確保生命週期安全
    val planningState by viewModel.planningState.collectAsStateWithLifecycle()

    // 處理狀態變化
    LaunchedEffect(planningState) {
        when (planningState) {
            is PlanningState.Completed -> {
                // 行程規劃完成，返回上一頁
                navController.popBackStack()
            }
            is PlanningState.Error -> {
                // 顯示錯誤信息（可以添加 Snackbar 或其他 UI 元素）
            }
            else -> {
                // 其他狀態不需要特殊處理
            }
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 標題
            Text(
                text = "新增行程",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // 表單欄位
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("目的地") },
                modifier = Modifier.fillMaxWidth(),
                enabled = planningState !is PlanningState.Planning
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = startDate,
                onValueChange = { startDate = it },
                label = { Text("開始日期") },
                modifier = Modifier.fillMaxWidth(),
                enabled = planningState !is PlanningState.Planning
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = endDate,
                onValueChange = { endDate = it },
                modifier = Modifier.fillMaxWidth(),
                enabled = planningState !is PlanningState.Planning,
                label = { Text("結束日期") }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = budget,
                onValueChange = { budget = it },
                label = { Text("預算") },
                modifier = Modifier.fillMaxWidth(),
                enabled = planningState !is PlanningState.Planning
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = interests,
                onValueChange = { interests = it },
                label = { Text("興趣 (用逗號分隔)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = planningState !is PlanningState.Planning
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            // 行程規劃狀態顯示
            when (planningState) {
                is PlanningState.Planning -> {
                    // 規劃中的動畫
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "正在規劃行程中...",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "請稍候，AI 正在為您生成最佳行程",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
                
                is PlanningState.Error -> {
                    // 錯誤狀態
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "行程規劃失敗: ${(planningState as PlanningState.Error).message}",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                else -> {
                    // 正常狀態或完成狀態
                    Button(
                        onClick = {
                            // 驗證輸入
                            if (destination.isBlank() || startDate.isBlank() || endDate.isBlank() || budget.isBlank() || interests.isBlank()) {
                                return@Button
                            }
                            
                            try {
                                val trip = Trip(
                                    destination = destination,
                                    startDate = startDate,
                                    endDate = endDate,
                                    budget = budget.toDouble(),
                                    interests = interests.split(",").map { it.trim() },
                                    itinerary = ""
                                )
                                viewModel.addTrip(trip)
                            } catch (e: NumberFormatException) {
                                // 預算格式錯誤處理
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = destination.isNotBlank() && startDate.isNotBlank() && 
                                 endDate.isNotBlank() && budget.isNotBlank() && interests.isNotBlank()
                    ) {
                        Text("新增行程")
                    }
                }
            }
        }
    }
}
