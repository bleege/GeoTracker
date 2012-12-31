/**
 * @author Brad Leege <bleege@gmail.com>
 * Created on 12/31/12 at 3:44 PM
 */

package com.bradleege.geotracker.testing;

import com.bradleege.geotracker.data.Track;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-beans.xml" })
public class RESTClientTest
{
    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @SuppressWarnings("unchecked")
    @Test
    public void restJsonClientTest() {

        List<Track> tracks = (ArrayList<Track>) restTemplate.getForObject("http://localhost:8080/geotracker/rest/track/get.json", List.class);
        Assert.assertNotNull(tracks);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void restXmlClientTest() {

        List<Track> tracks = (ArrayList<Track>) restTemplate.getForObject("http://localhost:8080/geotracker/rest/track/get.xml", List.class);

        Assert.assertNotNull(tracks);
    }
}
