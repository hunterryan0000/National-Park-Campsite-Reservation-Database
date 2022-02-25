package com.techelevator.dao;

import com.techelevator.model.Reservation;
import com.techelevator.model.Site;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class JdbcReservationDaoTests extends BaseDaoTests {

    private static final Reservation RESERVATION_1 = new Reservation(1, 1, "Test Testerson",
            LocalDate.now().plusDays(1L), LocalDate.now().plusDays(5L), LocalDate.now().plusDays(-23L));

    private static final Reservation RESERVATION_2 = new Reservation(2, 1, "Bob Robertson",
            LocalDate.now().plusDays(11L), LocalDate.now().plusDays(18L), LocalDate.now().plusDays(-23L));

    private ReservationDao dao;

    @Before
    public void setup() {
        dao = new JdbcReservationDao(dataSource);
    }

    @Test
    public void createReservation_Should_ReturnNewReservation() {
        Reservation reservation = new Reservation();
        reservation.setSiteId(1);
        reservation.setName("TEST NAME");
        reservation.setFromDate(LocalDate.parse("2030-01-02"));
        reservation.setToDate(LocalDate.parse("2030-01-08"));
        Reservation created = dao.createReservation(reservation.getSiteId(), reservation.getName(),
                reservation.getFromDate(), reservation.getToDate());

        // Reservation should not be null
        assertNotNull("Created reservation should not be null", created);

        // Make sure id comes back greater than 0
        assertTrue("Created reservation id should be greater than 0", created.getReservationId() > 0);

        // Update the id & created date on the reservation to check other values
        reservation.setReservationId(created.getReservationId());
        reservation.setCreateDate(created.getCreateDate());
        assertReservationsMatch(reservation, created);
    }

    @Test
    public void upcomingReservations_should_return_ListReservations(){
        List<Reservation> reservations = dao.upcomingReservations(1);
        assertEquals("The test data returns two reservations for test parkId 1", 2,reservations.size());
        assertReservationsMatch(RESERVATION_1, reservations.get(0));
        assertReservationsMatch(RESERVATION_2, reservations.get(1));


    }

    private void assertReservationsMatch(Reservation r1, Reservation r2) {
        assertEquals("Reservation ids should match", r1.getReservationId(), r2.getReservationId());
        assertEquals("Reservation site ids should match", r1.getSiteId(), r2.getSiteId());
        assertEquals("Reservation names should match", r1.getName(), r2.getName());
        assertEquals("Reservation from dates should match", r1.getFromDate(), r2.getFromDate());
        assertEquals("Reservation to dates should match", r1.getToDate(), r2.getToDate());
        assertEquals("Reservation create dates should match", r1.getCreateDate(), r2.getCreateDate());
    }

}
