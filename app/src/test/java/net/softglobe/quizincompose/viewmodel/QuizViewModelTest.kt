package net.softglobe.quizincompose.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import net.softglobe.quizincompose.model.QuestionModel
import net.softglobe.quizincompose.model.QuizDao
import net.softglobe.quizincompose.model.Repository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TestQuizDao : QuizDao {
    private val flow = MutableStateFlow<List<QuestionModel>>(emptyList())
    override suspend fun insertQuestions(questions: List<QuestionModel>) {
        flow.value = questions
    }

    override fun getAllQuestions() = flow
}

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class QuizViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        kotlinx.coroutines.Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        kotlinx.coroutines.Dispatchers.resetMain()
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    @Test
    fun testGetQuestionsAndNavigation() = runTest {
        val dao = TestQuizDao()
        val repository = Repository(dao)
        val vm = QuizViewModel(repository)

        // initial state: questions inserted by init
        // advance until coroutines run
        dispatcher.scheduler.advanceUntilIdle()

        val qs = vm.getQuestions()
        assertEquals(3, qs.size)

        vm.onOptionSelected(1)
        assertEquals(1, vm.uiState.value.selectedOptionIndex)

        // simulate correct answer for first question (index 1)
        vm.onNextClicked()
        assertEquals(1, vm.uiState.value.score)

        // restart
        vm.restartQuiz()
        assertEquals(0, vm.uiState.value.score)
        assertEquals(0, vm.uiState.value.currentQuestionIndex)
    }
}
