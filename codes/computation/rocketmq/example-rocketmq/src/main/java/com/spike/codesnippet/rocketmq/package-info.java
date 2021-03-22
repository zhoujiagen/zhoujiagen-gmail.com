/**
 * <pre>
 * Setup
 * 
 * Configuration: 见<a href="https://rocketmq.apache.org/docs/rmq-deployment/">Deployment</a>
 * conf/broker.conf: add 
 * brokerIP1 = 172.168.1.185. 
 * enablePropertyFilter = true
 * 
 * sh bin/mqnamesrv -n 172.168.1.185:9876
 * sh bin/mqbroker -n 172.168.1.185:9876 -c conf/broker.conf
 * </pre>
 */
package com.spike.codesnippet.rocketmq;