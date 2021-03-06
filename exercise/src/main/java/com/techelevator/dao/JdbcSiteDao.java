package com.techelevator.dao;

import com.techelevator.model.Site;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcSiteDao implements SiteDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcSiteDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Site> getSitesThatAllowRVs(int parkId) {
        List<Site> site = new ArrayList<>();
        String sql = "SELECT *, park_id from site join campground on site.campground_id = campground.campground_id\n" +
                "where park_id = ? AND max_rv_length > 0;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId);
        while (results.next()){
            site.add(mapRowToSite(results));
        }
        return site;
    }

    @Override
    public List<Site> identifyCurrentlyAvailable(int parkId) {
        List<Site> site =new ArrayList<>();
        String sql = "select * FROM site \n" +
                "LEFT JOIN reservation ON site.site_id =reservation.site_id\n" +
                "LEFT JOIN campground on site.campground_id = campground.campground_id\n" +
                "WHERE park_id = ? AND from_date IS NULL\n" +
                "ORDER BY campground.name, from_date;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId);
        while (results.next()){
            site.add(mapRowToSite(results));
        }
        return site;
    }

    private Site mapRowToSite(SqlRowSet results) {
        Site site = new Site();
        site.setSiteId(results.getInt("site_id"));
        site.setCampgroundId(results.getInt("campground_id"));
        site.setSiteNumber(results.getInt("site_number"));
        site.setMaxOccupancy(results.getInt("max_occupancy"));
        site.setAccessible(results.getBoolean("accessible"));
        site.setMaxRvLength(results.getInt("max_rv_length"));
        site.setUtilities(results.getBoolean("utilities"));
        return site;
    }
}
