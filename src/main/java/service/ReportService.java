package service;

import dao.EventDAO;
import dao.RegistrationDAO;
import Model.Event;
import java.util.*;

public class ReportService {

    private EventDAO eventDAO;
    private RegistrationDAO registrationDAO;

    public ReportService() {
        this.eventDAO = new EventDAO();
        this.registrationDAO = new RegistrationDAO();
    }

    public List<Object[]> getMostPopularCategories() throws Exception {
        List<Event> events = eventDAO.getAllEvents();
        Map<String, Long> categoriesCount = new HashMap<>();

        for (Event event : events) {
            String category = event.getCategory();
            categoriesCount.put(category, categoriesCount.getOrDefault(category, 0L) + 1);
        }

        long totalEvents = events.size();
        List<Object[]> result = new ArrayList<>();

        for (Map.Entry<String, Long> entry : categoriesCount.entrySet()) {
            String category = entry.getKey();
            long count = entry.getValue();
            double percentage = totalEvents > 0 ? (count * 100.0) / totalEvents : 0;

            result.add(new Object[]{category, count, percentage});
        }

        result.sort((a, b) -> Long.compare((Long) b[1], (Long) a[1]));

        return result;
    }

    public List<Object[]> getCapacityUtilization() throws Exception {
        List<Event> events = eventDAO.getAllEvents();
        List<Object[]> result = new ArrayList<>();

        for (Event event : events) {
            int totalCapacity = event.getCapacity();
            int availableSeats = event.getSeatsAvailable();
            int usedSeats = totalCapacity - availableSeats;

            double utilizationRate = 0.0;
            if (totalCapacity > 0) {
                utilizationRate = (usedSeats * 100.0) / totalCapacity;
            }

            result.add(new Object[]{event.getTitle(), utilizationRate});
        }

        result.sort((a, b) -> Double.compare((Double) b[1], (Double) a[1]));

        return result;
    }

    public List<Object[]> getMostRegisteredEvents() throws Exception {
        List<Event> events = eventDAO.getAllEvents();
        List<Object[]> result = new ArrayList<>();

        for (Event event : events) {
            int registrationCount = registrationDAO.countRegistrations(event.getEventId());
            int capacity = event.getCapacity();

            double registrationRate = 0.0;
            if (capacity > 0) {
                registrationRate = (registrationCount * 100.0) / capacity;
            }

            result.add(new Object[]{event.getTitle(), registrationCount, registrationRate});
        }

        result.sort((a, b) -> Double.compare((Double) b[2], (Double) a[2]));

        return result;
    }
}
