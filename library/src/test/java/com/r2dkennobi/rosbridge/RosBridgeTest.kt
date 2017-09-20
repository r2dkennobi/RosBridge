package com.r2dkennobi.rosbridge

import com.r2dkennobi.rosbridge.models.MockRosBridge
import com.r2dkennobi.rosbridge.models.TestAdvertiser
import com.r2dkennobi.rosbridge.models.TestSubscriber
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Created by Kenny Yokoyama on 4/18/17.
 *
 * ROS bridge test fixture
 */
class RosBridgeTest {
    lateinit private var mRosBridge: RosBridge
    lateinit private var mAdvertiser: TestAdvertiser
    lateinit private var mSubscriber: TestSubscriber
    private val mTopic = RosTopic("dummy/topic", "dummy/type")

    @Before
    fun setup() {
        mRosBridge = MockRosBridge()
        mAdvertiser = TestAdvertiser()
        mSubscriber = TestSubscriber()
    }

    @After
    fun teardown() = mRosBridge.cancelRosBridge()

    @Test
    fun testInitialization() {
        assertTrue(mRosBridge.initRosBridge("dumbdumb"))
        assertFalse(mRosBridge.initRosBridge("dumbdumb"))
    }

    @Test
    fun testAddAdvertiser() {
        assertTrue(mRosBridge.addAdvertiser(mAdvertiser))
        assertFalse(mRosBridge.addAdvertiser(mAdvertiser))
    }

    @Test
    fun testAddSubscriber() {
        assertTrue(mRosBridge.addSubscriber(mSubscriber))
        assertFalse(mRosBridge.addSubscriber(mSubscriber))
    }

    @Test
    fun testRemoveAdvertiser() {
        mRosBridge.addAdvertiser(mAdvertiser)
        assertTrue(mRosBridge.removeAdvertiser(mAdvertiser))
        assertFalse(mRosBridge.removeAdvertiser(mAdvertiser))
    }

    @Test
    fun testRemoveSubscriber() {
        mRosBridge.addSubscriber(mSubscriber)
        assertTrue(mRosBridge.removeSubscriber(mSubscriber))
        assertFalse(mRosBridge.removeSubscriber(mSubscriber))
    }

    @Test
    fun testPublishMessage() {
        assertTrue(mRosBridge.initRosBridge("dumbdumb"))
        assertTrue(mRosBridge.publishToTopic(mTopic.topic, JSONObject()))
        assertTrue(mRosBridge.publishToTopic(mTopic.topic, JSONObject()))
    }

    @Test
    fun testPublishMessageFail() {
        assertFalse(mRosBridge.publishToTopic(mTopic.topic, JSONObject()))
        assertFalse(mRosBridge.publishToTopic(mTopic.topic, JSONObject()))
    }
}