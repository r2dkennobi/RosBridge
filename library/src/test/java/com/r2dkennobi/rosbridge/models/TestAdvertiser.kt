package com.r2dkennobi.rosbridge.models

import com.r2dkennobi.rosbridge.RosBridgeAdvertiserI
import com.r2dkennobi.rosbridge.RosTopic

/**
 * Created by Kenny Yokoyama on 4/18/17.
 *
 * ROS bridge advertiser test fixture
 */
class TestAdvertiser : RosBridgeAdvertiserI {
    override val rosTopic = RosTopic("dummy/topic", "dummy_type")
}
