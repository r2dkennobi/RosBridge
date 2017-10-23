package com.r2dkennobi.rosbridge.models

import com.r2dkennobi.rosbridge.RosBridgeAdvertiser
import com.r2dkennobi.rosbridge.RosTopic

/**
 * Created by Kenny Yokoyama on 4/18/17.
 *
 * ROS bridge advertiser test fixture
 */
class TestAdvertiser(topic: RosTopic=RosTopic("dummy/topic", "dummy_type")) : RosBridgeAdvertiser() {
    override fun isNewDataAvailable(): Boolean = true

    override fun loadModule() {}

    override fun unloadModule() {}

    override fun hashCode(): Int = advertisingTopic.hashCode()

    override fun equals(other: Any?): Boolean =
            other is RosBridgeAdvertiser && other.advertisingTopic == this.advertisingTopic

    override val advertisingTopic = topic
}
