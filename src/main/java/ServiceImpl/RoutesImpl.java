package ServiceImpl;


import Dao.RouteDao;
import Models.Route;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoutesImpl {

    private ConfigDB configDB;

    public RoutesImpl(ConfigDB configDB) {
        this.configDB = configDB;
    }

    public List<RouteDao> getRoutes(Route routes) {
        String query = "FROM RouteDao route where route.source=" + "'" + routes.getSource() + "'" + "and route.destination=" + "'" + routes.getDestination() + "'" + "and route.date=" + "'" + routes.getDate() + "'";
        return getRouteList(query);
    }


    public RouteDao getRoutesBasedOnId(long id) {
        Session session = configDB.getSession();
        Transaction transaction = session.beginTransaction();
        String query = "FROM RouteDao route where route.id=" + "'" + id + "'";
        RouteDao routeDao = new RouteDao();
        try {
            routeDao = (RouteDao) session.createQuery(query).uniqueResult();
            session.lock(routeDao, LockMode.READ);
            transaction.commit();
            session.close();
        } catch (Throwable ex) {
            System.out.println("error creating session " + ex);
        }
        return routeDao;
    }

    public void updateRoute(RouteDao routeDao) {
        UpdateImpl update = new UpdateImpl(configDB);
        update.UpdateDb(routeDao);

    }

    public List<RouteDao> getrouteListBasedOnDate(Date date) {
        String query = "FROM RouteDao route where  route.date=" + "'" + date + "'";
        return getRouteList(query);
    }

    private List<RouteDao> getRouteList(String query) {
        Session session = configDB.getSession();
        Transaction transaction = session.beginTransaction();
        List<RouteDao> routeDaoList = new ArrayList<RouteDao>();
        try {
            routeDaoList = (List<RouteDao>) session.createQuery(query).list();
            session.lock(new RouteDao(), LockMode.READ);
            transaction.commit();
            session.close();
        } catch (Throwable ex) {
            System.out.println("error creating session " + ex);
        }
        return routeDaoList;
    }

}
