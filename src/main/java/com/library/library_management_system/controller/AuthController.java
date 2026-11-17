package com.library.library_management_system.controller;

import org.springframework.web.bind.annotation.RestController;

import com.library.library_management_system.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

// Frontend sends the Admin ID and password.
// Backend checks the Admin credentials.
// If correct, a session is created and the ADMIN role is saved.
    @PostMapping("/auth/admin-login")

// HttpServletRequest – gives access to the incoming request and its session.
// HttpServletResponse – represents the response sent back to the browser.
    public Map<String, String> adminLogin(@RequestBody Map<String,String> loginData, HttpServletRequest request, HttpServletResponse response) {
        String adminId = loginData.get("adminId");
        String password = loginData.get("password");

    // Verify credentials
        boolean authenticated = authService.authenticateAdmin(adminId, password);

        if(!authenticated){
            return Map.of("message", "Invalid Admin Id or Password");
        }

// UsernamePasswordAuthenticationToken : Spring Security class which represents authentication information about a user (Identity, Credential or Granted Authority).
        // Create Spring Security Authentication
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(adminId, null,
                                                // SimpleGrantedAuthority represents the user's permission or role.
                                                                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        
// SecurityContext as a container that holds information about the currently authenticated user.
        // Create Security Context
        // Empty Container
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

// the container knows that the authenticated user is the Admin and has the ROLE_ADMIN authority.
        securityContext.setAuthentication(authentication);

// it makes authentication information available to Spring Security during the current request.
        SecurityContextHolder.setContext(securityContext);

        // create Session
        HttpSession session = request.getSession(true);
        
// HttpSessionSecurityContextRepository : Spring Security class responsible for storing and retrieving the SecurityContext using an HTTP session.
// SPRING_SECURITY_CONTEXT_KEY : predefined constant containing the session attribute name that Spring Security expects.
        // Save Security Context
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,securityContext);

        return Map.of("message", "Admin Login Successfully");
    }



    @PostMapping("/auth/librarian-login")
    public Map<String, String> librarianLogin(@RequestBody Map<String, String> loginData, HttpSession session) {
        String librarianId = loginData.get("librarianId");
        String password = loginData.get("password");

        boolean authenticated = authService.authenticateLibrarian(librarianId, password);
        if(!authenticated){
            return Map.of("message", "Invalid Librarian Id or Password");
        }

        session.setAttribute("loggedInUser", librarianId);
        session.setAttribute("role", "LIBRARIAN");
        
        return Map.of("message", "Librarian Login Succesfully");
    }

    @PostMapping("/auth/logout")
    public Map<String, String> logout(HttpSession session) {

// destroys the current login session, so the user is logged out.
        session.invalidate();
        return Map.of("message", "Logout Successful");
    }
    
}
