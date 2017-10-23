package com.r2dkennobi.rosbridge

/**
 * Callback for the ROS module to notify module owner of data update
 *
 * @author Kenny Yokoyama
 * @since 10/23/17
 */
interface RosModuleInterface {
    /**
     * Called when there is new data available in the module.
     */
    fun newDataAvailable()
}