package in.reqres.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:config/api.properties"
})
public interface ApiConfig extends Config {
    @Key("reqres.baseUrl")
    String reqresBaseUrl();

    @Key("reqres.basePath")
    String reqresBasePath();
}
