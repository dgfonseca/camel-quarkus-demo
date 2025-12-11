package com.minhacienda.esb.properties;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "amq.consumer")
public interface AmqProducerConfig {
    public String getUser();	
	public String getPasswd();
	public String getQueueName();
	public String getHostName();	
	public String getPort();
}
