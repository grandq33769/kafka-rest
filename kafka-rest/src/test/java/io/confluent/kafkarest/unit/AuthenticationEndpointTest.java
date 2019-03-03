/*
 * Copyright 2018 Confluent Inc.
 *
 * Licensed under the Confluent Community License (the "License"); you may not use
 * this file except in compliance with the License.  You may obtain a copy of the
 * License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.confluent.kafkarest.unit;

import javax.ws.rs.core.MediaType;

import io.confluent.kafkarest.AdminClientWrapper;
import io.confluent.kafkarest.DefaultKafkaRestContext;
import io.confluent.kafkarest.Errors;
import io.confluent.kafkarest.KafkaRestApplication;
import io.confluent.kafkarest.KafkaRestConfig;
import io.confluent.kafkarest.ProducerPool;
import io.confluent.kafkarest.TestUtils;
import io.confluent.kafkarest.entities.EntityUtils;
import io.confluent.kafkarest.entities.Partition;
import io.confluent.kafkarest.entities.PartitionReplica;
import io.confluent.kafkarest.entities.Topic;
import io.confluent.kafkarest.resources.AuthenticationEndpoint;
import io.confluent.rest.EmbeddedServerTestHarness;
import io.confluent.rest.RestConfigException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static io.confluent.kafkarest.TestUtils.assertErrorResponse;
import static io.confluent.kafkarest.TestUtils.assertOKResponse;
import static org.junit.Assert.assertEquals;

public class AuthenticationEndpointTest extends EmbeddedServerTestHarness<KafkaRestConfig, KafkaRestApplication> {

    private AdminClientWrapper adminClientWrapper;
    private ProducerPool producerPool;
    private DefaultKafkaRestContext ctx;

    public AuthenticationEndpointTest() throws RestConfigException {
        adminClientWrapper = EasyMock.createMock(AdminClientWrapper.class);
        producerPool = EasyMock.createMock(ProducerPool.class);
        ctx = new DefaultKafkaRestContext(config, null, producerPool, null, null, null, adminClientWrapper);

        addResource(new AuthenticationEndpoint());
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        EasyMock.reset(adminClientWrapper, producerPool);
    }

    @Test
    public void testAuthenticationRespond() {
        Form urlCredentials = new Form();
        urlCredentials.param("username", "admin").param("password", "12345678");

        Response response = request("/authentication", MediaType.APPLICATION_JSON).post(Entity.form(urlCredentials));
        assertOKResponse(response, MediaType.APPLICATION_JSON);
        System.out.println(response.readEntity(String.class));

        EasyMock.reset(adminClientWrapper);
    }
}
