package com.techelevator.dao;

import com.techelevator.model.Reservation;
import com.techelevator.model.Site;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcReservationDao implements ReservationDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Reservation createReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate) {
        Reservation reservations = new Reservation();
        String sql = "INSERT into reservation (site_id, name, from_date, to_date)" +
                " VALUES (?,?,?,?) RETURNING reservation_id;";
        //SqlRowSet results = jdbcTemplate.queryForRowSet(sql, siteId, name, fromDate, toDate);
        //reservations = mapRowToReservation(results);
        //return reservations;
        int newId = jdbcTemplate.queryForObject(sql, int.class, siteId, name, fromDate, toDate);

        return getReservation(newId);
    }

    private Reservation getReservation(int reservationId){
        Reservation reservation = null;
        String sql = "SELECT * FROM reservation WHERE reservation_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, reservationId);
        if (results.next()){
            reservation = mapRowToReservation(results);
        }
        return reservation;
    }

    private Reservation mapRowToReservation(SqlRowSet results) {
        Reservation r = new Reservation();
        r.setReservationId(results.getInt("reservation_id"));
        r.setSiteId(results.getInt("site_id"));
        r.setName(results.getString("name"));
        r.setFromDate(results.getDate("from_date").toLocalDate());
        r.setToDate(results.getDate("to_date").toLocalDate());
        r.setCreateDate(results.getDate("create_date").toLocalDate());
        return r;
    }


}
