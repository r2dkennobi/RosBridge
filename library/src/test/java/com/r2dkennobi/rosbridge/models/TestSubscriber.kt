package com.r2dkennobi.rosbridge.models

import com.r2dkennobi.rosbridge.RosBridgeSubscriberI
import com.r2dkennobi.rosbridge.RosTopic
import org.json.JSONObject
import java.util.*

/**
 * Created by Kenny Yokoyama on 4/18/17.
 *
 * ROS subscriber test fixture
 */
class TestSubscriber : RosBridgeSubscriberI {
    private val mTopic1 = RosTopic("dummy/topic1", "dummy_type1")
    private val mTopic2 = RosTopic("dummy/topic2", "dummy_type2")

    override fun parseMessage(topic: String, msg: JSONObject): Boolean =
            mTopic1.topic == topic || mTopic2.topic == topic

    override val rosTopics: List<RosTopic>
        get() {
            val topics = ArrayList<RosTopic>()
            topics.add(mTopic1)
            topics.add(mTopic2)
            return topics
        }
}
