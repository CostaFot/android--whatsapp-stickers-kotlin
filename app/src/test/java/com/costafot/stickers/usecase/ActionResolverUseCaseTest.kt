package com.costafot.stickers.usecase

import com.costafot.stickers.CoroutinesTestRule
import com.costafot.stickers.runBlockingTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.Verify
import org.amshove.kluent.called
import org.amshove.kluent.mock
import org.amshove.kluent.on
import org.amshove.kluent.that
import org.amshove.kluent.was
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class ActionResolverUseCaseTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val whiteListCheckUseCase = mock<WhiteListCheckUseCase>()
    private lateinit var actionResolverUseCase: ActionResolverUseCase

    @Before
    fun setup() {
        actionResolverUseCase = ActionResolverUseCase(coroutinesTestRule.testDispatcherProvider, whiteListCheckUseCase)
    }

    @Test
    fun resolveActionAddStickerPack() {
        coroutinesTestRule.runBlockingTest {

            val expected = AddStickerPackAction.APPS_NOT_FOUND
            val actual = actionResolverUseCase.resolveActionAddStickerPack("random")
            Verify on whiteListCheckUseCase that whiteListCheckUseCase.isWhatsAppConsumerAppInstalled() was called
            assertEquals(expected, actual)
        }

        coroutinesTestRule.testDispatcher.runBlockingTest {
        }
    }
}
