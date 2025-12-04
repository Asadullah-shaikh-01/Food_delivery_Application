//package com.Online_Food.config;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.apache.coyote.BadRequestException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.crypto.SecretKey;
//import java.io.IOException;
//import java.util.List;
//
//public class JwtTokenValidator extends OncePerRequestFilter {
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        String jwt=request.getHeader(JwtConstant.JWT_HEADER);
//
//        //Bearer token
//        if(jwt!=null){
//            jwt= jwt.substring(7);
//            try {
//                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
//                Claims claims = Jwts.parserBuilder().setSigningKeys(key).build().parseClaimJws(jwt).getBody();
//
//                String email = String.valueOf(claims.get("email"));
//                String authorities = String.valueOf((claims.get("authorities")));
//                //ROLE_CUSTOMER ,ROLE_ADMIN
//
//                List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
//                Authentication authentication = new UsernamePasswordAuthenticationToken(email,null,auth);
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            }catch (Exception e){
//                throw  new BadRequestException("Invalid Token .....");
//            }
//        }
//     filterChain.doFilter(request,response);
//    }
//}



package com.Online_Food.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter  {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        // Check if JWT exists and starts with Bearer
        if (jwt != null && jwt.startsWith("Bearer")) {
            jwt = jwt.substring(7); // remove "Bearer "

            try {
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

                // ✅ Updated parser for JJWT 0.13.0
                Claims claims = Jwts.parser()
                        .setSigningKey(key).build().parseClaimsJws(jwt).getBody();

                // ✅ Extract custom fields from token
                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                // Convert string to GrantedAuthority list
                List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                // ✅ Create Authentication object
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(email, null, auth);

                // ✅ Set authentication in context
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                throw new BadRequestException("Invalid Token ...");
            }
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}

