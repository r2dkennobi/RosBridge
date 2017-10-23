package com.r2dkennobi.rosbridge

import com.r2dkennobi.rosbridge.models.TestAdvertiser
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Kenny Yokoyama on 4/18/17.
 *
 * ROS bridge advertiser interface tester
 */
class RosBridgeAdvertiserTest {
    lateinit private var advertiser: TestAdvertiser
    lateinit private var advertiser2: TestAdvertiser
    private val mTopic = RosTopic("dummy/topic", "dummy_type")

    @Before
    fun setup() {
        advertiser = TestAdvertiser()
        advertiser2 = TestAdvertiser(RosTopic("dummy/topic2", "dummy_type2"))
        advertiser.loadModule()
        advertiser2.loadModule()
    }

    @After
    fun teardown() {
        advertiser.unloadModule()
        advertiser2.unloadModule()
    }

    @Test
    fun equalityTest() {
        val target = TestAdvertiser()
        assertEquals(advertiser, target)
        assertEquals(advertiser.hashCode(), target.hashCode())
        assertNotEquals(advertiser, advertiser2)
        assertNotEquals(advertiser.hashCode(), advertiser2.hashCode())
    }

    @Test
    fun testGetTopic() {
        assertEquals(mTopic.topic, advertiser.advertisingTopic.topic)
        assertNotEquals(mTopic.topic, advertiser.advertisingTopic.type)
    }

    @Test
    fun testGetType() {
        assertEquals(mTopic.type, advertiser.advertisingTopic.type)
        assertNotEquals(mTopic.type, advertiser.advertisingTopic.topic)
    }

}