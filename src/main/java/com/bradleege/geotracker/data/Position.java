/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 12/22/2012 at 2:49 PM
 */

package com.bradleege.geotracker.data;

import java.io.Serializable;

public class Position implements Serializable
{
	private Double latitude;
	private Double longitude;
	private Double elevation;

	/**
	 * Constructor
	 */
	public Position()
	{
		super();
	}

	public Double getElevation()
	{
		return elevation;
	}

	public void setElevation(Double elevation)
	{
		this.elevation = elevation;
	}

	public Double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(Double latitude)
	{
		this.latitude = latitude;
	}

	public Double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(Double longitude)
	{
		this.longitude = longitude;
	}
}
