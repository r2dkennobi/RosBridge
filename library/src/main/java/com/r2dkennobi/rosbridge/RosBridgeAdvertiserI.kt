package com.r2dkennobi.rosbridge

/**
 * Created by Kenny Yokoyama on 3/27/17.
 *
 * Interface to be implemented by module that wishes to publish message onto
 * a ROS topic.
 */
interface RosBridgeAdvertiserI {
    /**
     * Returns the topic to publish on.
     *
     * @return Topic to publish the message on
     */
    val rosTopic: RosTopic
}