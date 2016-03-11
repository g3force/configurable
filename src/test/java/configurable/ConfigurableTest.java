/*
 * *********************************************************
 * Copyright (c) 2009 - 2016, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: Mar 10, 2016
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * *********************************************************
 */
package configurable;

import org.apache.log4j.Logger;

import com.github.g3force.configurable.ConfigRegistration;
import com.github.g3force.configurable.Configurable;
import com.github.g3force.configurable.IConfigClient;
import com.github.g3force.configurable.IConfigObserver;


/**
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 */
public class ConfigurableTest
{
	@SuppressWarnings("unused")
	private static final Logger	log	= Logger.getLogger(ConfigurableTest.class.getName());
	
	@Configurable(comment = "Document this field", defValue = "false")
	private static boolean			testBool;
	
	
	static
	{
		ConfigRegistration.registerClass("CATEGORY", ConfigurableTest.class);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(final String[] args)
	{
		ConfigRegistration.registerConfigurableCallback("CATEGORY", new IConfigObserver()
		{
			@Override
			public void afterApply(final IConfigClient configClient)
			{
				System.out.println("Call Back");
			}
		});
		testBool = true;
		ConfigRegistration.readClasses("CATEGORY");
		ConfigRegistration.applyConfig("CATEGORY");
		ConfigRegistration.save("CATEGORY");
	}
}
