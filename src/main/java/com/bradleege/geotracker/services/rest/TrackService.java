/**
 * @author Brad Leege <bleege@gmail.com>
 * Created on 12/22/2012 at 4:13 PM
 */

package com.bradleege.geotracker.services.rest;

import java.util.List;
import com.bradleege.geotracker.data.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/track")
public class TrackService
{
	@Autowired
	@Qualifier("mongoTemplate")
	public MongoTemplate mongoTemplate;

	@RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Track> getAll() throws Exception
	{
		return mongoTemplate.findAll(Track.class);
	}
}
