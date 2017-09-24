package Remal;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    @RequestMapping("/index")
    public String getIndex() {
        System.out.println("请求index页面");
        return "index";
    }

    @RequestMapping("/error")
    public String errorPage() {
        System.out.println("请求error页面");
        return "error";
    }

    @RequestMapping("/login")
    public String login() {
        System.out.println("请求login页面");
        return "login";
    }
    @RequiresPermissions("authc")
    @RequestMapping("/check")
    public String loginCheck(@RequestParam("name") String name, @RequestParam("pwd") String pwd, HttpServletRequest request) {
        System.out.println("登录请求" + name + pwd);
        String userName = name;
        String userPwd = pwd;
        UsernamePasswordToken token = new UsernamePasswordToken(name, pwd);
        token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();
        subject.hasRole("admin");
        boolean loginOk = false;
        try {
            System.out.println("对用户[" + name + "]进行登录验证..验证开始");
            //验证角色和用户
            subject.login(token);
            loginOk = true;
        } catch (UnknownAccountException uae) {
            System.out.println("对用户[" + name + "]进行登录验证..验证未通过,未知账户");
            request.setAttribute("message_login", "未知账户");

        } catch (IncorrectCredentialsException ice) {
            System.out.println("对用户[" + name + "]进行登录验证..验证未通过,错误的凭证");
            request.setAttribute("message_login", "密码不正确");
        } catch (LockedAccountException lae) {
            System.out.println("对用户[" + name + "]进行登录验证..验证未通过,账户已锁定");
            request.setAttribute("message_login", "账户已锁定");
        } catch (ExcessiveAttemptsException eae) {
            System.out.println("对用户[" + name + "]进行登录验证..验证未通过,错误次数过多");
            request.setAttribute("message_login", "用户名或密码错误次数过多");
        } catch (AuthenticationException ae) {
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            System.out.println("对用户[" + name + "]进行登录验证..验证未通过,堆栈轨迹如下");
            ae.printStackTrace();
            request.setAttribute("message_login", "用户名或密码不正确");
        }
        if (subject.isAuthenticated()) {
            System.out.println("用户[" + name + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
        } else {
            token.clear();
        }
        return loginOk?"success":"login";
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout.json", method = RequestMethod.POST)
    @ResponseBody
    public String logout() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return "";
    }
}
