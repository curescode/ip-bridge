package com.curescode.bridge.domain;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * channel entity which alive
 * @author Cure
 * @date 2024/02/03 23:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnabledChannel {

    private Channel channel;

    private long updateTimeMillis;

}
