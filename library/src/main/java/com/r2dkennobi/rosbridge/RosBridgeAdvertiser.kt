package com.r2dkennobi.rosbridge

/**
 * Interface to be implemented by module that wishes to publish message onto
 * a ROS topic.
 *
 * @author Kenny Yokoyama
 * @since 3/27/17
 */
abstract class RosBridgeAdvertiser : RosModule() {
    /**
     * Returns the topic to publish on.
     *
     * @return Topic to publish the message on
     */
    abstract val advertisingTopic: RosTopic
}