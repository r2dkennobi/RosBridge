package com.r2dkennobi.rosbridge

import com.r2dkennobi.rosbridge.models.TestAdvertiser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Kenny Yokoyama on 4/18/17.
 *
 * ROS bridge advertiser interface tester
 */
class RosBridgeAdvertiserITest {
    lateinit private var advertiser: TestAdvertiser
    private val mTopic = RosTopic("dummy/topic", "dummy_type")

    @Before
    fun setup() {
        advertiser = TestAdvertiser()
    }

    @Test
    fun testGetTopic() {
        assertEquals(mTopic.topic, advertiser.rosTopic.topic)
        assertNotEquals(mTopic.topic, advertiser.rosTopic.type)
    }

    @Test
    fun testGetType() {
        assertEquals(mTopic.type, advertiser.rosTopic.type)
        assertNotEquals(mTopic.type, advertiser.rosTopic.topic)
    }

}