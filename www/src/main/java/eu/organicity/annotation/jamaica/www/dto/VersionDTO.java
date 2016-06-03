package eu.organicity.annotation.jamaica.www.dto;


public class VersionDTO {

    private String version;

    public VersionDTO() {
    }

    public VersionDTO(final String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }
}
