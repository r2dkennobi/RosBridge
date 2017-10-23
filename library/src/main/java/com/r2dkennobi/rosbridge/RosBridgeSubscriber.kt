package com.r2dkennobi.rosbridge

import org.json.JSONObject

/**
 * Created by Kenny Yokoyama on 3/14/17.
 *
 * Interface to be implemented by module that wishes to subscribe to message.
 * on a ROS topic.
 */
abstract class RosBridgeSubscriber : RosModule() {
    /**
     * Converts the incoming JSON object for the specified topic into
     * the desired data type.
     *
     * @param topic Topic the message came from
     * @param msg   JSON object of the message
     */
    abstract fun parseMessage(topic: String, msg: JSONObject): Boolean

    /**
     * List of topic/type pairs for the module to subscribe to.
     *
     * @return List of topics
     */
    abstract val subscribingTopics: List<RosTopic>
}