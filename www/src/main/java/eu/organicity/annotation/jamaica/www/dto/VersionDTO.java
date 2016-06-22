package eu.organicity.annotation.jamaica.www.dto;


import java.io.Serializable;

public class VersionDTO implements Serializable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VersionDTO that = (VersionDTO) o;

        return version.equals(that.version);

    }

    @Override
    public int hashCode() {
        return version.hashCode();
    }
}
