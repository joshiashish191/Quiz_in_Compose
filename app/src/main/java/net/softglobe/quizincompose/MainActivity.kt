package net.softglobe.quizincompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import net.softglobe.quizincompose.model.QuizAttempt
import net.softglobe.quizincompose.model.QuizApp
import net.softglobe.quizincompose.ui.theme.QuizInComposeTheme
import net.softglobe.quizincompose.viewmodel.QuizViewModel
import net.softglobe.quizincompose.viewmodel.QuizViewModelFactory
import net.softglobe.quizincompose.viewmodel.UiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    val app by lazy { application as QuizApp }
    val viewModel by lazy {
        ViewModelProvider.create(
            owner = this,
            factory = QuizViewModelFactory(app.repository)
        )[QuizViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            QuizInComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (uiState.isHistoryDisplayed) {
                            HistoryScreen(uiState.attemptHistory)
                        } else if (uiState.questions.isEmpty()) {
                            CircularProgressIndicator()
                        } else {
                            if (!uiState.isResultDisplayed) {
                                QuizScreen(uiState)
                            } else {
                                ResultScreen(uiState)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun QuizScreen(uiState: UiState) {
        val currentQuestion = uiState.questions[uiState.currentQuestionIndex]

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { viewModel.showHistory() }) {
                    Text("History")
                }
            }

            Text(
                text = currentQuestion.question,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(20.dp))

            currentQuestion.options.forEachIndexed { index, string ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.onOptionSelected(index)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (uiState.selectedOptionIndex == index),
                        onClick = { viewModel.onOptionSelected(index) }
                    )
                    Text(
                        text = string,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                onClick = {
                    viewModel.onNextClicked()
                }
            ) {
                Text(
                    text = if (uiState.currentQuestionIndex == uiState.questions.size-1) "Show Result" else "Next Question"
                )
            }
        }
    }


    @Composable
    private fun ResultScreen(uiState: UiState) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val isPassed = uiState.score >= uiState.totalScore*0.5
            Text(
                text = if (isPassed) "🎉" else "😔",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 60.sp
            )
            Text(
                text = if (isPassed) "Congratulations!" else "Sorry!",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 40.sp
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = if (isPassed) "You're Passed!" else "You've Failed!",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Score: ${uiState.score} / ${uiState.totalScore}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(10.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                onClick = {
                    viewModel.showHistory()
                }
            ) {
                Text("View History")
            }
            Spacer(Modifier.height(10.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                onClick = {
                    viewModel.restartQuiz()
                }
            ) {
                Text(
                    text = "Attempt again"
                )
            }
        }
    }

    @Composable
    private fun HistoryScreen(attempts: List<QuizAttempt>) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quiz History",
                    style = MaterialTheme.typography.headlineSmall
                )
                Button(onClick = { viewModel.hideHistory() }) {
                    Text("Back")
                }
            }

            if (attempts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No quiz attempts yet")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(attempts, key = { it.id ?: it.completedAt }) { attempt ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = if (attempt.passed) "Passed" else "Failed",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Score: ${attempt.score} / ${attempt.totalScore}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = formatAttemptTime(attempt.completedAt),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun formatAttemptTime(timestamp: Long): String {
        val formatter = SimpleDateFormat("MMM d, yyyy 'at' h:mm a", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }
}