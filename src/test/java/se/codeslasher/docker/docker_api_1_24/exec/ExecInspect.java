package se.codeslasher.docker.docker_api_1_24.exec;

import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.codeslasher.docker.DefaultDockerClient;
import se.codeslasher.docker.DockerClient;
import se.codeslasher.docker.model.api124.ExecInfo;
import se.codeslasher.docker.model.api124.Volume;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by karl on 9/24/16.
 */
public class ExecInspect {
    private DockerClient client;
    private static Logger logger = LoggerFactory.getLogger(ExecInspect.class);

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
    public void inspect() {
        final String path = "/v1.24%2Fexec%2F35149c4cf8055c5cd524e109c8abff3b03d54e558e0633426860907cf793c714%2Fjson";

        ExecInfo exec = client.inspectExec("35149c4cf8055c5cd524e109c8abff3b03d54e558e0633426860907cf793c714");

        assertThat(exec).isNotNull();
        assertThat(exec.isCanRemove()).isEqualTo(false);
        assertThat(exec.getProcessConfig().getEntryPoint()).isEqualTo("date");

        UrlPattern pattern = UrlPattern.fromOneOf(path, null,null,null);
        RequestPatternBuilder requestPatternBuilder = RequestPatternBuilder.newRequestPattern(RequestMethod.GET,pattern);

        wireMockRule.verify(1, requestPatternBuilder);
    }


}
