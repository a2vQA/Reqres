package in.reqres.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:config/${driver}.properties"
})
public interface DriverConfig extends Config {
    @Key("is.remote")
    @DefaultValue("false")
    Boolean isRemote();

    @Key("browser.remote.url")
    String browserRemoteUrl();
}
