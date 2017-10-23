package com.r2dkennobi.rosbridge

import java.util.concurrent.ConcurrentHashMap

/**
 * ROS module base class. A typical class will inherit this class and then implement either the
 * advertiser or subscriber interface.
 *
 * @author Kenny Yokoyama
 * @since 10/23/17
 */
abstract class RosModule {
    /**
     * Map of clients that listen to data updates from the ROS module
     */
    private val mPeers = ConcurrentHashMap<String, RosModuleInterface>()

    /**
     * Called when the module is explicitly initialized in the application
     */
    abstract fun loadModule()

    /**
     * Called when the module is explicitly de-initialized in the application
     */
    abstract fun unloadModule()

    /**
     * Called to check if new data is available
     */
    abstract fun isNewDataAvailable(): Boolean

    /**
     * Called when a new listener wants to subscribe to the data updates. If the listener
     * is added to the index and new data is available, the callback will immediately
     * get called.
     *
     * @param tag String: Caller ID
     * @param listener RosModuleInterface: Callback
     */
    fun addListener(tag: String, listener: RosModuleInterface) {
        if (!this.mPeers.containsKey(tag)) {
            this.mPeers.put(tag, listener)
            if (isNewDataAvailable()) {
                listener.newDataAvailable()
            }
        }
    }

    /**
     * Called when a listener wants to stop listening to the data updates
     *
     * @param tag String: Caller ID
     */
    fun removeListener(tag: String) {
        if (this.mPeers.containsKey(tag)) {
            this.mPeers.remove(tag)
        }
    }

    /**
     * Override the default hashCode function
     */
    abstract override fun hashCode(): Int

    /**
     * Override the default equals function
     */
    abstract override fun equals(other: Any?): Boolean
}