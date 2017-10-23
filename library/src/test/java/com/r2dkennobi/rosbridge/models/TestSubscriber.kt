package com.r2dkennobi.rosbridge.models

import com.r2dkennobi.rosbridge.RosBridgeSubscriber
import com.r2dkennobi.rosbridge.RosTopic
import org.json.JSONObject

/**
 * Created by Kenny Yokoyama on 4/18/17.
 *
 * ROS subscriber test fixture
 */
private val defaultList: List<RosTopic> = arrayListOf(RosTopic("dummy/topic1", "dummy_type1"), RosTopic("dummy/topic2", "dummy_type2"))
class TestSubscriber(private val topics: List<RosTopic> = defaultList) : RosBridgeSubscriber() {
    override fun isNewDataAvailable(): Boolean = true

    override fun loadModule() {}

    override fun unloadModule() {}

    override fun hashCode(): Int = subscribingTopics.hashCode()

    override fun equals(other: Any?): Boolean =
            other is RosBridgeSubscriber && other.subscribingTopics == subscribingTopics

    override fun parseMessage(topic: String, msg: JSONObject): Boolean =
            topics.any { it.topic == topic }

    override val subscribingTopics: List<RosTopic> = topics
}
