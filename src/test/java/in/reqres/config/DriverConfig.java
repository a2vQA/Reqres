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
    @DefaultValue("https://user1:1234@selenoid.autotests.cloud/wd/hub")
    String browserRemoteUrl();
}
