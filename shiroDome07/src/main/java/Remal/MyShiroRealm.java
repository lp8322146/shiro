package Remal;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义的指定Shiro验证用户登录的类
 *
 * @author
 */
public class MyShiroRealm extends AuthorizingRealm {
    //没有连接数据库，默认用户
    private static final String user_name = "jack";
    private static final String user_pwd = "123456";

    /**
     * 为当前登录的Subject授予角色和权限
     * 经测试:本例中该方法的调用时机为需授权资源被访问时
     * 经测试:并且每次访问需授权资源时都会执行该方法中的逻辑,这表明本例中默认并未启用AuthorizationCache
     * 个人感觉若使用了Spring3.1开始提供的ConcurrentMapCache支持,则可灵活决定是否启用AuthorizationCache
     * 比如说这里从数据库获取权限信息时,先去访问Spring3.1提供的缓存,而不使用Shior提供的AuthorizationCache
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("进入doGetAuthorizationInfo");
        //获取当前登录的用户名,等价于(String)principals.fromRealm(this.getName()).iterator().next()
        String currentUserName = (String) super.getAvailablePrincipal(principalCollection);
        //      List<String> roleList = new ArrayList<String>();
//      List<String> permissionList = new ArrayList<String>();
//      //从数据库中获取当前登录用户的详细信息
//      User user = userService.getByUsername(currentUsername);
//      if(null != user){
//          //实体类User中包含有用户角色的实体类信息
//          if(null!=user.getRoles() && user.getRoles().size()>0){
//              //获取当前登录用户的角色
//              for(Role role : user.getRoles()){
//                  roleList.add(role.getName());
//                  //实体类Role中包含有角色权限的实体类信息
//                  if(null!=role.getPermissions() && role.getPermissions().size()>0){
//                      //获取权限
//                      for(Permission pmss : role.getPermissions()){
//                          if(!StringUtils.isEmpty(pmss.getPermission())){
//                              permissionList.add(pmss.getPermission());
//                          }
//                      }
//                  }
//              }
//          }
//      }else{
//          throw new AuthorizationException();
//      }
//      //为当前用户设置角色和权限
//      SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();
//      simpleAuthorInfo.addRoles(roleList);
//      simpleAuthorInfo.addStringPermissions(permissionList);
        SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();
        System.out.println("开始进行授权。");
        System.out.println("输出账号名，录入的账号名为，"+currentUserName);
        System.out.println("user_name用户名为"+user_name);
        if(null!=currentUserName && user_name.equals(currentUserName)){
            //添加一个角色,不是配置意义上的添加,而是证明该用户拥有admin角色
            simpleAuthorInfo.addRole("admin");
            //添加权限
            simpleAuthorInfo.addStringPermission("admin:manage");
            System.out.println("已为用户[papio]赋予了[admin]角色和[admin:manage]权限");
            return simpleAuthorInfo;
        }
        /*//获取角色列表
        Set<String> roleNames = new HashSet<String>();
        //获取权限列表
        Set<String> permissions = new HashSet<String>();
        //添加角色
        roleNames.add("administrator");
        //添加权限
        permissions.add("index");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
        info.setStringPermissions(permissions);*/
        return null;
    }

    /**
     * 验证当前登录的Subject
     *  经测试:本例中该方法的调用时机为LoginController.login()方法中执行Subject.login()时
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取基于用户名和密码的令牌
        //实际上这个authcToken是从LoginController里面currentUser.login(token)传过来的

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        System.out.println("开始验证输出录入的账号："+token.getUsername()+"密码："+token.getPassword());
        System.out.println("账号:"+user_name+"密码："+user_pwd);
        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
//此    //处无需比对,比对的逻辑Shiro会做,我们只需返回一个和令牌相关的正确的验证信息
        //说白了就是第一个参数填登录用户名,第二个参数填合法的登录密码(可以是从数据库中取到的,本例中为了演示就硬编码了)
        //这样一来,在随后的登录页面上就只有这里指定的用户和密码才能通过验证
        if(token.getUsername().equals(user_name)){
            System.out.println("令牌开始验证是否正确");
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(user_name,user_pwd,this.getName());
            return authcInfo;
        }
        return null;
    }
}
