package com.search.vehicle.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.http.HttpHeaders;

/**
 * Contains the configuration for elasticsearch setup.
 * @author Vansh Pratap Singh
 */
@Configuration
public class ElasticsearchConfiguration {

    @Value("${elasticsearch.host}")
    private String elasticsearchHost;

    @Value("${elasticsearch.user.name}")
    private String elasticsearchUserName;

    @Value("${elasticsearch.user.password}")
    private String elasticsearchPassword;

    /**
     * Creates the RestHighLevelClient bean with the required credentials.
     * @return              RestHighLevelClient bean.
     */
    @Bean
    public RestHighLevelClient restHighLevelClient() {

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Accept", "application/vnd.elasticsearch+json;compatible-with=7");
        httpHeaders.add("Content-Type", "application/vnd.elasticsearch+json;" + "compatible-with=7");

        ClientConfiguration clientConfiguration
                = ClientConfiguration.builder()
                .connectedTo(elasticsearchHost)
                .withBasicAuth(elasticsearchUserName, elasticsearchPassword)
                .withDefaultHeaders(httpHeaders)
                .build();

        return RestClients.create(clientConfiguration).rest();

    }

    /**
     * Creates the ElasticsearchRestTemplate bean with the required credentials.
     * @return              ElasticsearchRestTemplate bean.
     */
    @Bean
    public ElasticsearchRestTemplate elasticsearchTemplate() {

        return new ElasticsearchRestTemplate(restHighLevelClient());

    }

}
