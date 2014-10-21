package com.claro.cfcmobile.robospice.response;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 7/25/2014.
 */
public class VersionResponse {

    private String version;

    private String updateLink;

    public VersionResponse(){
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUpdateLink() {
        return updateLink;
    }

    public void setUpdateLink(String updateLink) {
        this.updateLink = updateLink;
    }

    @Override
    public String toString() {
        return "VersionResponse{" +
                "version='" + version + '\'' +
                ", updateLink='" + updateLink + '\'' +
                '}';
    }
}
