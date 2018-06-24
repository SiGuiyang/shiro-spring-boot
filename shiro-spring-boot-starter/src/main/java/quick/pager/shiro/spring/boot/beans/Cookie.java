package quick.pager.shiro.spring.boot.beans;

import java.io.Serializable;

/**
 * Attributes encoded in the header's cookie fields.
 */
public class Cookie implements Serializable {

    private String name = "rememberMe";

    private String value;
    /**
     * Version=1 ... means RFC 2109 style
     */
    private int version = 0;

    /**
     * Comment=VALUE ... describes cookie's use
     */
    private String comment;
    /**
     * Domain=VALUE ... domain that sees cookie
     */
    private String domain = null;
    /**
     * Max-Age=VALUE ... cookies auto-expire
     */
    private int maxAge = -1;
    /**
     * Path=VALUE ... URLs that see the cookie
     */
    private String path = null;
    /**
     * Secure ... e.g. use SSL
     */
    private boolean secure = false;
    /**
     * Not in cookie specs, but supported by browsers
     */
    private boolean httpOnly = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }
}
