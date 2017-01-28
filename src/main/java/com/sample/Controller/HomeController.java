package com.sample.Controller;

import Models.NumberOfSeats;
import Models.Route;
import Models.UserDetails;
import Services.RouteService;
import Validators.SearchRoutesValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

    @RequestMapping(value = "/Home", method = RequestMethod.POST)
    public String successLogin(@ModelAttribute("route") Route route, Model model, HttpServletRequest request, @RequestParam("selectedDate") String date, RedirectAttributes redirectAttribute) {
        SearchRoutesValidator searchRoutesValidator = new SearchRoutesValidator();
        String error = searchRoutesValidator.validateAllFields(route, date);
        redirectAttribute.addAttribute("error", error);
        if (error != null) {

            return "redirect:/searchRoutes";
        }

        HttpSession httpSession = request.getSession();
        UserDetails userDetails = (UserDetails) httpSession.getAttribute("userDetails");
        Cookie[] cookie = request.getCookies();
        route = mapDate(date, route);


        RouteService routeService = new RouteService();
        List<Route> routeList = routeService.getRouteList(route);
        model.addAttribute("selectedRoute", route);
        model.addAttribute("userName", cookie[2].getValue());
        model.addAttribute("routesList", routeList);
        model.addAttribute("numberOfSeats", new NumberOfSeats());
        return "home";
    }

    @RequestMapping(value = "/searchRoutes", method = RequestMethod.GET)
    public String searchRoutes(Model model, HttpServletRequest request, @ModelAttribute("error") String error) {
        Route route = new Route();
        Cookie[] cookie = request.getCookies();
        model.addAttribute("error", error);
        model.addAttribute("route", route);
        model.addAttribute("userName", cookie[2].getValue());

        return "searchRoutes";

    }

    public Route mapDate(String date, Route route) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date selectedDate = null;
        try {
            selectedDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        route.setSelectedDate(formatter.format(selectedDate));
        route.setDate(selectedDate);
        return route;
    }
}
