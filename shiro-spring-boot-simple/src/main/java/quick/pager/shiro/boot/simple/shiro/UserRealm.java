package quick.pager.shiro.boot.simple.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

public class UserRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<String> permission = new ArrayList<>();
        List<String> roles = new ArrayList<>();
        String username = (String) principals.getPrimaryPrincipal();
        if ("admin".equals(username)) {
            roles.add("admin");
            permission.add("admin:other");
            permission.add("admin:success");
        } else {
            roles.add("sp");
            permission.add("admin:role:success");
            permission.add("admin:role:sp");
        }

        info.addRoles(roles);
        info.addStringPermissions(permission);

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String password = new String((char[]) token.getCredentials());
        String username = (String) token.getPrincipal();

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, password, this.getName());

        return info;
    }
}
