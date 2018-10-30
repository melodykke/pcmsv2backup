package com.zhsl.pcmsv2.sso.oauth;

import com.zhsl.pcmsv2.properties.SSOProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SSOAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    public static final String SSO_SECURITY_ACCESS_TOKEN_KEY = "accessToken";

    private String accessTokenParameter = SSO_SECURITY_ACCESS_TOKEN_KEY;

    private boolean getOnly = true;

    @Autowired
    private ValidateIdentityProcessor validateIdentityProcessor;

    @Autowired
    private SSOProperties sSOProperties;


    // ~ Constructors
    // ===================================================================================================

    public SSOAuthenticationFilter() {
        super(new AntPathRequestMatcher("/sSOLogin", "GET"));
    }

// ~ Methods
    // ========================================================================================================

    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (getOnly && !request.getMethod().equals("GET")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String accessToken = obtainAccessToken(request);

        if (accessToken == null) {
            accessToken = "";
        }
        accessToken = accessToken.trim();

        // 拿到accessToken获取用户身份信息
        // String username = validateIdentityProcessor.validate(sSOProperties.getsSOAuthenticationUrl(), accessToken);

        String username = "stjgc";

        UsernameAuthenticationToken authRequest = new UsernameAuthenticationToken(username);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainAccessToken(HttpServletRequest request) {
        return request.getParameter(accessTokenParameter);
    }

    protected void setDetails(HttpServletRequest request,
                              UsernameAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    /*
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (ifIntercept(request) && StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
            logger.info("拦截SSO单点登录的身份验证路径：(" + request.getRequestURI());

            String accessToken = request.getParameter("access_token");

            try {
                if (accessToken == null || "".equals(accessToken)) {
                    log.error("SSO单点登录参数错误， accessToken为空！");
                    throw new SSOAuthenticationException("SSO认证失败！");
                }
                String username = validateIdentityProcessor.validate(sSOProperties.getsSOAuthenticationUrl(), accessToken);
                logger.info("SSO校验通过! 登录中...");

            } catch (SSOAuthenticationException exception) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }*/

}
