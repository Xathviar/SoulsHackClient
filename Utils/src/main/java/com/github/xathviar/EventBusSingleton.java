package com.github.xathviar;

import com.google.common.eventbus.EventBus;
import lombok.Data;

@Data
public class EventBusSingleton {
    public static final EventBus eventbus = new EventBus();
}
