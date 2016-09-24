package se.codeslasher.docker.docker_api_1_24.container;

import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import org.junit.*;
import se.codeslasher.docker.*;
import se.codeslasher.docker.model.api124.Container;
import se.codeslasher.docker.model.api124.DockerImageName;
import se.codeslasher.docker.utils.Filters;

import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by karl on 9/10/16.
 */
public class ContainerList {

    private DockerClient client;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options().usingFilesUnderClasspath("src/test/resources/1_24").port(9779)); // No-args constructor defaults

    @Before
    public void setup() {
        client = new DefaultDockerClient("http://127.0.0.1:9779");
    }

    @After
    public void tearDown() {
        client.close();
    }

    @Test
    public void list() {
        List<Container> containerList = client.listContainers();

        assertThat(containerList.size()).isEqualTo(2);

        DockerImageName mongo = new DockerImageName("mongo");

        Container one = containerList.get(0);
        Container two = containerList.get(1);

        System.out.println(one.getState());

        assertThat(one.getImage()).isEqualTo(mongo);
        assertThat(two.getImage()).isEqualTo(mongo);

        UrlPattern pattern = UrlPattern.fromOneOf("/v1.24/containers/json", null,null,null);
        RequestPatternBuilder requestPatternBuilder = RequestPatternBuilder.newRequestPattern(RequestMethod.GET,pattern);

        wireMockRule.verify(1, requestPatternBuilder);
    }

    @Test
    public void listAll() {
        ContainerListRequest request = ContainerListRequest.builder().all(true).build();
        List<Container> containerList = client.listContainers(request);

        assertThat(containerList.size()).isEqualTo(3);

        DockerImageName mongo = new DockerImageName("mongo");
        DockerImageName ubuntu = new DockerImageName("ubuntu:14.04");

        Container one = containerList.get(0);
        Container two = containerList.get(1);
        Container three = containerList.get(2);

        assertThat(one.getImage()).isEqualTo(mongo);
        assertThat(two.getImage()).isEqualTo(mongo);
        assertThat(three.getImage()).isEqualTo(ubuntu);

        UrlPattern pattern = UrlPattern.fromOneOf("/v1.24/containers/json?all=true", null,null,null);
        RequestPatternBuilder requestPatternBuilder = RequestPatternBuilder.newRequestPattern(RequestMethod.GET,pattern);

        wireMockRule.verify(1, requestPatternBuilder);
    }

    @Test
    public void listSince() {
        final String path = "/v1.24/containers/json?since=mongo";
        ContainerListRequest request = ContainerListRequest.builder().since("mongo").build();
        List<Container> containerList = client.listContainers(request);

        DockerImageName mongo = new DockerImageName("mongo");

        assertThat(containerList.size()).isEqualTo(1);
        assertThat(containerList.get(0).getImage()).isEqualTo(mongo);
        assertThat(containerList.get(0).getNames().get(0)).isEqualTo("/new_mongo");

        UrlPattern pattern = UrlPattern.fromOneOf(path, null,null,null);
        RequestPatternBuilder requestPatternBuilder = RequestPatternBuilder.newRequestPattern(RequestMethod.GET,pattern);

        wireMockRule.verify(1, requestPatternBuilder);
    }

    @Test
    public void listBefore() {
        final String path = "/v1.24/containers/json?before=new_mongo";
        ContainerListRequest request = ContainerListRequest.builder().before("new_mongo").build();
        List<Container> containerList = client.listContainers(request);

        DockerImageName mongo = new DockerImageName("mongo");

        assertThat(containerList.size()).isEqualTo(2);
        assertThat(containerList.get(0).getImage()).isEqualTo(mongo);
        assertThat(containerList.get(1).getImage()).isEqualTo(mongo);
        assertThat(containerList.get(0).getNames().get(0)).isEqualTo("/new_mongo");
        assertThat(containerList.get(1).getNames().get(0)).isEqualTo("/mongo");

        UrlPattern pattern = UrlPattern.fromOneOf(path, null,null,null);
        RequestPatternBuilder requestPatternBuilder = RequestPatternBuilder.newRequestPattern(RequestMethod.GET,pattern);

        wireMockRule.verify(1, requestPatternBuilder);
    }

    @Test
    public void listLimit() {
        ContainerListRequest request = ContainerListRequest.builder().limit(1).build();
        List<Container> containerList = client.listContainers(request);


        assertThat(containerList.size()).isEqualTo(1);

        DockerImageName mongo = new DockerImageName("mongo");

        Container one = containerList.get(0);

        assertThat(one.getImage()).isEqualTo(mongo);

        UrlPattern pattern = UrlPattern.fromOneOf("/v1.24/containers/json?limit=1", null,null,null);
        RequestPatternBuilder requestPatternBuilder = RequestPatternBuilder.newRequestPattern(RequestMethod.GET,pattern);

        wireMockRule.verify(1, requestPatternBuilder);
    }

    @Test
    public void listSize() {
        ContainerListRequest request = ContainerListRequest.builder().size(true).build();
        List<Container> containerList = client.listContainers(request);

        assertThat(containerList.size()).isEqualTo(2);

        DockerImageName mongo = new DockerImageName("mongo");

        Container one = containerList.get(0);
        Container two = containerList.get(1);

        assertThat(one.getImage()).isEqualTo(mongo);
        assertThat(two.getImage()).isEqualTo(mongo);

        UrlPattern pattern = UrlPattern.fromOneOf("/v1.24/containers/json?size=true", null,null,null);
        RequestPatternBuilder requestPatternBuilder = RequestPatternBuilder.newRequestPattern(RequestMethod.GET,pattern);

        wireMockRule.verify(1, requestPatternBuilder);
    }

    @Test
    public void listFilterBefore() {
        final String path = "/v1.24/containers/json?filters=%7B%22before%22%3A%7B%22new_mongo%22%3Atrue%7D%7D";

        Filters filters = new Filters();
        filters.add("before","new_mongo");

        ContainerListRequest request = ContainerListRequest.builder().filters(filters).build();
        List<Container> containerList = client.listContainers(request);

        assertThat(containerList.size()).isEqualTo(1);
        assertThat(containerList.get(0).getNames().get(0)).isEqualTo("/mongo");

        UrlPattern pattern = UrlPattern.fromOneOf(path, null,null,null);
        RequestPatternBuilder requestPatternBuilder = RequestPatternBuilder.newRequestPattern(RequestMethod.GET,pattern);

        wireMockRule.verify(1, requestPatternBuilder);
    }

    @Test
    public void listFilterSince() {
        final String path = "/v1.24/containers/json?filters=%7B%22since%22%3A%7B%22mongo%22%3Atrue%7D%7D";

        Filters filters = new Filters();
        filters.add("since","mongo");

        ContainerListRequest request = ContainerListRequest.builder().filters(filters).build();
        List<Container> containerList = client.listContainers(request);

        DockerImageName mongo = new DockerImageName("mongo");

        assertThat(containerList.size()).isEqualTo(1);
        assertThat(containerList.get(0).getNames().get(0)).isEqualTo("/new_mongo");
        assertThat(containerList.get(0).getImage()).isEqualTo(mongo);

        UrlPattern pattern = UrlPattern.fromOneOf(path, null,null,null);
        RequestPatternBuilder requestPatternBuilder = RequestPatternBuilder.newRequestPattern(RequestMethod.GET,pattern);

        wireMockRule.verify(1, requestPatternBuilder);
    }



}