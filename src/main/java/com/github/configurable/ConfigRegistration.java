/*
 * *********************************************************
 * Copyright (c) 2009 - 2014, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: Aug 25, 2014
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * *********************************************************
 */
package com.github.configurable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * Central registration for all configs
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 */
public class ConfigRegistration
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	private static final Logger					log		= Logger.getLogger(ConfigRegistration.class.getName());
	private final Map<String, ConfigClient>	configs	= new LinkedHashMap<String, ConfigClient>();
	private static final ConfigRegistration	INSTANCE	= new ConfigRegistration();
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	  * 
	  */
	private ConfigRegistration()
	{
	}
	
	
	/**
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 * @param client
	 */
	public static void registerConfigClient(final ConfigClient client)
	{
		INSTANCE.configs.put(client.getName().toLowerCase(Locale.ENGLISH), client);
	}
	
	
	/**
	 * Register a callback to a config category to get informed by changes
	 * 
	 * @param cat
	 * @param callback
	 */
	public static void registerConfigurableCallback(final Enum<?> cat, final IConfigObserver callback)
	{
		ConfigClient cc = INSTANCE.configs.get(cat.name().toLowerCase(Locale.ENGLISH));
		if (cc == null)
		{
			log.error("Categorie " + cat + " not found");
			return;
		}
		cc.addObserver(callback);
	}
	
	
	/**
	 * Unregister previously registered callbacks
	 * 
	 * @param cat
	 * @param callback
	 */
	public static void unregisterConfigurableCallback(final Enum<?> cat, final IConfigObserver callback)
	{
		ConfigClient cc = INSTANCE.configs.get(cat.name().toLowerCase(Locale.ENGLISH));
		if (cc == null)
		{
			log.error("Categorie " + cat + " not found");
			return;
		}
		cc.removeObserver(callback);
	}
	
	
	/**
	 * Apply the spezi to the object in category
	 * 
	 * @param obj
	 * @param cat
	 * @param spezi
	 */
	public static void applySpezis(final Object obj, final Enum<?> cat, final String spezi)
	{
		ConfigClient cc = INSTANCE.configs.get(cat.name().toLowerCase(Locale.ENGLISH));
		if (cc == null)
		{
			log.error("Categorie " + cat + " not found");
			return;
		}
		// first apply default
		cc.applyConfigToObject(obj, "");
		// then the spezi
		cc.applyConfigToObject(obj, spezi);
	}
	
	
	/**
	 * Apply the spezi to all classes
	 * 
	 * @param cat
	 * @param spezi
	 */
	public static void applySpezis(final Enum<?> cat, final String spezi)
	{
		ConfigClient cc = INSTANCE.configs.get(cat.name().toLowerCase(Locale.ENGLISH));
		if (cc == null)
		{
			log.error("Categorie " + cat + " not found");
			return;
		}
		cc.applySpezi(spezi);
	}
	
	
	/**
	 * Apply the spezi to all classes and all categories
	 * 
	 * @param spezi
	 */
	public static void applySpezis(final String spezi)
	{
		for (ConfigClient cc : INSTANCE.configs.values())
		{
			cc.applySpezi(spezi);
		}
	}
	
	
	/**
	 * @return
	 */
	public static List<IConfigClient> getConfigClients()
	{
		return new ArrayList<IConfigClient>(INSTANCE.configs.values());
	}
}
