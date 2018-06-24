package quick.pager.shiro.spring.boot.beans;

import java.io.Serializable;

public class Session implements Serializable{

    private Boolean sessionIdCookieEnabled = Boolean.TRUE;

    private Boolean sessionIdUrlRewritingEnabled = Boolean.TRUE;

    private Boolean deleteInvalidSessions = Boolean.TRUE;

    private Cookie cookie;

    public Boolean getSessionIdCookieEnabled() {
        return sessionIdCookieEnabled;
    }

    public void setSessionIdCookieEnabled(Boolean sessionIdCookieEnabled) {
        this.sessionIdCookieEnabled = sessionIdCookieEnabled;
    }

    public Boolean getSessionIdUrlRewritingEnabled() {
        return sessionIdUrlRewritingEnabled;
    }

    public void setSessionIdUrlRewritingEnabled(Boolean sessionIdUrlRewritingEnabled) {
        this.sessionIdUrlRewritingEnabled = sessionIdUrlRewritingEnabled;
    }

    public Boolean getDeleteInvalidSessions() {
        return deleteInvalidSessions;
    }

    public void setDeleteInvalidSessions(Boolean deleteInvalidSessions) {
        this.deleteInvalidSessions = deleteInvalidSessions;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }
}
