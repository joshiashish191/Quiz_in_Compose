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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import net.softglobe.quizincompose.model.QuizApp
import net.softglobe.quizincompose.model.QuizAttempt
import net.softglobe.quizincompose.ui.theme.QuizInComposeTheme
import net.softglobe.quizincompose.viewmodel.QuizViewModel
import net.softglobe.quizincompose.viewmodel.QuizViewModelFactory
import net.softglobe.quizincompose.viewmodel.UiState

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
            var isHistoryDisplayed by rememberSaveable { mutableStateOf(false) }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            QuizInComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (uiState.questions.isEmpty()) {
                            CircularProgressIndicator()
                        } else if (isHistoryDisplayed) {
                            QuizHistoryScreen(
                                attempts = uiState.quizAttempts,
                                onBack = { isHistoryDisplayed = false }
                            )
                        } else {
                            if (!uiState.isResultDisplayed) {
                                QuizScreen(uiState)
                            } else {
                                ResultScreen(
                                    uiState = uiState,
                                    onShowHistory = { isHistoryDisplayed = true }
                                )
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
    private fun ResultScreen(uiState: UiState, onShowHistory: () -> Unit) {
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
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                onClick = onShowHistory
            ) {
                Text("Quiz History")
            }
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
    private fun QuizHistoryScreen(attempts: List<QuizAttempt>, onBack: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Quiz History",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(16.dp))
            if (attempts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No quiz attempts yet.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(attempts, key = { it.id }) { attempt ->
                        QuizAttemptItem(attempt)
                    }
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onBack
            ) {
                Text("Back")
            }
        }
    }

    @Composable
    private fun QuizAttemptItem(attempt: QuizAttempt) {
        val completedAt = DateTimeFormatter
            .ofPattern("MMM d, yyyy, h:mm a", Locale.getDefault())
            .withZone(ZoneId.systemDefault())
            .format(Instant.ofEpochMilli(attempt.completedAt))
        val percentage = if (attempt.totalQuestions > 0) {
            attempt.score * 100 / attempt.totalQuestions
        } else {
            0
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Score: ${attempt.score}/${attempt.totalQuestions} ($percentage%)",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Completed: $completedAt",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}