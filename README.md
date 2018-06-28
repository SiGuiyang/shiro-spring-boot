# shiro-spring-boot
## 引入项目<code>maven</code>依赖
     <dependency>
		    <groupId>quick.pager</groupId>
			   <artifactId>shiro-spring-boot-starter</artifactId>
			   <version>1.0.0</version>
		</dependency>

## 使用<code>shiro-spring-boot</code>方法
1. 在spring boot启动类中添加@EnableShiroConfiguration 注解，启用shiro权限框架
    例如：
    
        @SpringBootApplication
        @EnableShiroConfiguration
        public class ShiroSimpleApplication {
        
            public static void main(String[] args) {
                SpringApplication.run(ShiroSimpleApplication.class, args);
            }
        
        }
        
2. 编写权限控制xxxRealm extends AuthorizingRealm 类，用于shiro的登陆于权限管理
    例如：
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
          
3. 在spring boot 资源文件application.yml 添加shiro相关配置信息，shiro-spring-boot 的资源前缀时 spring.shiro
    例如：
        
        spring:
          shiro:
            realm-class: quick.pager.shiro.boot.simple.shiro.UserRealm
            login-url: '/admin/login'
            success-url: '/admin/success'
            unauthorized-url: '/admin/404'
            filter-chain-definition-map: {'[/admin/login/page]':'anon','[/admin/login]':'anon','[/admin/logout]':'anon','[/admin/role/**]':'authc,roles[sp]','[/admin/**]':'authc,roles[admin]'}
            proxy-target-class: true

    

### 具体可以参考<code>shiro-spring-simple</code>例子，该例子以结合各种权限的配置设置，可以在aop SubjectAspect 类查看是否具有的权限与路由
