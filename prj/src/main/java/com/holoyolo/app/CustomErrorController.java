package com.holoyolo.app;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {
	
	@GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        // error로 들어온 에러의 status를 불러온다 (ex:404,500,403...)
        
        if(status != null){
            int statusCode = Integer.valueOf(status.toString());
            
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
            	model.addAttribute("code", statusCode);
                return "/error/404";
            } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
            	model.addAttribute("code", statusCode);
            	return "/error/403";
        	} else {
            	model.addAttribute("code", statusCode);
                return "/error/error";
            }
        }

        return "/error/error";
    }

}
