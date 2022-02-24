package com.techelevator.dao;

import com.techelevator.model.Campground;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class JdbcCampgroundDaoTests extends BaseDaoTests {

    private CampgroundDao dao;

    @Before
    public void setup() {
        dao = new JdbcCampgroundDao(dataSource);
    }

    @Test
    public void getCampgrounds_Should_ReturnAllCampgrounds() {
        List<Campground> campgrounds = dao.getCampgroundsByParkId(1);
        assertNotNull("List should not be null", campgrounds);
        assertEquals("List for park 1 should have 2 campgrounds", 2, campgrounds.size());
    }

    @Test
    public void getCampgrounds_InvalidID_NoCampgrounds() {
        List<Campground> campgrounds = dao.getCampgroundsByParkId(99);
        assertNotNull("Park 99 does not exist, list should be empty not null", campgrounds);
        assertEquals("Park 99 does not exist, list should be empty", 0, campgrounds.size());
    }
}
