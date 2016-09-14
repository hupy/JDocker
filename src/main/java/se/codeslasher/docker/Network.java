package se.codeslasher.docker;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

/**
 * Created by karl on 9/11/16.
 */
@JsonPropertyOrder({
        "IPAMConfig",
        "Links",
        "Aliases",
        "NetworkID",
        "EndpointID",
        "Gateway",
        "IPAddress",
        "IPPrefixLen",
        "IPv6Gateway",
        "GlobalIPv6Address",
        "GlobalIPv6PrefixLen",
        "MacAddress"
})
@Getter
public class Network {

    @JsonProperty("IPAMConfig")
    private IPAMConfig ipamConfig;

    @JsonProperty("Links")
    private String links;

    @JsonProperty("Aliases")
    private String aliases;

    @JsonProperty("NetworkID")
    private String networkId;

    @JsonProperty("EndpointID")
    private String endpointId;

    @JsonProperty("Gateway")
    private String gateway;

    @JsonProperty("IPAddress")
    private String ipAddress;

    @JsonProperty("IPPrefixLen")
    private int ipPrefixLen;

    @JsonProperty("IPv6Gateway")
    private String ipv6GateWay;

    @JsonProperty("GlobalIPv6Address")
    private String globalIpv6Address;

    @JsonProperty("GlobalIPv6PrefixLen")
    private int globalIpv6PrefixLen;

    @JsonProperty("MacAddress")
    private String macAddress;


}