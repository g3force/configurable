/*
 * *********************************************************
 * Copyright (c) 2009 - 2011, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 25.11.2011
 * Author(s): Gero
 * *********************************************************
 */
package com.github.g3force.configurable;

import org.apache.commons.configuration.HierarchicalConfiguration;


/**
 * @author Gero
 */
public interface IConfigClient
{
	/**
	 * @return The common name of this {@link IConfigClient}
	 */
	String getName();
	
	
	/**
	 * @return The path to the config-file
	 */
	String getPath();
	
	
	/**
	 * Get configuration from file
	 * 
	 * @return
	 */
	HierarchicalConfiguration getFileConfig();
	
	
	/**
	 * Get configuration from file
	 * 
	 * @return
	 */
	HierarchicalConfiguration loadConfig();
	
	
	/**
	 * Get configuration from file
	 * 
	 * @return
	 */
	HierarchicalConfiguration getConfig();
	
	
	/**
	 * Read all fields from all registered classes
	 */
	void readClasses();
	
	
	/**
	 * Save to file
	 * 
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 * @return
	 */
	boolean saveCurrentConfig();
	
	
	/**
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 * @param observer
	 */
	void addObserver(final IConfigObserver observer);
	
	
	/**
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 * @param observer
	 */
	void removeObserver(final IConfigObserver observer);
}
