package com.r2dkennobi.rosbridge

import com.r2dkennobi.rosbridge.models.TestSubscriber
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Created by Kenny Yokoyama on 4/18/17.
 *
 * ROS bridge subscriber interface test
 */
class RosBridgeSubscriberTest {
    lateinit private var subscriber: TestSubscriber
    private val mTopic1 = RosTopic("dummy/topic1", "dummy_type1")
    private val mTopic2 = RosTopic("dummy/topic2", "dummy_type2")
    private val mTopic3 = RosTopic("dummy/topic3", "dummy_type3")

    @Before
    fun setup() {
        subscriber = TestSubscriber(arrayListOf(mTopic1, mTopic2))
        subscriber.loadModule()
    }

    @After
    fun teardown() {
        subscriber.unloadModule()
    }

    @Test
    fun testEquality() {
        // TODO maybe I should allow equality when array is not ordered a certain way...
        val target = TestSubscriber(arrayListOf(mTopic1, mTopic2))
        val dummy = TestSubscriber(arrayListOf(mTopic1, mTopic2, mTopic3))
        assertEquals(subscriber, target)
        assertEquals(subscriber.hashCode(), target.hashCode())
        assertNotEquals(subscriber, dummy)
        assertNotEquals(subscriber.hashCode(), dummy.hashCode())
    }

    @Test
    fun testParseMessage() {
        assertTrue(subscriber.parseMessage(mTopic1.topic, JSONObject()))
        assertTrue(subscriber.parseMessage(mTopic2.topic, JSONObject()))
        assertFalse(subscriber.parseMessage(mTopic1.type, JSONObject()))
    }

    @Test
    fun testGetTopics() {
        val topics = subscriber.subscribingTopics
        assertTrue(topics.contains(mTopic1))
        assertTrue(topics.contains(mTopic2))
        assertFalse(topics.contains(mTopic3))
        assertTrue(topics.isNotEmpty())
    }
}