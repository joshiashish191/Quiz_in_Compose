package net.softglobe.quizincompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.softglobe.quizincompose.model.QuestionModel
import net.softglobe.quizincompose.model.QuizAttempt
import net.softglobe.quizincompose.model.Repository


data class UiState(
    val isResultDisplayed : Boolean = false,
    val isHistoryDisplayed: Boolean = false,
    val isAttemptSaved: Boolean = false,
    val totalScore : Int = 0,
    val score : Int = 0,
    val questions : List<QuestionModel> = emptyList(),
    val currentQuestionIndex : Int = 0,
    val selectedOptionIndex : Int = -1,
    val attemptHistory: List<QuizAttempt> = emptyList()
)

class QuizViewModel(private val repository: Repository) : ViewModel() {

    var uiState = MutableStateFlow(UiState())
    private set

    init {
        viewModelScope.launch {
            repository.getAllQuestions().collect { questions ->
                if (questions.isEmpty()) {
                    repository.insertQuestions(getQuestions())
                } else {
                    uiState.update {
                        it.copy(
                            questions = questions,
                            totalScore = questions.size
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            repository.getAllAttempts().collect { attempts ->
                uiState.update {
                    it.copy(attemptHistory = attempts)
                }
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

    fun onOptionSelected(index: Int) {
        uiState.update {
            it.copy(
                selectedOptionIndex = index
            )
        }
    }

    fun onNextClicked() {
        val selectedOptionIndex = uiState.value.selectedOptionIndex
        val currentQuestion = uiState.value.questions[uiState.value.currentQuestionIndex]
        val finalScore = uiState.value.score +
            if (selectedOptionIndex == currentQuestion.correctAnswerIndex) 1 else 0

        if (selectedOptionIndex == currentQuestion.correctAnswerIndex) {
            uiState.update {
                it.copy(
                    score = it.score+1
                )
            }
        }
        if (uiState.value.currentQuestionIndex < uiState.value.questions.size - 1) {
            uiState.update {
                it.copy(
                    currentQuestionIndex = it.currentQuestionIndex+1,
                    selectedOptionIndex = -1
                )
            }
        } else {
            if (uiState.value.isAttemptSaved) return
            uiState.update {
                it.copy(
                    score = finalScore,
                    isResultDisplayed = true,
                    isAttemptSaved = true
                )
            }
            viewModelScope.launch {
                repository.insertAttempt(
                    QuizAttempt(
                        score = finalScore,
                        totalScore = uiState.value.questions.size,
                        passed = finalScore >= uiState.value.questions.size * 0.5,
                        completedAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun restartQuiz() {
        uiState.update {
            it.copy(
                isResultDisplayed = false,
                isHistoryDisplayed = false,
                isAttemptSaved = false,
                score = 0,
                selectedOptionIndex = -1,
                currentQuestionIndex = 0
            )
        }
    }

    fun showHistory() {
        uiState.update {
            it.copy(isHistoryDisplayed = true)
        }
    }

    fun hideHistory() {
        uiState.update {
            it.copy(isHistoryDisplayed = false)
        }
    }
}