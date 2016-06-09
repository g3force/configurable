/*
 * *********************************************************
 * Copyright (c) 2009 - 2016, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: Mar 14, 2016
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * *********************************************************
 */
package configurable;

import com.github.g3force.configurable.ConfigRegistration;
import com.github.g3force.configurable.Configurable;


/**
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 */
public class ConfigClass2
{
	@Configurable(comment = "Document this field", spezis = { "", "CONF1", "CONF2" }, defValueSpezis = { "1", "2", "3" })
	static double testSpezi;
	
	
	static
	{
		ConfigRegistration.registerClass("default", ConfigClass2.class);
	}
	
}