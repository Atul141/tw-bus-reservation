package ServiceTest;


import Dao.RouteDao;
import ServiceImpl.ConfigDB;
import ServiceImpl.SaveImpl;
import ServiceImpl.SyntaxSugar;
import ServiceImplTest.ConfigTest;
import Models.Route;
import ServiceImpl.RoutesImpl;
import Services.RouteService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RouteServiceTest {

    private ConfigTest configTest;
    private RouteService routeService;
    private RoutesImpl routesImpl;
    private ConfigDB configDB;


    @Before
    public void setup() {
        initMocks(this);
        configDB = new ConfigDB();
        configDB.setEnvironment(SyntaxSugar.TEST_ENV);
        configTest = new ConfigTest();
        routesImpl = mock(RoutesImpl.class);
        routeService = new RouteService(configDB);
        SaveImpl save = new SaveImpl(configDB);
        RouteDao route = configTest.getRouteDaoDetails();
        save.saveToDb(route);
    }

    @Test
    public void shouldGetRoutesBaseOnId() {
        assertEquals(1, routeService.getRouteBasedOnId(1).getId());

    }

    @Test
    public void shouldGetRoutesList() {
        initMocks(this);
        Route route = new Route();
        route.setId(1);
        route.setSource("BANGALORE");
        route.setDestination("MYSORE");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse("2017-1-18");
            route.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<RouteDao> routeList = new ArrayList<RouteDao>();
        when(routesImpl.getRoutes(any(Route.class))).thenReturn(routeList);
        assertEquals(routeList, routeService.getRouteList(route));
    }
}
