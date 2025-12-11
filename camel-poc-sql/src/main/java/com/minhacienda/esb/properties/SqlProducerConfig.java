package com.minhacienda.esb.properties;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "sql.producer")
public interface SqlProducerConfig {
    public String getQuery();
}
