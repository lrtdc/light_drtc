package org.light.rtc.base;

import java.util.Timer;

import org.light.rtc.timer.MqDoTimer;
import org.light.rtc.util.Constants;

public class MqConsumer {
	
	protected MqDoTimer mqTimer = new MqDoTimer();
	
	public MqConsumer(){
		Timer streamLogTimer = new Timer();
		streamLogTimer.schedule(mqTimer, 1000, 1000 * Constants.mqDoBatchTimer);
	}
	
}
