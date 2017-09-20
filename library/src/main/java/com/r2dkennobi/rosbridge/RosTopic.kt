package com.r2dkennobi.rosbridge

/**
 * Created by Kenny Yokoyama on 6/7/17.
 *
 * ROS topic class. Represents a topic and corresponding topic type.
 */
class RosTopic(val topic: String = "", val type: String = "") {
    override fun equals(other: Any?): Boolean =
            other != null && other is RosTopic && other.topic == topic && other.type == type

    override fun hashCode(): Int = super.hashCode()
}