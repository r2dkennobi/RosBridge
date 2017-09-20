package com.r2dkennobi.rosbridge

import com.r2dkennobi.rosbridge.models.TestSubscriber
import org.json.JSONObject
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Created by Kenny Yokoyama on 4/18/17.
 *
 * ROS bridge subscriber interface test
 */
class RosBridgeSubscriberITest {
    lateinit private var subscriber: TestSubscriber
    private val mTopic1 = RosTopic("dummy/topic1", "dummy_type1")
    private val mTopic2 = RosTopic("dummy/topic2", "dummy_type2")
    private val mTopic3 = RosTopic("dummy/topic3", "dummy_type3")

    @Before
    fun setup() {
        subscriber = TestSubscriber()
    }

    @Test
    fun testParseMessage() {
        assertTrue(subscriber.parseMessage(mTopic1.topic, JSONObject()))
        assertTrue(subscriber.parseMessage(mTopic2.topic, JSONObject()))
        assertFalse(subscriber.parseMessage(mTopic1.type, JSONObject()))
    }

    @Test
    fun testGetTopics() {
        val topics = subscriber.rosTopics
        assertTrue(topics.contains(mTopic1))
        assertTrue(topics.contains(mTopic2))
        assertFalse(topics.contains(mTopic3))
        assertTrue(topics.isNotEmpty())
    }
}