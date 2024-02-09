package com.curescode.bridge.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ip address info entity
 * @author Cure
 * @date 2024/02/01 15:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IPAddress {

    private String ip;

    private int port;
}
