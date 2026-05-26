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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.softglobe.quizincompose.ui.theme.QuizInComposeTheme
import kotlin.text.compareTo

class MainActivity : ComponentActivity() {
    var isResultDisplayed by mutableStateOf(false)
    var totalScore = getQuestions().size
    var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizInComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (!isResultDisplayed) {
                            QuizScreen()
                        } else {
                            ResultScreen()
                        }
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun QuizScreen() {
        val questionsList = remember { getQuestions() }
        var currentQuestionIndex by remember { mutableIntStateOf(0) }
        val currentQuestion = questionsList[currentQuestionIndex]
        var selectedOptionIndex by remember { mutableIntStateOf(-1) }

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
                            selectedOptionIndex = index
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (selectedOptionIndex == index),
                        onClick = { selectedOptionIndex = index }
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
                    if (selectedOptionIndex == currentQuestion.correctAnswerIndex) {
                        score++
                    }
                    if (currentQuestionIndex < questionsList.size - 1) {
                        currentQuestionIndex++
                        selectedOptionIndex = -1
                    } else {
                        isResultDisplayed = true
                    }
                }
            ) {
                Text(
                    text = if (currentQuestionIndex == questionsList.size-1) "Show Result" else "Next Question"
                )
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    private fun ResultScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val isPassed = score >= totalScore*0.5
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
                onClick = {
                    isResultDisplayed = false
                    score = 0
                }
            ) {
                Text(
                    text = "Attempt again"
                )
            }
        }
    }

    fun getQuestions() : List<QuestionModel> {
        return listOf(
            QuestionModel(
                question = "Which of the following is software?",
                options = listOf(
                    "Keyboard",
                    "MS Word",
                    "Speakers",
                    "Mouse"
                ),
                correctAnswerIndex = 1
            ),
            QuestionModel(
                question = "What does RAM stands for",
                options = listOf(
                    "Review Admittance Monitor",
                    "Review Admittance Memory",
                    "Random Access Memory",
                    "Random Access Monitor"
                ),
                correctAnswerIndex = 2
            ),
            QuestionModel(
                question = "What does BIOS stand for",
                options = listOf(
                    "Basic Input/Output System",
                    "Basic Inbox/Outbox System",
                    "Between Input/Output System",
                    "Between Input/Output Service"
                ),
                correctAnswerIndex = 0
            ),
        )
    }
}