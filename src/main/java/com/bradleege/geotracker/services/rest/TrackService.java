/**
 * @author Brad Leege <bleege@gmail.com>
 * Created on 12/22/2012 at 4:13 PM
 */

package com.bradleege.geotracker.services.rest;

import java.util.List;
import com.bradleege.geotracker.data.Track;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.Box;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/track")
public class TrackService
{
    private static final Logger logger = LoggerFactory.getLogger(TrackService.class);

    public static final Double KILOMETER = 111.0d;

    /**
     * The Attribute that is used for the search for the start position
     */
    public static final String START = "start";
    /**
     * The Attribute that is used for the search for the user
     */
    private static final String USER = "user";

    @Autowired
    @Qualifier("mongoTemplate")
    public MongoTemplate mongoTemplate;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody List<Track> getAll() throws Exception
    {
        logger.info("/track/get called...");
        return mongoTemplate.findAll(Track.class);
    }


    @RequestMapping(value = "/get/{lon1}/{lat1}/{lon2}/{lat2}/", method = RequestMethod.GET)
    public @ResponseBody List<Track> getByBounds(@PathVariable("lon1") Double lon1,
                                                 @PathVariable("lat1") Double lat1,
                                                 @PathVariable("lon2") Double lon2,
                                                 @PathVariable("lat2") Double lat2) throws Exception
    {
        /**
         > box = [[40.73083, -73.99756], [40.741404,  -73.988135]]
         > db.places.find({"loc" : {"$within" : {"$box" : box}}})
         **/
        Criteria criteria = new Criteria(START).within(new Box(new Point(lon1, lat1), new Point(lon2, lat2)));
        List<Track> tracks = mongoTemplate.find(new Query(criteria), Track.class);
        return tracks;
    }

    @RequestMapping(value = "/get/{lon}/{lat}/{maxdistance}", method = RequestMethod.GET)
    public @ResponseBody List<Track> getByLocation(@PathVariable("lon") Double lon,
                                                   @PathVariable("lat") Double lat,
                                                   @PathVariable("maxdistance") Double maxdistance) throws Exception
    {
        Criteria criteria = new Criteria(START).near(new Point(lon, lat)).maxDistance(getInKilometer(maxdistance));
        List<Track> tracks = mongoTemplate.find(new Query(criteria), Track.class);
        return tracks;
    }

    /**
     * The current implementation of near assumes an idealized model of a flat
     * earth, meaning that an arcdegree
     * of latitude (y) and longitude (x) represent the same distance everywhere.
     * This is only true at the equator where they are both about equal to 69 miles
     * or 111km. Therefore you must divide the
     * distance you want by 111 for kilometer and 69 for miles.
     *
     * @param maxdistance The distance around a point.
     * @return The calcuated distance in kilometer.
     */
    private Double getInKilometer(Double maxdistance)
    {
        return maxdistance / KILOMETER;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void add(@RequestBody Track track) throws Exception
    {
        mongoTemplate.insert(track);
    }

    @RequestMapping(value = "/foruser", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Track> tracksForUser(@RequestParam("user") String user) throws Exception
    {
        Criteria criteria = Criteria.where(USER).is(user);
        List<Track> tracks = mongoTemplate.find(new Query(criteria), Track.class);
        return tracks;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void upload(@RequestParam("file") MultipartFile multipartFile) throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        Track track = mapper.readValue(multipartFile.getBytes(), Track.class);
        mongoTemplate.insert(track);
    }
}
