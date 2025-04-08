package com.torilab.assignment.testfoundations

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

interface ScopeTestable {
    val dispatcher: TestDispatcher
    val scope: TestScope

    fun setupScope()
    fun tearDownScope()
}

@OptIn(ExperimentalCoroutinesApi::class)
class RealScopeTestable : ScopeTestable {
    override val dispatcher: TestDispatcher = StandardTestDispatcher()
    override val scope: TestScope = TestScope(dispatcher)

    override fun setupScope() {
        Dispatchers.setMain(dispatcher)
    }

    override fun tearDownScope() {
        Dispatchers.resetMain()
    }
}
